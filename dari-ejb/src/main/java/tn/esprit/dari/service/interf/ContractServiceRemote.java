package tn.esprit.dari.service.interf;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Remote;

import tn.esprit.dari.entity.Contract;
import tn.esprit.dari.entity.ContractModel;
import tn.esprit.dari.entity.User;

@Remote
@LocalBean

public interface ContractServiceRemote {


	public void addcont(Contract cont); //C
	public List<Contract> getAllCont(); //R
	public void updateCont(Contract e , int id); //U
    public void deleteCont(int id); //D
     
    public Contract findByID(int id ); 
	public List<Contract> findByContractType(ContractModel s); 
	public List<Contract> findContractByUser(User s); 
	
	
	public String findDetailsByCntId(int s);

	void updateEnd(Contract e);  
}
