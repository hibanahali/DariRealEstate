package tn.esprit.dari.managedbeans;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Random;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.Part;

import tn.esprit.dari.entity.User;
import tn.esprit.dari.service.implt.UserService;
import tn.esprit.dari.utils.BCrypt;

@ManagedBean
@SessionScoped
public class UserBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String fn;
	private String ln;
	private String email;
	private String password;
	private String profile;
	private List<User> users;
	private long number;
	private String oldpass;
	private Part image;

	@EJB
	UserService ur;

	public void addUser() {
		User u = new User();
		u.setNom(ln);
		u.setPrenom(fn);
		u.setEmail(email);
		u.setPassword(password);
		// u.setProfile_image(profile);
		try {
			InputStream in = image.getInputStream();
			String name = image.getSubmittedFileName();
			String extension = name.substring(name.lastIndexOf("."));
			int ran = new Random().nextInt(999999999 + 1) + 1000000000;
			String newn = Integer.toString(ran);
			String finaln = newn + extension;
			File file = new File("/var/www/html/dari_images/" + finaln);
			file.createNewFile();
			FileOutputStream out = new FileOutputStream(file);

			byte[] buffer = new byte[1024];
			int length;

			while ((length = in.read(buffer)) > 0) {
				out.write(buffer, 0, length);
			}

			out.close();
			in.close();
			u.setProfile_image("http://localhost/dari_images/" + finaln);

		} catch (Exception e) {
			e.printStackTrace(System.out);
		}

		if (ur.findUserByEmail(u.getEmail()) != null) {
			FacesContext.getCurrentInstance().addMessage("form:btn",
					new FacesMessage("An account with email alreay exists"));
		} else {

			ur.register(u);
			FacesContext.getCurrentInstance().addMessage("form:btn",
					new FacesMessage("Account Created, You will receive an email to confirm your account "));
		}
	}

	public void forgotpassword() {

		System.out.println(email);
		if (ur.findUserByEmail(email) != null) {
			ur.forgotpassword(email);
			FacesContext.getCurrentInstance().addMessage("form:btn",
					new FacesMessage("Check your email for your new password "));
		} else {
			FacesContext.getCurrentInstance().addMessage("form:btn",
					new FacesMessage("An account with email doesn't exists"));

		}
	}

	public void changepassword() {

		User u = ur.findUserByEmail(AuthenticationBean.LoggedPerson.getEmail());
		Boolean v = BCrypt.checkpw(oldpass, u.getPassword());

		if (v == false) {
			FacesContext.getCurrentInstance().addMessage("form:btn",
					new FacesMessage("Old password is wrong, please try again "));
		} else {
			String hashed2 = BCrypt.hashpw(password, BCrypt.gensalt());
			u.setPassword(hashed2);
			ur.updateUser(u);
			FacesContext.getCurrentInstance().addMessage("form:btn", new FacesMessage("Password changed"));

		}
	}

	public String deletemyaccount() {
		String navigateTo = "null";

		User u = ur.findUserByEmail(AuthenticationBean.LoggedPerson.getEmail());
		Boolean v = BCrypt.checkpw(password, u.getPassword());

		if (v == false) {
			FacesContext.getCurrentInstance().addMessage("form:btn",
					new FacesMessage("Old password is wrong, please try again "));
		} else {
			ur.DeleteUser(AuthenticationBean.LoggedPerson.getEmail());
			FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
			navigateTo = "/dari/sign-in?faces-redirect=true";
		}
		return navigateTo;
	}

	public String updateuser() {
		String navigateTo = "null";

		User u = ur.findUserByEmail(AuthenticationBean.LoggedPerson.getEmail());
		Boolean v = BCrypt.checkpw(password, u.getPassword());

		if (v == false) {
			FacesContext.getCurrentInstance().addMessage("form:btn",
					new FacesMessage("Old password is wrong, please try again "));
		} else {

			u.setNom(ln);
			u.setPrenom(fn);
			try {
				InputStream in = image.getInputStream();
				String name = image.getSubmittedFileName();
				String extension = name.substring(name.lastIndexOf("."));
				int ran = new Random().nextInt(999999999 + 1) + 1000000000;
				String newn = Integer.toString(ran);
				String finaln = newn + extension;
				File file = new File("/var/www/html/dari_images/" + finaln);
				file.createNewFile();
				FileOutputStream out = new FileOutputStream(file);

				byte[] buffer = new byte[1024];
				int length;

				while ((length = in.read(buffer)) > 0) {
					out.write(buffer, 0, length);
				}

				out.close();
				in.close();
				u.setProfile_image("http://localhost/dari_images/" + finaln);

			} catch (Exception e) {
				e.printStackTrace(System.out);
			}
			ur.updateUser(u);
			FacesContext.getCurrentInstance().addMessage("form:btn", new FacesMessage("User Updated"));
			FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
			navigateTo = "/dari/sign-in?faces-redirect=true";

		}
		return navigateTo;
	}

	public void ChangeStatus(User u) {
		if (u.isStatus() == true) {
			ur.changestatus(u.getId_user(), false);
		} else {
			ur.changestatus(u.getId_user(), true);
		}
	}

	public void DeleteUser(User u) {

		ur.DeleteUser(u.getEmail());
	}

	public void DeleteUserAdmin(User u) {

		ur.DeleteUserAdmin(u.getEmail());
	}

	public long UsersNumber() {
		number = ur.getUsersNumber();
		return number;

	}

	public long UsersNumberStatus(Boolean s) {
		long numberST = ur.getUsersNumberBystatus(s);
		return numberST;

	}

	public String ActiveUsersPercentage() {
		long number = ur.getUsersNumber();
		long numberST = ur.getUsersNumberBystatus(true);
		float per1 = ((float) numberST / number) * 100;
		String truncated = String.format("%.0f", per1);
		return truncated;
	}

	public String style() {
		return "width:" + ActiveUsersPercentage() + "%";
	}

	public List<User> getUsers() {
		users = ur.ListeUsers();
		return users;
	}

	public String getFn() {
		return fn;
	}

	public void setFn(String fn) {
		this.fn = fn;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLn() {
		return ln;
	}

	public void setLn(String ln) {
		this.ln = ln;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public long getNumber() {
		return number;
	}

	public String getOldpass() {
		return oldpass;
	}

	public void setOldpass(String oldpass) {
		this.oldpass = oldpass;
	}

	public Part getImage() {
		return image;
	}

	public void setImage(Part image) {
		this.image = image;
	}

}
