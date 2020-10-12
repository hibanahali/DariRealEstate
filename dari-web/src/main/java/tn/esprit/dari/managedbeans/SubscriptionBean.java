package tn.esprit.dari.managedbeans;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.xhtmlrenderer.pdf.ITextRenderer;
import tn.esprit.dari.entity.Subscription;
import tn.esprit.dari.entity.User;
import tn.esprit.dari.service.implt.SubscriptionService;
import tn.esprit.dari.service.implt.UserService;
import tn.esprit.dari.service.interf.UserServiceRemote;
import tn.esprit.dari.utils.Mail;

import java.util.Date;
import java.util.Arrays;

@ManagedBean(name = "SubscriptionBean")
@SessionScoped

public class SubscriptionBean implements Serializable {

	private static final long serialVersionUID = 1L;

	// private int idsub;
	private String subtype;
	// private List<User> users;
	private List<Subscription> sub;
	private Date subdate;
	private Date expdate;
	// private long price;
//	private String subdescription;

	Mail mail;

	@EJB
	UserService ur;

	@EJB
	SubscriptionService sr;

	String[] subtypes = new String[] { "Seller", "Locator", "realtor" };
	int[] priceperday = new int[] { 1200, 1489, 975 };

	Map<String, Integer> prices = IntStream.range(0, subtypes.length).boxed()
			.collect(Collectors.toMap(i -> subtypes[i], i -> priceperday[i]));

	public String addSub() {

		Date now = new Date();
		Subscription s = new Subscription();
		s.setSubdate(now);
		s.setUser(AuthenticationBean.LoggedPerson);
		mail.sendMail(s.getUser().getEmail(), "You are Subscribed To Dari RealEstate as a Locator", "subscription To DariRealEstate");
		if (Arrays.asList(subtypes).contains(subtype)) {
			if (expdate.after(now) && now.before(expdate)) {
				s.setSubtype(subtype);
				s.setPrice(prices.get(s.getSubtype()) * TimeUnit.DAYS
						.convert(Math.abs(s.getExpdate().getTime() - now.getTime()), TimeUnit.MILLISECONDS));

				s.setSubdescription("You are Subscribed to DariRealEstate as a " + s.getSubtype()
						+ "  your subscription price is :" + s.getPrice() + "  for a duration = " + TimeUnit.DAYS
								.convert(Math.abs(s.getExpdate().getTime() - now.getTime()), TimeUnit.MILLISECONDS)
						+ " Days");

			} else {
				FacesContext.getCurrentInstance().addMessage("form:btn",
						new FacesMessage("please set a date in the future !"));
			}
		} else {

			FacesContext.getCurrentInstance().addMessage("form:btn",
					new FacesMessage("please set a subtype : Realtor  / Seller / Locator"));

		}

		sr.addsub(s);
		String navigateTo = "/dari-web/dari/contract?faces-redirect=true";
		return navigateTo;
	}

	public void RemoveSub(Subscription s) {

		sr.deleteSub(s.getIdsub());

	}

	public void updatesub(Subscription db, Subscription js) {
		sr.updateSub(js, db.getIdsub());
	}

	public List<Subscription> getAllSub() {
		sub = sr.getAllSub();
		return sub;

	}

	public List<Subscription> findBySubtype(String type) {
		List<Subscription> sub = (List<Subscription>) sr.findBySubType(type);
		return sub;

	}

	/*
	 * public int getIdsub() { return idsub; }
	 */

	/*
	 * public void setIdsub(int idsub) { this.idsub = idsub; }
	 */

	public String getSubtype() {
		return subtype;
	}

	public void setSubtype(String subtype) {
		this.subtype = subtype;
	}

	/*
	 * public List<User> getUsers() { return users; }
	 */

	/*
	 * public void setUsers(List<User> users) { this.users = users; }
	 */

	public Date getSubdate() {
		return subdate;
	}

	public void setSubdate(Date subdate) {
		this.subdate = subdate;
	}

	public Date getExpdate() {
		return expdate;
	}

	public void setExpdate(Date expdate) {
		this.expdate = expdate;
	}

	/*
	 * public long getPrice() { return price; }
	 */

	/*
	 * public void setPrice(long price) { this.price = price; }
	 */

	/*
	 * public String getSubdescription() { return subdescription; }
	 * 
	 * public void setSubdescription(String subdescription) { this.subdescription =
	 * subdescription; }
	 */

}