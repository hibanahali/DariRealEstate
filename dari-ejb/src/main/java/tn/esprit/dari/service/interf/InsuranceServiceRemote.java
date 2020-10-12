package tn.esprit.dari.service.interf;

import java.util.List;

import tn.esprit.dari.entity.Insurance;

public interface InsuranceServiceRemote {


	public void addIns(Insurance b); //C
	public List<Insurance> getAllIns(); //R
	public void updateIns(Insurance e , int id); //U
    public void deleteIns(int id); //D
     
    public Insurance findByID(int id ); 
	//public Insurance findByCode(String s); 
	
	public void activateInsurance(int id);
	
}
