package tn.esprit.dari.api;


import java.util.Base64;

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

import tn.esprit.dari.entity.Role;
import tn.esprit.dari.entity.User;
import tn.esprit.dari.rest.Secured;
import tn.esprit.dari.service.interf.UserServiceRemote;
import tn.esprit.dari.utils.BCrypt;

@Path("User")
public class UserServiceApi {
	@EJB
	UserServiceRemote ur;
	
	
	
	@POST
	@Path("Register")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	 public Response addUser(User u) {
		if ( u.getNom() == "" || u.getPrenom() == "" || u.getEmail() == "" || u.getPassword() == "")
		{
			
			return Response.status(Status.FORBIDDEN).entity("Please fill all the required fields").build();
		}
		else if (!u.getNom().matches("^[a-zA-Z\\s]*$") || !u.getPrenom().matches("^[a-zA-Z\\s]*$"))
		{
			return Response.status(Status.FORBIDDEN).entity("First Name and Last name must contain only letters and spaces").build();
		}
		else if (!u.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$"))
		{
			return Response.status(Status.FORBIDDEN).entity("Please Enter a valid email").build();
		}
		else if (!u.getPassword().matches("((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!]).{8,40})"))
		{
			return Response.status(Status.FORBIDDEN).entity("Please Enter a valid password \nBetween 8 and 40 characters long\nContain at least one digit\nContain at least one lower case character\nContain at least one upper case character\nContain at least one special character from (@ # $ % ! . )").build();
		}
		else {
		if (ur.findUserByEmail(u.getEmail()) == null) 
		{
			
		ur.register(u);
			return Response.status(Status.CREATED).entity(u).build();	
		}
		else {
			return Response.status(Status.FOUND).entity("An account with this email exists").build();
		}
		}
	}
	
	
	@GET
	@Path("activate/{encoded_id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response activate(@PathParam("encoded_id")String encoded_id) {
		byte[] decodedBytes = Base64.getDecoder().decode(encoded_id);
		String decodedString = new String(decodedBytes);
		Integer id = Integer.valueOf(decodedString);	 
		ur.activate(id);
		return Response.status(Status.OK).entity("your account has been activated").build();	
	}
	
	
	
	
	@POST
	@Secured(Permissions = Role.ROLE_a0)
	@Path("deletemyaccount")
	@Produces(MediaType.APPLICATION_JSON)
	public Response DeleteAccount(@HeaderParam("password") String password) {
		
		User u = AuthenticationRessource.LoggedPerson;
		Boolean v = BCrypt.checkpw(password,u.getPassword());
		if (v == true) 
		{
		    ur.DeleteUser(u.getEmail());
		
			return Response.status(Status.OK).entity("Your Account has been Deleted").build();
		}
		else
		{
			return Response.status(Status.NOT_FOUND).entity("Please enter a valid password for your account").build();
		}	
	}
	
	@POST
	@Secured(Permissions = Role.ROLE_a0)
	@Path("updateaccount")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response UpdateAccount(@HeaderParam("nom") String nom ,@HeaderParam("prenom") String prenom, @HeaderParam("password") String password , @HeaderParam("newpassword") String newpassword, @HeaderParam("picture") String picture) {
		
		User u = AuthenticationRessource.LoggedPerson;
		Boolean v = BCrypt.checkpw(password,u.getPassword());
		
		if (v == true)
		{
			if (nom != null && nom.matches("^[a-zA-Z\\s]*$"))
			{
				u.setNom(nom);
			}
			if (prenom != null && prenom.matches("^[a-zA-Z\\s]*$"))
			{
				u.setPrenom(prenom);
			}
			if (picture != null)
			{
				u.setProfile_image(picture);
			}
			if (newpassword != null && newpassword.matches("((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!]).{8,40})"))
			{
				String hashed = BCrypt.hashpw(newpassword, BCrypt.gensalt());
				u.setPassword(hashed);
			}
			
			ur.updateUser(u);
			return Response.status(Status.OK).entity(u).build();
		}
		
		else
		{
			return Response.status(Status.NOT_FOUND).entity("Please enter a valid password for your account").build();
		}	
	}
}
