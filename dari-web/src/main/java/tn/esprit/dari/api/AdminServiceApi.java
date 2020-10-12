package tn.esprit.dari.api;



import javax.ejb.EJB;
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
import tn.esprit.dari.service.interf.UserServiceRemote;

@Secured(Permissions = Role.ROLE_a1)
@Path("D033E22AE348AEB5660FC2140AEC35850C4DA997")
public class AdminServiceApi {
	@EJB
	UserServiceRemote ur;
	
	@EJB
	FurnitureServiceRemote fr;
	
	
	
	
	@GET
	@Secured(Permissions = Role.ROLE_a1)
	@Path("get")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUsers() {
		return Response.status(Status.FOUND).entity(ur.ListeUsers()).build();	
	}
	
	@GET
	@Secured(Permissions = Role.ROLE_a1)
	@Path("getFurniture")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getFurniture() {
		return Response.status(Status.FOUND).entity(fr.ListeFurnitures()).build();	
	}
	
	@GET
	@Secured(Permissions = Role.ROLE_a1)
	@Path("getInactiveUsers")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getInactiveUsers() {
		return Response.status(Status.FOUND).entity(ur.InactiveUser()).build();	
	}
	
	@GET
	@Secured(Permissions = Role.ROLE_a1)
	@Path("getNumber")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUsersNumber() {
		return Response.status(Status.FOUND).entity(ur.getUsersNumber()).build();	
	}
	
	@GET
	@Secured(Permissions = Role.ROLE_a1)
	@Path("getByStatus/{status}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUsersByStatus(@PathParam("status") Boolean status) {
		return Response.status(Status.FOUND).entity(ur.findUserBystatus(status)).build();	
	}
	
	@GET
	@Secured(Permissions = Role.ROLE_a1)
	@Path("getFrByStatus/{status}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getFurnitureByStatus(@PathParam("status") Boolean status) {
		return Response.status(Status.FOUND).entity(fr.findFurnitureBystatus(status)).build();	
	}
	
	
	@GET
	@Secured(Permissions = Role.ROLE_a1)
	@Path("getNumberByStatus/{status}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUsersNumberByStatus(@PathParam("status") Boolean status) {
		return Response.status(Status.FOUND).entity(ur.getUsersNumberBystatus(status)).build();	
	}
	
	@GET
	@Secured(Permissions = Role.ROLE_a1)
	@Path("getById/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUserById(@PathParam("id")int id) {
		return Response.status(Status.FOUND).entity(ur.findUserById(id)).build();	
	}
	
	@GET
	@Secured(Permissions = Role.ROLE_a1)
	@Path("getByEmail/{email}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUserByEmail(@PathParam("email")String email) {
		if (ur.findUserByEmail(email) == null) 
		{
			
			return Response.status(Status.NOT_FOUND).entity("An account with this email does not exist").build();	
		}
		else {
			return Response.status(Status.OK).entity(ur.findUserByEmail(email)).build();
		}
	}
	
	@POST
	@Secured(Permissions = Role.ROLE_a1)
	@Path("changeUserStatus")
	@Produces(MediaType.APPLICATION_JSON)
	public Response changestatus(@HeaderParam("email") String email) {
		if (ur.findUserByEmail(email) == null) 
		{
			
			return Response.status(Status.NOT_FOUND).entity("An account with this email does not exist").build();	
		}
		else {
			User u = ur.findUserByEmail(email);
		if (u.isStatus() == true)
		{
			ur.changestatus(u.getId_user(), false);
		return Response.status(Status.OK).entity("User Deactivated Successfully").build();
		}
		else 
		{
			ur.changestatus(u.getId_user(), true);
		 return Response.status(Status.OK).entity("User Activated Successfully").build();
		}
		
		}
	}
	
	@POST
	@Secured(Permissions = Role.ROLE_a1)
	@Path("changeFurnitureStatus")
	@Produces(MediaType.APPLICATION_JSON)
	public Response changefurniturestatus(@HeaderParam("id") int id) {
		if (fr.findFurnitureById(id) == null) 
		{
			
			return Response.status(Status.NOT_FOUND).entity("Furniture not found").build();	
		}
		else {
			Furniture f = fr.findFurnitureById(id);
		if (f.isStatus() == true)
		{
			fr.changestatus(f.getId_furniture(), false);
		return Response.status(Status.OK).entity("Furniture Post Deactivated Successfully").build();
		}
		else 
		{
			fr.changestatus(f.getId_furniture(), true);
		 return Response.status(Status.OK).entity("Furniture Post Activated Successfully").build();
		}
		
		}
	}
	
	@POST
	@Secured(Permissions = Role.ROLE_a1)
	@Path("delete")
	@Produces(MediaType.APPLICATION_JSON)
	public Response DeleteUser(@HeaderParam("email") String email) {
		
		if (ur.DeleteUser(email) == null) 
		{
			
			return Response.status(Status.NOT_FOUND).entity("An account with this email does not exist").build();	
		}
		else {
			return Response.status(Status.OK).entity("User Deleted").build();
		}
	}
	
	@POST
	@Secured(Permissions = Role.ROLE_a1)
	@Path("deleteFr")
	@Produces(MediaType.APPLICATION_JSON)
	public Response DeleteFurniture(@HeaderParam("id") int id) {
		
		if (fr.DeleteFurniture(id) == null) 
		{
			
			return Response.status(Status.NOT_FOUND).entity("Furniture Post Not Found").build();	
		}
		else {
			return Response.status(Status.OK).entity("Furniture Post Deleted").build();
		}
	}
	
}
