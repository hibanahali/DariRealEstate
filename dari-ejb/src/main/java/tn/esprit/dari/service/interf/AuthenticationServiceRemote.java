package tn.esprit.dari.service.interf;

import javax.ejb.LocalBean;
import javax.ejb.Remote;

import tn.esprit.dari.entity.User;

@Remote
@LocalBean
public interface AuthenticationServiceRemote {
	
	public User authentificationUser(String email, String password);

}
