package tn.esprit.dari.service.implt;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import tn.esprit.dari.entity.Subscription;
import tn.esprit.dari.service.interf.SubServiceRemote;

@Stateless
@LocalBean

public class SubscriptionService implements SubServiceRemote {

	@PersistenceContext(unitName = "dari-ejb")
	EntityManager em;

	@Override
	public void addsub(Subscription x) {
		
		em.persist(x);
	}

	@Override
	public List<Subscription> getAllSub() {
		
		List<Subscription> L = em.createQuery("select x from Subscription x", Subscription.class).getResultList();
		return (L);
	}

	@Override
	public void updateSub(Subscription s, int id) {

		Subscription toUpdate = em.find(Subscription.class, id);
		if (s.getSubtype() != toUpdate.getSubtype()) {
			toUpdate.setSubtype(s.getSubtype());
		}
		if (s.getExpdate() != toUpdate.getExpdate()) {

			toUpdate.setExpdate(s.getExpdate());
		}

	}

	@Override
	public void deleteSub(int id) {

		em.remove(em.find(Subscription.class, id));
	}

	@Override
	public Subscription findByID(int id) {

		Subscription s = em.find(Subscription.class, id);
		return s;
	}

	@Override
	public List<Subscription> findBySubType(String type) {

		List<Subscription> s = (List<Subscription>) new Subscription();
		try {
			TypedQuery<Subscription> query = em.createQuery("SELECT p FROM Subscription p where p.subtype =:param",
					Subscription.class);
			query.setParameter("param", type);
			s = query.getResultList();

		} catch (Exception e) {
			System.out.println("\n type not found | error Subscription not found");
			return null;
		}
		return s;
	}
}