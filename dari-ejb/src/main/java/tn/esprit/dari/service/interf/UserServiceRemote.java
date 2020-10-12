package tn.esprit.dari.service.interf;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Remote;

import tn.esprit.dari.entity.User;

@Remote
@LocalBean
public interface UserServiceRemote {

	public int register(User u);
	public List<User> ListeUsers();
	public int updateUser(User u);
	public User findUserById(int id);
	public User findUserByEmail(String email);
	public List<User> findUserBystatus(Boolean status);
	public void activate(int id);
	public void changestatus(int id, Boolean status);
	public String DeleteUser(String email);
	public String DeleteUserAdmin(String email);
	public long getUsersNumber();
	public long getUsersNumberBystatus(Boolean status);
	public List<User> InactiveUser();
	public void changestatusInactive(int id);
	public void forgotpassword(String email);
}
