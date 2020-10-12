package tn.esprit.dari.api;

import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import tn.esprit.dari.entity.Contract;
import tn.esprit.dari.entity.ContractModel;
import tn.esprit.dari.entity.User;
import tn.esprit.dari.service.interf.ContractServiceRemote;
import tn.esprit.dari.service.interf.UserServiceRemote;


@Path("Cont")
public class ContractServiceApi {

	
	@EJB 
	ContractServiceRemote cr ;
	
	@EJB
	UserServiceRemote ur ;
	
	
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{idu}/add")
	@Produces(MediaType.APPLICATION_JSON)
	public Response crContract(Contract c , @PathParam("idu") int idu )
	{
		Date now = new Date();
		ContractModel models[] =  ContractModel.values() ;
		if ( ur.findUserById(idu) == null ) {
			return Response.status(Status.FORBIDDEN)
					.entity("please sign in to get your contract")
					.build();
		}
		else if (!(Arrays.asList(models).contains(c.getTypeCont())) ){
			return Response.status(Status.FORBIDDEN)
					.entity("this type is not supported")
					.build();
		}
		else if (!(c.getEnddate().after(now) && now.before(c.getEnddate()))) {
			return Response.status(406)
					.entity("Please verify the end date of ur contract !")
					.build();
		}
		else {
			 
			c.setStartdate(now);
			c.setUser(ur.findUserById(idu));
			 
			
			if ( c.getTypeCont().toString().equals("Guarantee")  )
		     {
		        		System.out.println("Case1: Value is: Guarentee");
		        		String description = /*"For value received ,"
		        				+ " ImmoDari  the grantor do hereby grant , "
		        				+ "bargain sell and covery unto"
		        				+ */c.getUser().getNom() +" , "+ c.getUser().getPrenom()  	
		        				+ "  , the Grantee , whose current address is Tunisia the"
		        				/*+ " following described premisies ,in Ariana County , "*/
		        				+ "In "+c.getStartdate();
		        		c.setDescription(description);
		     }
			else  if ( c.getTypeCont().toString().equals("Renting")  )
			{
		        		System.out.println("Case2: Value is: Renting ");
		        		String description3 = "This Lease rental Agreement is made and executed by "
		        				+ c.getUser().getNom()+" " +c.getUser().getPrenom()
		        				/*+ " , leases the property by the agreement of ImmoDari Rules . "
		        				+ "The agreement has been made for the period from "*/
		        				+" "+ c.getStartdate()+" to "+c.getEnddate();
		        				/*+ ". The tenant is responsible for the rent during this period . ";	*/		
		        		c.setDescription(description3);
			}
			else  if ( c.getTypeCont().toString().equals("Selling")  )
			{
		        	System.out.println("Case3: Value is: Selling ");
		        		String description2 = "THIS SALES CONTRACT, effective as of "
		        				+c.getStartdate();
		        				/*+ ", is made and entered into by and between ImmoDari, "
		        				+ "a company organized and existing in Tunisia, Ariana, "
		        				+" And Mr " + c.getUser().getNom() +" "+c.getUser().getPrenom()
		        			    +"Now, Therefore, in consideration of the foregoing premises, "
		        			    + "and of the mutual promises and covenants herein contained, "
		        			    + "the parties, intending to be legally bound, do hereby agree";*/		
		        		c.setDescription(description2);
			}else {
				System.out.println("Default: Value is: No type of contract so an empty description gonna make");
		        String description1 = "here just to test";			
		        c.setDescription(description1);
		      }
			c.setPrice(c.getDescription().length() * 7); 			
			cr.addcont(c);
			return Response.status(Status.OK)
					.entity(c)
					.build();
			
		}
		
	}
		

	    @GET
	    @Path("/downloads/{id}")
	    @Produces("application/pdf")
	    public Response getFile(@PathParam("id")int id) {
	    	
	    	String x = cr.findDetailsByCntId(id);
	    	
	        File f = new File("C:\\Users\\Acer\\Downloads\\"+x+".pdf");
	        if(f.exists() && !f.isDirectory()) {
	        ResponseBuilder response = Response.ok((Object) f);
	        response.header("Content-Disposition",
	            "attachment; filename=\"contract.pdf\"");
	        return response.build(); 
	        }
	        else {
			return Response.status(200)
					.entity("Sorry this file does not exist")
					.build();
	        }
	    }
	
	    
	    
 
	
	@DELETE
	@Path("delete/{id}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response delete(@PathParam("id")int id)
	{
		cr.deleteCont(id);
		return Response.status(Status.OK)
				.entity("Okay Delete Done s")
				.build();
	}
  
	@GET
	@Path("readall")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Contract> getAllCont(){ //readAllSubs
		return cr.getAllCont();
	}
	
	@PUT
	@Path("update/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public Response updateSub(Contract s , @PathParam("id") int id  )
	{
		cr.updateCont(s,id);		
		return Response.status(Status.ACCEPTED)
				.entity("update done this is your new contract "+ s)
				.build();
	}
			
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("findbyid/{id}")
	public Contract get(@PathParam(value="id") int  id)
	{	
		return cr.findByID(id);
	
	}
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("findbytype/{type}")
	public Response searchtype(@PathParam("type") ContractModel t) {
		// search by type 
		List<Contract> l = cr.findByContractType(t);
		return Response.status(Status.OK)
				.entity(l)
				.build();

	}
	
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("findbyuser/{User}")
	public Response searchContractByUser(@PathParam("User") int id) {
		// search by type 
		User u = ur.findUserById(id);
		List<Contract> l = cr.findContractByUser(u);
		return Response.status(Status.OK)
				.entity(l)
				.build();
	}
	
}
