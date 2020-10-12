package tn.esprit.dari.managedbeans;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import tn.esprit.dari.entity.User;
import tn.esprit.dari.service.implt.UserService;

@Named("dtFilterUsers")
@ViewScoped
public class FilterUsers implements Serializable {
 
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<User> users;
 
    private List<User> filteredUsers;
 
    @Inject
    private UserService service;
    

 
    @PostConstruct
    public void init() {
    	users = service.ListeUsers();
    }
 
   
    public boolean globalFilterFunction(Object value, Object filter, Locale locale) {
        String filterText = (filter == null) ? null : filter.toString().trim().toLowerCase();
        if (filterText == null || filterText.equals("")) {
            return true;
        }
        int filterInt = getInteger(filterText);
 
        User user = (User) value;
        return  user.getId_user() < filterInt
        		|| user.getNom().toLowerCase().contains(filterText)
                || user.getPrenom().toLowerCase().contains(filterText)
                || user.getEmail().toLowerCase().contains(filterText);
                

    }
 
    private int getInteger(String string) {
        try {
            return Integer.valueOf(string);
        }
        catch (NumberFormatException ex) {
            return 0;
        }
    }
 
    public List<User> getUsers() {
		return users;
	}
    
    public void setUsers(List<User> users) {
		this.users = users;
	}
    
    
    public List<User> getFilteredUsers() {
		return filteredUsers;
	}
    
    public void setFilteredUsers(List<User> filteredUsers) {
		this.filteredUsers = filteredUsers;
	}
    
   
 
    public void setService(UserService service) {
        this.service = service;
    }
}
