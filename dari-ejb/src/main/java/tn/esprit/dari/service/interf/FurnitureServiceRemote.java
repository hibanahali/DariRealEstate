package tn.esprit.dari.service.interf;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Remote;

import tn.esprit.dari.entity.Furniture;
import tn.esprit.dari.entity.User;

@Remote
@LocalBean
public interface FurnitureServiceRemote {
	
	public int addFurniture(Furniture f);
	public List<Furniture> ListeFurnitures();
	public List<Furniture> ListeFurnitures2(User user);
	public List<Furniture> ListeMyFurnitures(User user);
	public int updateFurniturePrice(Furniture f);
	public String DeleteFurniture(int id);
	public String DeleteFurnitureAdmin(int id);
	public Furniture findFurnitureById(int id);
	public void sold(int id);
	public List<Furniture> findFurnitureBystatus(Boolean status);
	public void activate(int id);
	public void changestatus(int id, Boolean status);
	public List<Furniture> InactiveFurniturePost();
	public void changestatusInactive(int id);
	public long reportnumber(User user);
	public long getFurnitureNumber();
	public long getFurnitureNumberBystatus(Boolean status);
	public Double revenue();
}
