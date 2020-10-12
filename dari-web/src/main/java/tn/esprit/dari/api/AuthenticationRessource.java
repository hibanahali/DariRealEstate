package tn.esprit.dari.api;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import javax.crypto.spec.SecretKeySpec;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import tn.esprit.dari.entity.User;
import tn.esprit.dari.entity.Role;
import tn.esprit.dari.service.interf.AuthenticationServiceRemote;
import tn.esprit.dari.service.interf.UserServiceRemote;

@Path("Login")
public class AuthenticationRessource {

	@EJB
	AuthenticationServiceRemote local ;
	
	@EJB
	UserServiceRemote ur;
	
	public static User LoggedPerson;
	
	@Context
	private UriInfo uriInfo; 

	@POST
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response authenticateUser(@HeaderParam("email") String email, @HeaderParam("password") String password) {	
			User user = authenticate(email, password);
			
			if ( email == "" || password == "")
			{
				
				return Response.status(Status.FORBIDDEN).entity("Please fill all the required fields").build();
			}
			
			else if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$"))
			{
				return Response.status(Status.FORBIDDEN).entity("Please Enter a valid email").build();
			}
			
			else if (!password.matches("((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#$%!]).{8,40})"))
			{
				return Response.status(Status.FORBIDDEN).entity("Please Enter a valid password").build();
			}
			
			else {
			
			 if (ur.findUserByEmail(email) == null)
			{
				
				return Response.status(Response.Status.NOT_FOUND).entity("Email not found").build();
			}
			 else if (user.isStatus()==false)
			 {
				 System.out.println(user.isStatus());
				return Response.status(Response.Status.FORBIDDEN).entity("User Not Activated Yet").build();
			}
			 else if (user.getId_user()!=0) {
				if (user.getRole() == Role.ROLE_a0) {
					Date currentDate = new Date();
					user.setLast_login(currentDate);
					ur.updateUser(user);
				String token = issueToken(email,user.getId_user(),"ROLE_a0");
				return Response.ok(token).build();
				
				}
				else {
					Date currentDate = new Date();
					user.setLast_login(currentDate);
					ur.updateUser(user);
					if (!ur.InactiveUser().isEmpty()) {
						List <User> lu = ur.InactiveUser();
						for(User u : lu){
							if (u.isStatus() == true) {
								ur.changestatusInactive(u.getId_user());
							}
							
						}
					}
					String token = issueToken(email,user.getId_user(),"ROLE_a1");
					return Response.ok(token).build();
				}
			}
			
			
		   else 
			{
				return Response.status(Response.Status.FORBIDDEN).entity("Wrong credentials").build();
			}
			 
			}
	}

	private User authenticate(String email, String password) {
		User user = local.authentificationUser(email, password);
		if (user != null) {
			LoggedPerson = user;
		}
		return user;
	}

	private String issueToken(String email,int id,String Role) {
		
		 
		  String keyString = "simplekey";
		  SecretKeySpec key = new SecretKeySpec(keyString.getBytes(), 0, keyString.getBytes().length, "DES");
		  System.out.println("the key is : " + key.hashCode()); 
		 
		  System.out.println("uriInfo.getAbsolutePath().toString() : " + uriInfo.getAbsolutePath().toString());
		  System.out.println("Expiration date: " + toDate(LocalDateTime.now().plusMinutes(15L))); 
//		  .setId(Integer.toString(id))
		  String jwtToken = Jwts.builder().setAudience(Role).setId(Integer.toString(id)).setSubject(email).setIssuer(uriInfo.getAbsolutePath().toString()).setIssuedAt(new Date()).setExpiration(toDate(LocalDateTime.now().plusMinutes(10000L))).signWith(SignatureAlgorithm.HS512, key).compact(); 
		 
		  System.out.println("the returned token is : " + jwtToken);  
		  return jwtToken;  }

	private Date toDate(LocalDateTime localDateTime) { 
		return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()); 
		}
	public User getLoggedPerson() {
		if (LoggedPerson == null) {
			return null;
		} else {
			return LoggedPerson;
		}}
}
