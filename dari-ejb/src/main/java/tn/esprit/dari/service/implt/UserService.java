package tn.esprit.dari.service.implt;

import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import tn.esprit.dari.entity.Role;
import tn.esprit.dari.entity.User;
import tn.esprit.dari.service.interf.UserServiceRemote;
import tn.esprit.dari.utils.BCrypt;
import tn.esprit.dari.utils.Mail;
import tn.esprit.dari.utils.Passwordgenerator;

@Stateless
@LocalBean
public class UserService implements UserServiceRemote {

	Mail mail;

	@PersistenceContext
	EntityManager em;
	

	
	@Override
	public int register(User u) {
		em.persist(u);
		String hashed = BCrypt.hashpw(u.getPassword(), BCrypt.gensalt());
		u.setPassword(hashed);

		u.setStatus(false);
		//u.setProfile_image("http://localhost/dari_images/user.png");
		Date currentDate = new Date();
		u.setCreation_date(currentDate);
		u.setRole(Role.ROLE_a0);
		String id_string = Integer.toString(u.getId_user());
		String encodedString = Base64.getEncoder().encodeToString(id_string.getBytes());
		String destmail = u.getEmail();
		String message = "Hello Mr/Mrs " + u.getPrenom()+ " "+ u.getNom()
				+ "\n you are registered to our website please click on the link to activate your account"
				+ " http://localhost:9080/dari-web/Dari/User/activate/" + encodedString;
		String subject = "Activation";
		mail.sendMail(destmail, message, subject);
		return u.getId_user();
	}

	@Override
	public List<User> ListeUsers() {
		List<User> u = em.createQuery("SELECT x FROM User x", User.class).getResultList();

		return u;
	}

	@Override
	public int updateUser(User u) {
		em.merge(u);
		return 0;
	}

	@Override
	public User findUserById(int id) {
		User e = em.find(User.class, id);
		return e;
	}

	@Override
	public User findUserByEmail(String email) {
		User person = new User();
		try {
		TypedQuery<User> query = em.createQuery("SELECT p FROM User p where p.email = :email", User.class);
		query.setParameter("email", email);
		person = query.getSingleResult();
		}
		catch (NoResultException n) {
			System.out.println("err");
			return null;
		}
		return person;
	}

	@Override
	public List<User> findUserBystatus(Boolean status) {
		List<User> u = em.createQuery("select u from User u where u.status = :status", User.class).setParameter("status", status).getResultList();
		return u ;
	}
	
@Override
	public List<User> InactiveUser() {
	//List<User> lu = new ArrayList<User>();
	//try {
		//Query query = em.createNativeQuery("SELECT u FROM User u WHERE DATEDIFF(CURDATE(),DATE(u.last_login)) >= 365 ");
	//			lu = (List<User>)query.getResultList();
	//}
	//catch (NoResultException n) {
		//System.out.println("err");
		//return null;
	//}
	//return lu;
	Query query = em.createNativeQuery("SELECT * FROM User u WHERE DATEDIFF(CURDATE(),DATE(u.last_login)) >= 365 ",User.class);
	@SuppressWarnings("unchecked")
	List<User> lu = (List<User>)query.getResultList();
	return lu;
	}

	@Override
	public void activate(int id) {
		User u = em.find(User.class, id);
		u.setStatus(true);
	}
	
	@Override
	public void changestatus(int id, Boolean status) {
		User u = em.find(User.class, id);
		if ( status == true) {
			u.setStatus(status);
			String id_string = Integer.toString(u.getId_user());
			String encodedString = Base64.getEncoder().encodeToString(id_string.getBytes());
			String destmail = u.getEmail();
			String message = "Hello Mr/Mrs " + u.getPrenom()+ " "+ u.getNom()
					+ "\n your account has been successfulyy reactivated please click on the link to activate your acount"
					+ " http://localhost:9080/dari-web/Dari/User/activate/" + encodedString;
			String subject = "Account Reactivation";
			mail.sendMail(destmail, message, subject);
		}
		else {
			u.setStatus(status);
			String destmail = u.getEmail();
			String message = "Hello Mr/Mrs " + u.getPrenom()+ " "+ u.getNom()
					+ "\n Your Account was deactivated due to a violation of our community guidelines";
			String subject = "Account Deactivation";
			mail.sendMail(destmail, message, subject);
		}	
	}
	
	@Override
	public void changestatusInactive(int id) {
		User u = em.find(User.class, id);
		u.setStatus(false);
		String id_string = Integer.toString(u.getId_user());
		String encodedString = Base64.getEncoder().encodeToString(id_string.getBytes());
		String destmail = u.getEmail();
		String message = "Hello Mr/Mrs " + u.getPrenom()+ " "+ u.getNom()
				+ "\n Due to your inactivity for over a year your account has been  deactivated please click on this link if you wish to reactivate your acount"
				+ " http://localhost:9080/dari-web/Dari/User/activate/" + encodedString;
		String subject = "Inactive User";
		mail.sendMail(destmail, message, subject);
		
	}

	@Override
	public String DeleteUser(String email) {
		User person = new User();
		try {
		TypedQuery<User> query = em.createQuery("SELECT p FROM User p where p.email = :email", User.class);
		query.setParameter("email", email);
		person = query.getSingleResult();
		}
		catch (NoResultException n) {
			System.out.println("err");
			return null;
		}
		em.remove(person);
		
		return ("ok");
	}
	
	@Override
	public String DeleteUserAdmin(String email) {
		User person = new User();
		try {
		TypedQuery<User> query = em.createQuery("SELECT p FROM User p where p.email = :email", User.class);
		query.setParameter("email", email);
		person = query.getSingleResult();
		}
		catch (NoResultException n) {
			System.out.println("err");
			return null;
		}
		String destmail = person.getEmail();
		String message = "Hello Mr/Mrs " + person.getPrenom()+ " "+ person.getNom()
				+ "\n Your Account was Deleted due to a violation of our community guidelines";
		String subject = "Account Definitive Ban";
		mail.sendMail(destmail, message, subject);
		em.remove(person);
		
		return ("ok");
	}
	
	@Override
	public long getUsersNumber() {
		TypedQuery<Long> query = 
				em.createQuery("select count(e) from User e where e.role IN (:Role)", Long.class);
		query.setParameter("Role", Role.ROLE_a0);
		return query.getSingleResult();
	}
	
	@Override
	public long getUsersNumberBystatus(Boolean status) {
		TypedQuery<Long> query = 
				em.createQuery("select count(e) from User e where e.status = :status and e.role IN (:Role)", Long.class).setParameter("status", status);
		query.setParameter("Role", Role.ROLE_a0);
		return query.getSingleResult();
	}
	
	
	@Override
	public void forgotpassword(String email) {
		User u = findUserByEmail(email);
		String newpass = Passwordgenerator.generatePassword();
		String hashed = BCrypt.hashpw(newpass, BCrypt.gensalt());
		u.setPassword(hashed);
		em.merge(u);
		String id_string = Integer.toString(u.getId_user());
		String encodedString = Base64.getEncoder().encodeToString(id_string.getBytes());
		String destmail = u.getEmail();
		String message = "Hello Mr/Mrs " + u.getPrenom()+ " "+ u.getNom()
				+ "\n you will attached to this email your new requested password, use it to Sign in " + newpass;
		String subject = "New Password";
		mail.sendMail(destmail, message, subject);
		
	}
	
}
