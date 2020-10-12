package tn.esprit.dari.api;

import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import tn.esprit.dari.entity.Insurance;
import tn.esprit.dari.service.interf.InsuranceServiceRemote;
import tn.esprit.dari.service.interf.UserServiceRemote;
import tn.esprit.dari.utils.SmsApi;

@Path("ins")
public class InsuranceServiceApi {

	SmsApi sms;

	@EJB // interface
	InsuranceServiceRemote inr;

	@EJB
	UserServiceRemote ur;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("{idu}/add")
	@Produces(MediaType.APPLICATION_JSON)
	public Response crIns(Insurance i, @PathParam("idu") int idu) {

		Date now = new Date();

		if (ur.findUserById(idu) == null) {
			return Response.status(Status.FORBIDDEN).entity("Please enter the suitable IDuser").build();
		} else if (!(i.getEnddate().after(now) && now.before(i.getEnddate()))) {
			return Response.status(406).entity("Please verify the end date of ur Insurance !").build();
		} else if (i.getNum() == null | i.getNum().length() != 8) {
			return Response.status(406).entity("Please verify your phone number !").build();
		} else {
			i.setStartdate(now);
			i.setEntreprise("Trust");
			i.setUser(ur.findUserById(idu));
			i.setNum("216" + i.getNum());
			char randomLetter = (char) ('a' + Math.random() * ('z' - 'a' + 1));
			System.out.println("the random caracter is = " + randomLetter);
			String code = Integer.toString(1 + (int) (Math.random() * (99))) + randomLetter;
			String encodedString = Base64.getEncoder().encodeToString(code.getBytes());
			System.out.println(code);
			System.out.println(encodedString);
			i.setCode(encodedString);
			i.setPrice(400.8f
					* TimeUnit.DAYS.convert(Math.abs(i.getEnddate().getTime() - now.getTime()), TimeUnit.MILLISECONDS));
			inr.addIns(i);
			System.out.println(i.getNum());
			System.out.println(i.getCode());
			String message = "Your code is : " + code;
			System.out.println(message);

			sms.sendSmsbyvonage("DariRealEstate", i.getNum(), message);

			return Response.status(Status.OK).entity(i).build();
		}
	}

	@GET
	@Path("act/{id}") 
	@Produces(MediaType.APPLICATION_JSON)
	public Response activate(@PathParam("id") int id, @QueryParam(value = "code") String decoded_code) {

		// Insurance i = inr.findByID(id);//insurance from database of id entered by
		// client
		// String x = i.getCode();//code of insurance
		// byte[] decodedBytes = Base64.getDecoder().decode(x);//decode x from string to
		// byte by base64
		// String decodedfromdatabase = new String(decodedBytes); // transform data from
		// byte to string
		// or
		String decodedfromdatabase = new String(Base64.getDecoder().decode(inr.findByID(id).getCode()));
		System.out.println(decodedfromdatabase.length());
		System.out.println(decoded_code.length());
		System.out.println(decoded_code.equals(decodedfromdatabase));
		System.out.println(decoded_code == decodedfromdatabase);
		if (decodedfromdatabase.equals(decoded_code)) {
			inr.activateInsurance(id);
			return Response.status(Status.OK).entity("Your insurance has been activated").build();
		} else {
			return Response.status(Status.FORBIDDEN).entity("Please verify your code ! it's wrong").build();
		}
	}

	@DELETE
	@Path("delete/{id}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response delete(@PathParam("id") int id) {
		inr.deleteIns(id);
		return Response.status(Status.OK).entity("Okay Delete Done s").build();
	}

	@GET
	@Path("readall")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Insurance> readAllIns() {
		return inr.getAllIns();
	}

	@PUT
	@Path("update/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public Response updateIns(Insurance i, @PathParam("id") int id) {
		Date now = new Date();

		if ((i.getNum() == null) & (i.getEnddate() == null)) {
			return Response.status(406).entity("fill in the field !").build();
		} else {

			if (((now.before(i.getEnddate()) && i.getEnddate().after(now)))
					| i.getEnddate() != inr.findByID(id).getEnddate()) {
				inr.updateIns(i, id);
				return Response.status(Status.ACCEPTED).entity("update done").build();
			} else {
				return Response.status(406)
						.entity("Please Verify the End Date , it's in the past ;) or you put the same Tel number")
						.build();
			}
		}
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("findbyid/{id}")
	public Insurance get(@PathParam(value = "id") int id) { // find insurance by id
		return inr.findByID(id);

	}

}
