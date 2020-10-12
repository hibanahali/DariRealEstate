package tn.esprit.dari.service.implt;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import tn.esprit.dari.entity.User;
import tn.esprit.dari.service.interf.AuthenticationServiceRemote;
import tn.esprit.dari.utils.BCrypt;

@Stateless
@LocalBean
public class AuthenticationService implements AuthenticationServiceRemote {

	@PersistenceContext
	EntityManager em;
	
	public static User LoggedPerson;
	
	@Override
	public User authentificationUser(String email, String password) {
		User person = new User();
		try {
			TypedQuery<User> query = em.createQuery("SELECT p FROM User p where p.email = :email", User.class);
			query.setParameter("email", email);
			person = query.getSingleResult();
			
			
			if (BCrypt.checkpw(password,person.getPassword()) ) {

				LoggedPerson = person;
				System.out.println(person.getId_user());

				return LoggedPerson;

			} else {
				System.out.println(person);
				return new User();
			}

		} catch (NoResultException n) {
			System.out.println("err");
			System.out.println(email);
			System.out.println("err");
		}

		System.out.println(person);
		return person;

	}
	
	

}
