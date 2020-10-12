package tn.esprit.dari.managedbeans;

import java.io.Serializable;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import tn.esprit.dari.entity.ContractModel;

@ManagedBean(name = "data")
@ApplicationScoped
public class Data implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ContractModel[] getModels() { return ContractModel.values(); }
}
