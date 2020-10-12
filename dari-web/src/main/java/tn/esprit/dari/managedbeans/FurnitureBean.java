package tn.esprit.dari.managedbeans;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.xhtmlrenderer.pdf.ITextRenderer;
import tn.esprit.dari.entity.Furniture;
import tn.esprit.dari.entity.User;
import tn.esprit.dari.service.implt.FurnitureService;
import tn.esprit.dari.service.implt.UserService;

@ManagedBean
@SessionScoped
public class FurnitureBean implements Serializable {
	
private static final long serialVersionUID = 1L;


private List <Furniture> furnitures;
private int id_furniture;
private List <User> users;
public  List<Furniture> cart = new ArrayList<Furniture>();
private float p=0;
private int n=0;
private float price;
private String name;
private String description;
private String picture="http://localhost/dari_images/user.png";
private Part image;


@EJB
UserService ur;

@EJB
FurnitureService fr;

public String addFurniture() {
	Furniture f = new Furniture();
	f.setNom(name);
	f.setDescription(description);
	f.setPrice(price);
	//f.setPicture(picture);
	f.setUser(AuthenticationBean.LoggedPerson);
	try {
		InputStream in = image.getInputStream();
		String name = image.getSubmittedFileName();
		String extension = name.substring(name.lastIndexOf("."));
		int ran = new Random().nextInt(999999999 + 1) + 1000000000;
		String newn = Integer.toString(ran);
		String finaln = newn + extension;
		File file = new File("/var/www/html/dari_images/"+finaln);
		file.createNewFile();
		FileOutputStream out = new FileOutputStream(file);
		
		byte[] buffer =new byte [1024];
		int length;
		
		while ((length = in.read(buffer))>0) {
			out.write(buffer,0,length);
		}
		
		out.close();
		in.close();
		f.setPicture("http://localhost/dari_images/"+finaln);
		
	}
	catch (Exception e) {
		e.printStackTrace(System.out);
	}
	
	fr.addFurniture(f);
	String navigateTo= "/user/myfurnitures?faces-redirect=true"; 
	return navigateTo;
	
}

public void ChangeStatus(Furniture f) {
	if (f.isStatus() == true)
	{
		fr.changestatus(f.getId_furniture(), false);
	}
	else 
	{
		fr.changestatus(f.getId_furniture(), true);
	}
}

public long getreportnumber(User u) {
	return fr.reportnumber(u);
}

public void report(Furniture f) {
	f.setReport(f.getReport()+1);
	fr.updateFurniturePrice(f);
}

public void chc(Furniture f) {
	if (f.isSold() == true) {
		f.setSold(false);
		f.setSoldu(false);
		fr.updateFurniturePrice(f);
	}
	else {
		f.setSold(true);
		f.setSoldu(true);
		fr.updateFurniturePrice(f);
	}
}

public long FurnituresNumber()
{
	long number = fr.getFurnitureNumber();
	return number;
	
}

public String revenu() {
	Double revenu = fr.revenue();
	String truncated = String.format("%.0f", revenu);
	return truncated;
}

public String revenudari() {
	Double revenu = fr.revenue();
	Double r = (revenu * 10) /100;
	String truncated = String.format("%.0f", r);
	return truncated;
}

public String benefit() {
	Double revenu = fr.revenue();
	Double r = (revenu * 7) /100;
	String truncated = String.format("%.0f", r);
	return truncated;
}

public long FurnituresNumberStatus(Boolean s)
{
	long numberST = fr.getFurnitureNumberBystatus(s);
	return numberST;
	
}

public String ActiveFurniturePercentage()
{
	long number = fr.getFurnitureNumber();
	long numberST = fr.getFurnitureNumberBystatus(true);
	float per1 = ((float)numberST/number)*100;
	String truncated = String.format("%.0f", per1);
	return truncated;
}

public String styleF() {
    return "width:"+ActiveFurniturePercentage()+"%";
}

public String RevenuRate()
{
	long number = Long.parseLong(revenu());
	long numberST = Long.parseLong(revenudari());
	float per1 = ((float)numberST/number)*100;
	String truncated = String.format("%.0f", per1);
	return truncated;
}

public String styleR() {
    return "width:"+RevenuRate()+"%";
}

public String BenefitRate()
{
	long number = Long.parseLong(revenu());
	long numberST = Long.parseLong(benefit());
	float per1 = ((float)numberST/number)*100;
	String truncated = String.format("%.0f", per1);
	return truncated;
}

public String styleB() {
    return "width:"+BenefitRate()+"%";
}


public void updatefurnitureprice(Furniture f) {
	
	f.setPrice(price);
    fr.updateFurniturePrice(f);
    price=0;
}

public void RemoveFurniture (Furniture f) {
	fr.DeleteFurniture(f.getId_furniture());
}

public void RemoveFurnitureAdmin (Furniture f) {
	fr.DeleteFurnitureAdmin(f.getId_furniture());
}


public String AddtoCart (int id_furniture) {
	String navigateTo = "null";
	if (AuthenticationBean.gLoggedIn() == false) {
		navigateTo= "/dari/sign-in?faces-redirect=true"; 
	}
	else {
	Furniture f = fr.findFurnitureById(id_furniture);
	cart.add(f);
	navigateTo= "/user/cart?faces-redirect=true"; 
	}
	return navigateTo;
}

public Boolean foundincart(int id_furniture) {
	Boolean found = false;
	for(Furniture i : cart) {
		if (i.getId_furniture() == id_furniture) {
			found=true;
		}
	}
	
	return found;
	
}

public void RemoveFromCart (int id_furniture) {
	cart.removeIf(obj -> obj.getId_furniture() == id_furniture);
}

public void ClearCart() {
	cart.clear();
}

public void ConfirmCart() {
	for(Furniture i : cart) {
		fr.sold(i.getId_furniture());
	}
	cart.clear();
}

public float pricetopay() {
	p=0;
	for(Furniture i : cart) {
		p=p+i.getPrice();
	}
	return p;
}

public int itemcart() {
	n=0;
	n= cart.size();
	
	return n;
}

public List<User> getUsers() {
users = ur.ListeUsers();
return users;
}

public List<Furniture> getFurnitures() {
	if (AuthenticationBean.gLoggedIn() == false) {
		furnitures = fr.findFurnitureBystatus(true) ;

	}
	else if (AuthenticationBean.gLoggedIn() == true) {
furnitures = fr.ListeFurnitures2(AuthenticationBean.LoggedPerson) ;
	}
return furnitures;
}

public  List<Furniture> getCart() {
	return cart;
}

public List<Furniture> getMyFurnitures() {
	furnitures = fr.ListeMyFurnitures(AuthenticationBean.LoggedPerson);
	return furnitures;
}

public List<Furniture> getAllFurnitures() {
	furnitures = fr.ListeFurnitures();
	return furnitures;
}

public void createrec() {
	FacesContext facesContext = FacesContext.getCurrentInstance();
	ExternalContext externalContext = facesContext.getExternalContext();
	HttpSession session = (HttpSession) externalContext.getSession(true);
	 
	String url = "http://localhost:9080/dari-web/user/rec.jsf;jsessionid="+session.getId()+"?pdf=true";
	try {
		ITextRenderer renderer = new ITextRenderer();
		renderer.setDocument(new URL(url).toString());
		renderer.layout();
		HttpServletResponse	response = (HttpServletResponse) externalContext.getResponse();
		response.reset();
		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "inline; filename=\"receipt.pdf\"");
		OutputStream outputStream = response.getOutputStream();
		renderer.createPDF(outputStream);
		
		
	}catch (Exception ex) {
		ex.printStackTrace();
	}
	facesContext.responseComplete();
	for(Furniture i : cart) {
		fr.sold(i.getId_furniture());
	}
	cart.clear();
}








public int getId_furniture() {
	return id_furniture;
}

public float getP() {
	return p;
}

public void setP(float p) {
	this.p = p;
}

public int getN() {
	return n;
}

public void setN(int n) {
	this.n = n;
}

public float getPrice() {
	return price;
}
public void setPrice(float price) {
	this.price = price;
}

public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}


public String getPicture() {
	return picture;
}
public void setPicture(String picture) {
	this.picture = picture;
}

public String getDescription() {
	return description;
}
public void setDescription(String description) {
	this.description = description;
}

public Part getImage() {
	return image;
}
public void setImage(Part image) {
	this.image = image;
}

}
