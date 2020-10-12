package tn.esprit.dari.managedbeans;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import tn.esprit.dari.entity.Contract;
import tn.esprit.dari.entity.ContractModel;
import tn.esprit.dari.service.implt.UserService;
import tn.esprit.dari.service.interf.ContractServiceRemote;

//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
//import java.util.ArrayList;
//import java.util.Random;

import javax.faces.context.ExternalContext;
//import javax.servlet.http.Part;

import org.xhtmlrenderer.pdf.ITextRenderer;


@ManagedBean/*(name = "contractBean")*/
@SessionScoped
public class ContractBean implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@EJB  
	UserService ur;
	
	@EJB
	ContractServiceRemote cr;

	private Date enddate;
	private Date date;
	private Date now = new Date();
	
	public Date getDate() {
		return date;
	}


	public void setDate(Date date) {
		this.date = date;
	}



	private ContractModel model ;
    private List<Contract> contt;
    private Contract mycontract; 
    Contract contractToUpdate;
    
    private String description; 
	
	public String addContract() {
		Date now = new Date();
		Contract c = new Contract();
		c.setTypeCont(model);
		c.setEnddate(enddate);
		c.setStartdate(now);
		c.setUser(AuthenticationBean.LoggedPerson);
	
		
		if ( model.toString().equals("Guarantee")  )
	     {
	        		System.out.println("Case1: Value is: Guarentee");
	        		String description = "For value received ,"
	        				+ " DariRealEstate  the grantor do hereby grant , "
	        				+ "bargain sell and covery unto"
	        				+ c.getUser().getNom() +" , "+ c.getUser().getPrenom()  	
	        				+ "  , the Grantee , whose current address is Tunisia the"
	        				+ " following described premisies ,in Ariana County , "
	        				+ "In "+c.getStartdate();
	        		c.setDescription("here's your Contract containing your different guarentee elements");
	        		c.setPrice(description.length() * 7); 			
	     }
		else  if ( model.toString().equals("Renting")  )
		{
	        		System.out.println("Case2: Value is: Renting ");
	        		String description3 = "This Lease rental Agreement is made and executed by "
	        				+ c.getUser().getNom()+" " +c.getUser().getPrenom()
	        				+ " , leases the property by the agreement of DariRealEstate Rules . "
	        				+ "The agreement has been made for the period from "
	        				+" "+ c.getStartdate()+" to "+c.getEnddate()
	        				+ ". The tenant is responsible for the rent during this period . ";			
	        		c.setDescription("here's your Contract containing your different renting elements");
	        		c.setPrice(description3.length() * 7); 			
		}
		else  if ( model.toString().equals("Selling")  )
		{
	        	System.out.println("Case3: Value is: Selling ");
	        		String description2 = "THIS SALES CONTRACT, effective as of "
	        				+c.getStartdate()
	        				+ ", is made and entered into by and between DariRealEstate, "
	        				+ "a company organized and existing in Tunisia, Ariana, "
	        				+" And Mr " + c.getUser().getNom() +" "+c.getUser().getPrenom()
	        			    +"Now, Therefore, in consideration of the foregoing premises, "
	        			    + "and of the mutual promises and covenants herein contained, "
	        			    + "the parties, intending to be legally bound, do hereby agree";	
	        		c.setDescription("here's your Contract containing your different selling elements");
	        		c.setPrice(description2.length() * 7); 			
		}
		c.setDetails(c.getTypeCont()+" contract");
		cr.addcont(c); 
		String navigateTo = "/user/mycontracts?faces-redirect=true";
		return navigateTo ;
	}
	
	
	//Done 
	public void RemoveContract (Contract c) {
		System.out.println("this is just a test");
		cr.deleteCont(c.getId());
	}
	
	
	
	public void update(Contract c) {
		cr.updateCont(c, c.getId());	
	}
	
	
	
	public void updateEnd(Contract e) {
		System.out.println("***************");
		System.out.println("date " + date);
		System.out.println("***************");
		e.setEnddate(date);
		cr.updateEnd(e);
				
	}
	
	
	public List<Contract> getAllContracts() {
		contt = cr.getAllCont();
		System.out.println(contt);
		return contt;
	}
	
	public List<Contract> getMyContracts() {
		System.out.println("here just to test if the function getMyContracts is executed ");
		System.out.println(cr.findContractByUser(AuthenticationBean.LoggedPerson));
		return cr.findContractByUser(AuthenticationBean.LoggedPerson);
	}

	public Contract findByID(int id) {
		
		Contract cont = cr.findByID(id);
		
		return cont;
	}
	

	public void generatePdf(Contract c) {
		setMycontract(cr.findByID(c.getId()));
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext externalContext = facesContext.getExternalContext();
		HttpSession session = (HttpSession) externalContext.getSession(true);
		 
		String url = "http://localhost:9080/dari-web/user/contractpdf.jsf;jsessionid="+session.getId()+"?pdf=true";
		try {
			ITextRenderer renderer = new ITextRenderer();
			renderer.setDocument(new URL(url).toString());
			renderer.layout();
			HttpServletResponse	response = (HttpServletResponse) externalContext.getResponse();
			response.reset();
			response.setContentType("application/pdf");
			response.setHeader("Content-Disposition", "inline; filename=\"contract.pdf\"");
			OutputStream outputStream = response.getOutputStream();
			renderer.createPDF(outputStream);
			
			
		}catch (Exception ex) {
			ex.printStackTrace();
		}
		facesContext.responseComplete();
		
	}

	
	

	
	public List<Contract> findByContractType(ContractModel c) {
		
		List<Contract> cont = cr.findByContractType(model);
             return cont;		
		}

	public Date getEnddate() {
		return enddate;
	}

	public void setEnddate(Date enddate) {
		this.enddate = enddate;
	}

	public ContractModel getModel() {
		return model;
	}

	public void setModel(ContractModel model) {
		this.model = model;
	}



	public Contract getMycontract() {
		return mycontract;
	}



	public void setMycontract(Contract mycontract) {
		this.mycontract = mycontract;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public Date getNow() {
		return now;
	}


	public void setNow(Date now) {
		this.now = now;
	}
	
}