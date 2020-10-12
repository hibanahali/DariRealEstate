package tn.esprit.dari.service.interf;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Remote;

import tn.esprit.dari.entity.Subscription;


@Remote
@LocalBean
public interface SubServiceRemote {

	
	public void addsub(Subscription sub); //C
	public List<Subscription> getAllSub(); //R
	public void updateSub(Subscription e , int id); //U
    public void deleteSub(int id); //D
     
    public Subscription findByID(int id );//Research by ID 
	public List<Subscription> findBySubType(String s);//Research by Type 
	

}
