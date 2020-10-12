package tn.esprit.dari.managedbeans;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import tn.esprit.dari.entity.User;
import tn.esprit.dari.entity.Furniture;
import tn.esprit.dari.entity.Role;
import tn.esprit.dari.service.implt.AuthenticationService;
import tn.esprit.dari.service.implt.FurnitureService;
import tn.esprit.dari.service.implt.UserService;

@ManagedBean
@SessionScoped
public class AuthenticationBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String email;
	private String password;
	private User user;
	private Boolean loggedIn = false;
	private static Boolean loggedIn2 = false;

	@EJB
	AuthenticationService authenticationservice;

	@EJB
	UserService ur;

	@EJB
	FurnitureService fr;

	public static User LoggedPerson;

	public String doLogin() {

		String navigateTo = "null";
		user = authenticate(email, password);

		if (ur.findUserByEmail(user.getEmail()) == null) {
			FacesContext.getCurrentInstance().addMessage("form:btn", new FacesMessage("Bad credentials "));
		}

		else if (user.isStatus() == false) {
			FacesContext.getCurrentInstance().addMessage("form:btn", new FacesMessage("User Not Activated Yet "));
		}

		else if (user != null && user.getRole() == Role.ROLE_a1) {
			loggedIn = true;
			loggedIn2 = true;
			Date currentDate = new Date();
			user.setLast_login(currentDate);
			ur.updateUser(user);
			if (!ur.InactiveUser().isEmpty()) {
				List<User> lu = ur.InactiveUser();
				for (User u : lu) {
					if (u.isStatus() == true) {
						ur.changestatusInactive(u.getId_user());
					}

				}
			}

			if (!fr.InactiveFurniturePost().isEmpty()) {
				List<Furniture> lu = fr.InactiveFurniturePost();
				for (Furniture f : lu) {
					if (f.isStatus() == true) {
						fr.changestatusInactive(f.getId_furniture());
						;
					}

				}
			}
			navigateTo = "/D033E22AE348AEB5660FC2140AEC35850C4DA997/admin?faces-redirect=true";
		}

		else if (user != null && user.getRole() == Role.ROLE_a0) {
			navigateTo = "/index?faces-redirect=true";
			loggedIn = true;
			loggedIn2 = true;
			Date currentDate = new Date();
			user.setLast_login(currentDate);
			ur.updateUser(user);
		}

		else {
			FacesContext.getCurrentInstance().addMessage("form:btn", new FacesMessage("Bad Credential "));
		}

		return navigateTo;
	}

	public String doLogout() {
		loggedIn = false;
		loggedIn2 = false;
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		return "/dari/sign-in?faces-redirect=true";
	}

	private User authenticate(String email, String password) {
		User user = authenticationservice.authentificationUser(email, password);
		if (user != null) {
			LoggedPerson = user;
		}
		return user;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public static User getLoggedPerson() {
		return LoggedPerson;
	}

	public void setLoggedIn(Boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

	public static Boolean gLoggedIn() {
		return loggedIn2;
	}

	public Boolean getLoggedIn() {
		return loggedIn;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}