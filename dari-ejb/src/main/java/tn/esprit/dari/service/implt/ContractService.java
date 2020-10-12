package tn.esprit.dari.service.implt;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import tn.esprit.dari.entity.Contract;
import tn.esprit.dari.entity.ContractModel;
import tn.esprit.dari.entity.User;
import tn.esprit.dari.service.interf.ContractServiceRemote;

@Stateless
@LocalBean

public class ContractService implements ContractServiceRemote {

	@PersistenceContext(unitName = "dari-ejb")
	EntityManager em;

	@Override
	public void addcont(Contract s) {
		// Done
		em.persist(s);
	}

	@Override
	public List<Contract> getAllCont() {
		// Done
		return em.createQuery("select x from Contract x", Contract.class).getResultList();
	}

	@Override
	public void updateCont(Contract e, int id) {

		Contract toUpdate = em.find(Contract.class, id);

		if (e.getTypeCont() != toUpdate.getTypeCont()) {
			toUpdate.setTypeCont(e.getTypeCont());
		}
		if (e.getDescription() != toUpdate.getDescription()) {
			toUpdate.setDescription(e.getDescription());
		}
		if (e.getDetails() != toUpdate.getDetails()) {
			toUpdate.setDetails(e.getDetails());
		}
		if (e.getEnddate() != toUpdate.getEnddate()) {
			toUpdate.setEnddate(e.getEnddate());
		}
		if (e.getPrice() != toUpdate.getPrice()) {
			toUpdate.setPrice(e.getPrice());
		}
	}

	@Override
	public void updateEnd(Contract e) {
		em.merge(e);
	}

	@Override
	public void deleteCont(int id) {
		// Done
		em.remove(em.find(Contract.class, id));
	}

	@Override
	public Contract findByID(int id) {
		// Done
		return em.find(Contract.class, id);
	}

	@Override
	public List<Contract> findByContractType(ContractModel s) {

		try {
			System.out.println("return list done ");
			return em.createQuery("select x from Contract x where x.typeCont =:param", Contract.class)
					.setParameter("param", s).getResultList();
		} catch (Exception e) {
			System.out.println("PLease verify the contract Type || your insert ");
			return new ArrayList<Contract>();
		}
	}

	@Override
	public List<Contract> findContractByUser(User s) {
		// Done successfully
		try {
			System.out.println("return list done ");
			return em.createQuery("select x from Contract x where x.user =:param", Contract.class)
					.setParameter("param", s).getResultList();
		} catch (Exception e) {
			System.out.println("PLease verify the contract User || your insert ");
			return new ArrayList<Contract>();
		}
	}

	@Override
	public String findDetailsByCntId(int s) {

		try {
			System.out.println("return list done ");
			return em.find(Contract.class, s).getDetails();
		} catch (Exception e) {
			System.out.println("PLease verify the contract User || your insert ");
			return null;
		}
	}
}
