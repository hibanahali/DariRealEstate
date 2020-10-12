package tn.esprit.dari.service.implt;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import tn.esprit.dari.entity.Insurance;
import tn.esprit.dari.service.interf.InsuranceServiceRemote;
import tn.esprit.dari.utils.Mail;
import tn.esprit.dari.utils.SmsApi;

@Stateless
@LocalBean

public class InsuranceService implements InsuranceServiceRemote {

	Mail mail ; 
	
	SmsApi s ;
	
	@PersistenceContext(unitName = "dari-ejb")
	EntityManager em;

	@Override
	public void addIns(Insurance b) {
		
	 
		em.persist(b);

	}

	@Override
	public List<Insurance> getAllIns() {
		
		List<Insurance> L = em.createQuery("select x from Insurance x", Insurance.class).getResultList();
		return (L);
	}

	@Override
	public void updateIns(Insurance i, int id) {
		
		Insurance toUpdate = em.find(Insurance.class, id);
		
		if ( i.getEnddate() != toUpdate.getEnddate()) {
			toUpdate.setEnddate(i.getEnddate());
		}
	}

	@Override
	public void deleteIns(int id) {
	
		em.remove(em.find(Insurance.class, id));

	}

	@Override
	public Insurance findByID(int id) {
		
		Insurance i = em.find(Insurance.class, id);
		return i;
	}

	

 
	
	@Override 
	public void activateInsurance(int id ) {
		Insurance i  = em.find(Insurance.class, id);
	
		if ( i.getActivation() == false) {
			i.setActivation(true);
		}
		else {
			String dl = i.getUser().getEmail();
			String msg = "Hello Mr/Mrs " + i.getUser().getPrenom()+ " "+ i.getUser().getNom()
					+ "\n Your Insurance inscription is already activated ";
			String ins = "Insurance Activated";
			mail.sendMail(dl, msg, ins);		
			
		}
	}

}