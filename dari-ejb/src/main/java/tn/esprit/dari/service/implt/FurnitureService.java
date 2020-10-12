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

import tn.esprit.dari.entity.Furniture;
import tn.esprit.dari.entity.User;
import tn.esprit.dari.service.interf.FurnitureServiceRemote;
import tn.esprit.dari.utils.Mail;

@Stateless
@LocalBean
public class FurnitureService implements FurnitureServiceRemote {
	Mail mail;

	@PersistenceContext
	EntityManager em;

	@Override
	public int addFurniture(Furniture f) {
		em.persist(f);
		Date currentDate = new Date();
		f.setCreation_date(currentDate);
		f.setSold(false);
		f.setStatus(true);
		f.setSoldu(false);
		f.setReport(0);
		return f.getId_furniture();
	}
	
	@Override
	public long reportnumber(User user) {
		TypedQuery<Long> query = 
				em.createQuery("select sum(f.report) from Furniture f where f.user = :user", Long.class).setParameter("user", user);
		if (query.getSingleResult() == null) return -1;
		else {
		return query.getSingleResult();}
	}

	@Override
	public List<Furniture> ListeFurnitures() {
		List<Furniture> f = em.createQuery("SELECT x FROM Furniture x", Furniture.class).getResultList();

		return f;
	}

	@Override
	public List<Furniture> ListeFurnitures2(User user) {
		List<Furniture> f = em.createQuery("SELECT f FROM Furniture f where f.user != :user and f.status = true", Furniture.class).setParameter("user", user).getResultList();

		return f;
	}

	@Override
	public List<Furniture> ListeMyFurnitures(User user) {
		List<Furniture> f = em.createQuery("SELECT f FROM Furniture f where f.user = :user and f.status = true", Furniture.class).setParameter("user", user).getResultList();

		return f;
	}

	@Override
	public int updateFurniturePrice(Furniture f) {
		em.merge(f);
		return 0;
	}

	@Override
	public String DeleteFurniture(int id) {
		Furniture f = new Furniture();
		try {
			TypedQuery<Furniture> query = em.createQuery("SELECT f FROM Furniture f where f.id_furniture = :id", Furniture.class);
			query.setParameter("id", id);
			f = query.getSingleResult();
		}
		catch (NoResultException n) {
			System.out.println("err");
			return null;
		}
		em.remove(f);
		return ("ok");
	}
	
	@Override
	public String DeleteFurnitureAdmin(int id) {
		Furniture f = new Furniture();
		try {
			TypedQuery<Furniture> query = em.createQuery("SELECT f FROM Furniture f where f.id_furniture = :id", Furniture.class);
			query.setParameter("id", id);
			f = query.getSingleResult();
		}
		catch (NoResultException n) {
			System.out.println("err");
			return null;
		}
		String destmail = f.getUser().getEmail();
		String message = "Hello Mr/Mrs " + f.getUser().getPrenom()+ " "+ f.getUser().getNom()
				+ "\n Your Furniture Post was deleted due to a violation of our community guidelines";
		String subject = "Furniture Post Deactivation";
		mail.sendMail(destmail, message, subject);
		em.remove(f);
		
		return ("ok");
	}

	@Override
	public Furniture findFurnitureById(int id) {
		Furniture f = em.find(Furniture.class, id);
		return f;
	}


	@Override
	public void sold(int id) {
		Furniture f = em.find(Furniture.class, id);
		f.setSold(true);
	}

	@Override
	public void activate(int id) {
		Furniture f = em.find(Furniture.class, id);
		f.setStatus(true);

	}

	@Override
	public void changestatus(int id, Boolean status) {
		Furniture f = em.find(Furniture.class, id);
		if ( status == true) {
			f.setStatus(status);
			String id_string = Integer.toString(f.getId_furniture());
			String encodedString = Base64.getEncoder().encodeToString(id_string.getBytes());
			String destmail = f.getUser().getEmail();
			String message = "Hello Mr/Mrs " + f.getUser().getPrenom()+ " "+ f.getUser().getNom()
					+ "\n your Furniture post has been successfulyy reactivated please click on the link to activate your post"
					+ " http://localhost:9080/dari-web/Dari/Furniture/activate/" + encodedString;
			String subject = "Furniture Post Reactivation";
			mail.sendMail(destmail, message, subject);
		}
		else {
			f.setStatus(status);
			String destmail = f.getUser().getEmail();
			String message = "Hello Mr/Mrs " + f.getUser().getPrenom()+ " "+ f.getUser().getNom()
					+ "\n Your Furniture Post was deactivated due to a violation of our community guidelines";
			String subject = "Furniture Post Deactivation";
			mail.sendMail(destmail, message, subject);
		}	

	}

	@Override
	public List<Furniture> findFurnitureBystatus(Boolean status) {
		List<Furniture> f = em.createQuery("select f from Furniture f where f.status = :status", Furniture.class).setParameter("status", status).getResultList();
		return f ;
	}


	@Override
	public List<Furniture> InactiveFurniturePost() {
		Query query = em.createNativeQuery("SELECT * FROM Furniture f WHERE DATEDIFF(CURDATE(),DATE(f.creation_date)) >= 365 ",Furniture.class);
		@SuppressWarnings("unchecked")
		List<Furniture> lu = (List<Furniture>)query.getResultList();
		return lu;
	}
	
	@Override
	public void changestatusInactive(int id) {
		Furniture f = em.find(Furniture.class, id);
		f.setStatus(false);
		String id_string = Integer.toString(f.getId_furniture());
		String encodedString = Base64.getEncoder().encodeToString(id_string.getBytes());
		String destmail = f.getUser().getEmail();
		String message = "Hello Mr/Mrs " + f.getUser().getPrenom()+ " "+ f.getUser().getNom()
				+ "\n Due to the status inactivity for over a year of your post named : " + f.getNom() + ". This has been deactivated please click on this link if you wish to reactivate your post"
				+ " http://localhost:9080/dari-web/Dari/Furniture/activate/" + encodedString;
		String subject = "Inactive Furniture Post";
		mail.sendMail(destmail, message, subject);
		
	}

	@Override
	public long getFurnitureNumber() {
		TypedQuery<Long> query = 
				em.createQuery("select count(e) from Furniture e", Long.class);
		
		return query.getSingleResult();
	}
	
	@Override
	public long getFurnitureNumberBystatus(Boolean status) {
		TypedQuery<Long> query = 
				em.createQuery("select count(e) from Furniture e where e.status = :status", Long.class).setParameter("status", status);
		return query.getSingleResult();
	}
	
	@Override
	public Double revenue() {
		TypedQuery<Double> query = 
				em.createQuery("select sum(f.price) from Furniture f where f.sold =:sold and f.soldu =:soldu", Double.class).setParameter("sold", true).setParameter("soldu", false);
		if (query.getSingleResult() == null) return (double) 0;
		else {
		return query.getSingleResult();}
	}
	
}
