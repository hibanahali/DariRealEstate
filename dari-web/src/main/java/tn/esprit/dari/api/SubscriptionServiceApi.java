package tn.esprit.dari.api;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import tn.esprit.dari.entity.Subscription;
import tn.esprit.dari.service.interf.SubServiceRemote;
import tn.esprit.dari.service.interf.UserServiceRemote;
import tn.esprit.dari.api.AuthenticationRessource;
import tn.esprit.dari.utils.Mail;

@Path("sub")
public class SubscriptionServiceApi {

	AuthenticationRessource x;

	Mail mail;

	@EJB
	SubServiceRemote sr;

	@EJB
	UserServiceRemote ur;

	String[] subtypes = new String[] { "Seller", "Locator", "realtor" };
	int[] priceperday = new int[] { 1200, 1489, 975 };

	Map<String, Integer> prices = IntStream.range(0, subtypes.length).boxed()
			.collect(Collectors.toMap(i -> subtypes[i], i -> priceperday[i]));

	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{idu}/add")
	@Produces(MediaType.APPLICATION_JSON)
	public Response crSub(Subscription s, @PathParam("idu") int id) {
		Date now = new Date();
		s.setSubdate(now);
		if (Arrays.asList(subtypes).contains(s.getSubtype())) {
			if (s.getExpdate().after(now) && now.before(s.getExpdate())) {

				s.setPrice(prices.get(s.getSubtype()) * TimeUnit.DAYS
						.convert(Math.abs(s.getExpdate().getTime() - now.getTime()), TimeUnit.MILLISECONDS));

				s.setSubdescription("You are Subscribed with DariRealEstate as a " + s.getSubtype()
						+ "  your subscription price is :" + s.getPrice() + "  for a duration = " + TimeUnit.DAYS
								.convert(Math.abs(s.getExpdate().getTime() - now.getTime()), TimeUnit.MILLISECONDS)
						+ " Days");
				if (ur.findUserById(id) != null) {

					s.setUser(ur.findUserById(id));
					Mail.sendMail(s.getUser().getEmail(), s.getSubdescription(), "Subscription To DariRealEstate");

				} else {
					return Response.status(406)
							.entity("Please Authenticate first this subscription creation is not allowed !").build();
				}
				sr.addsub(s);
				return Response.status(Status.OK).entity(s).build();
			} else {
				return Response.status(406).entity("Please set a future date of expiration! ").build();
			}
		} else {

			return Response.status(406).entity("Subscription type not defined! ").build();
		}
	}

	@DELETE
	@Path("delete/{id}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response delete(@PathParam("id") int id) {
		sr.deleteSub(id);
		return Response.status(Status.OK).entity("Okay Delete Done s").build();
	}

	@GET
	@Path("readall")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Subscription> readAllSubs() {
		System.out.println(prices);
		return sr.getAllSub();
	}

	@PUT
	@Path("update/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public Response updateSub(Subscription s, @PathParam("id") int id) {
		Date now = new Date();
		s.setSubdate(now);
		if (Arrays.asList(subtypes).contains(s.getSubtype())) {
			if (s.getExpdate().after(now) && now.before(s.getExpdate())) {

				sr.updateSub(s, id);
				return Response.status(Status.ACCEPTED).entity("update done").build();
			} else {
				return Response.status(406).entity("Please set a future date of expiration! ").build();
			}
		} else {

			return Response.status(406).entity("Subscription type not defined! ").build();
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("findbyid/{id}")
	public Subscription get(@PathParam(value = "id") int id) { 
		return sr.findByID(id);

	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("findbyt/{type}")
	public List<Subscription> searchtype(@PathParam("type") String t) {
		
		return sr.findBySubType(t);

	}

}