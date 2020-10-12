package tn.esprit.dari.api;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import tn.esprit.dari.entity.Furniture;
import tn.esprit.dari.entity.Role;
import tn.esprit.dari.entity.User;
import tn.esprit.dari.rest.Secured;
import tn.esprit.dari.service.interf.FurnitureServiceRemote;

@Path("Furniture")
public class FurnitureServiceApi {
	
	public static List<Furniture> cart = new ArrayList<Furniture>();

	@EJB
	FurnitureServiceRemote fr;

	@POST
	@Secured(Permissions = Role.ROLE_a0)
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("addtocart/{id}")
	public Response AddToCart(@PathParam("id")int id) {
		Furniture f = fr.findFurnitureById(id);
		Furniture f1 = new Furniture(f.getId_furniture(),f.getNom(), f.getDescription(), f.getPrice(), f.getPicture());
		cart.add(f1);
		return Response.status(Status.CREATED).entity(cart).build();
		
	}
	
	@POST
	@Secured(Permissions = Role.ROLE_a0)
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("removefromcart/{id}")
	public Response RemoveFromCart(@PathParam("id")int id) {
		cart.removeIf(obj -> obj.getId_furniture() == id);
		return Response.status(Status.CREATED).entity(cart).build();
		
	}
	
	@POST
	@Secured(Permissions = Role.ROLE_a0)
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("clearcart")
	public Response ClearCart() {
		cart.clear();
		return Response.status(Status.CREATED).entity(cart).build();
		
	}
	
	
	@POST
	@Secured(Permissions = Role.ROLE_a0)
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("confirmcart")
	public Response ConfirmCart() {
		for(Furniture i : cart) {
			fr.sold(i.getId_furniture());
		}
		cart.clear();
		return Response.status(Status.CREATED).entity("payment established successfully").build();
		
	}
	
	@GET
	@Secured(Permissions = Role.ROLE_a0)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("getcart")
	public Response getcart() {
		if(cart == null)
		{
			return Response.status(Status.NOT_FOUND)
					.entity("No Cart")
					.build();
		}
		else if (cart.isEmpty())
		{
			return Response.status(Status.NO_CONTENT)
					.entity("Cart is empty")
					.build();
		}
		return Response.status(Status.OK)
				.entity(cart)
				.build();
	}
	
	@GET
	@Path("getFurniture")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getFurniture() {
		List<Furniture> f1 = fr.ListeFurnitures();
		List<Furniture> lf = new ArrayList<Furniture>();
		for(Furniture i : f1)
		{
			User u = new User(i.getUser().getPrenom(), i.getUser().getNom());
			Furniture f = new Furniture(i.getId_furniture(),i.getNom(), i.getDescription(), i.getPrice(), i.getPicture(),i.getCreation_date(),u);
			
			lf.add(f);
			
		}
		
		return Response.status(Status.FOUND).entity(lf).build();	
	}
	
	@POST
	@Secured(Permissions = Role.ROLE_a0)
	@Path("add")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addUser(Furniture f) {

		User u = AuthenticationRessource.LoggedPerson;
		System.out.println(u.getId_user());
		f.setUser(u);
		fr.addFurniture(f);

		return Response.status(Status.CREATED).entity(f).build();
	}
	
	@GET
	@Path("activate/{encoded_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response activate(@PathParam("encoded_id")String encoded_id) {
		byte[] decodedBytes = Base64.getDecoder().decode(encoded_id);
		String decodedString = new String(decodedBytes);
		Integer id = Integer.valueOf(decodedString);	 
		fr.activate(id);
		return Response.status(Status.OK).entity("your furniture post has been activated").build();	
	}
	
	@POST
	@Secured(Permissions = Role.ROLE_a0)
	@Path("deletemyfurniture/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response DeleteAccount(@PathParam("id")int id) {
		
		User u = AuthenticationRessource.LoggedPerson;
		Furniture f = fr.findFurnitureById(id);
		if (f.getUser().getId_user() == u.getId_user()) 
		{
		    fr.DeleteFurniture(id);
		
			return Response.status(Status.OK).entity("Your Furniture has been Deleted").build();
		}
		else
		{
			return Response.status(Status.NOT_FOUND).entity("You can't delete this furniture").build();
		}	
	}
	
	@POST
	@Secured(Permissions = Role.ROLE_a0)
	@Path("updatefurniture/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response UpdateAccount(@HeaderParam("price") Float price, @PathParam("id")int id) {
		
		User u = AuthenticationRessource.LoggedPerson;
		Furniture f = fr.findFurnitureById(id);
		if (f.getUser().getId_user() == u.getId_user()) 
		{
			f.setPrice(price);
		    fr.updateFurniturePrice(f);
		
			return Response.status(Status.OK).entity("Your Furniture pricehas been Updated").build();
		}
		else
		{
			return Response.status(Status.NOT_FOUND).entity("You can't update this furniture").build();
		}
	}

}
