package tn.esprit.dari.managedbeans;

import java.io.Serializable;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
//import javax.ws.rs.core.Response;
//import javax.ws.rs.core.Response.Status;

import tn.esprit.dari.entity.Insurance;

import tn.esprit.dari.service.implt.InsuranceService;
import tn.esprit.dari.service.implt.UserService;
import tn.esprit.dari.utils.Mail;
import tn.esprit.dari.utils.SmsApi;

@ManagedBean(name = "InsuranceBean")
@SessionScoped

public class InsuranceBean implements Serializable {

	Mail mail;
	SmsApi s;

	private static final long serialVersionUID = 1L;

	@EJB
	UserService ur;

	@EJB
	InsuranceService inr;

	private Date enddate;
	private String code;
	private String num;
	private List<Insurance> I;
	// private Float price;
	// private User user = AuthenticationBean.LoggedPerson;

	Insurance i;

	public String createI() {

		Date now = new Date();
		i = new Insurance();
		i.setStartdate(now);
		i.setEntreprise("Trust");
		if (!(enddate.after(now) && now.before(enddate))) {
			FacesContext.getCurrentInstance().addMessage("form:btn",
					new FacesMessage("Please verify the end date of ur Insurance !"));
		} else if (num == null | num.length() != 8) {
			FacesContext.getCurrentInstance().addMessage("form:btn",
					new FacesMessage("Please verify your phone number !"));
		} else {
			i.setEnddate(enddate);
			i.setNum("216" + num);
			char randomLetter = (char) ('a' + Math.random() * ('z' - 'a' + 1));
			System.out.println("the random caracter is = " + randomLetter);
			String code = Integer.toString(1 + (int) (Math.random() * (99))) + randomLetter;
			String encodedString = Base64.getEncoder().encodeToString(code.getBytes());
			i.setCode(encodedString);
			i.setPrice(400.8f
					* TimeUnit.DAYS.convert(Math.abs(i.getEnddate().getTime() - now.getTime()), TimeUnit.MILLISECONDS));

			inr.addIns(i);
			String message = "your Insurance validation code is : " + code;

			 s.sendSmsbyvonage("DariRealEstate", i.getNum(), message);
			mail.sendMail(AuthenticationBean.LoggedPerson.getEmail(), "your Insurance validation code is :" + code,
					"validate insurance");
		}

		String navigateTo = "/user/confirminsurance?faces-redirect=true";
		return navigateTo;
	}

	public void RemoveInsurance(Insurance i) {
		inr.deleteIns(i.getId());
	}

	public void updateI(Insurance i, Insurance ii) {
		inr.updateIns(ii, i.getId());

	}

	public List<Insurance> getAllIn() {
		I = inr.getAllIns();
		return I;

	}

	public Insurance findById(int id) {

		Insurance I = inr.findByID(id);
		return I;
	}

	public void activI(int id, String code) {
		String decodedfromdatabase = new String(Base64.getDecoder().decode(inr.findByID(id).getCode()));
		if (decodedfromdatabase.equals(code)) {
			inr.activateInsurance(id);
			FacesContext.getCurrentInstance().addMessage("form:btn",
					new FacesMessage("Your insurance has been activated"));
		} else {
			FacesContext.getCurrentInstance().addMessage("form:btn",
					new FacesMessage("Please verify your code ! it's wrong"));
		}
	}

	public Date getEnddate() {
		return enddate;
	}

	public void setEnddate(Date enddate) {
		this.enddate = enddate;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

}
