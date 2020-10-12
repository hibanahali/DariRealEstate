package tn.esprit.dari.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Contract implements Serializable {

	

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Enumerated(EnumType.STRING)
	private ContractModel typeCont;

	@Column(name="description" ,length = 50000)
	private String description;
	
	@Column(name="price")
	private int price;
	
	 
	@Column(name="Details")
	private String details;
	
	@Temporal(TemporalType.DATE)
	@Column(name="StartDate")
	private Date startdate;

	@Temporal(TemporalType.DATE)
	@Column(name="End_Date")
	private Date enddate;

	@ManyToOne
	private User user ;

	public Contract() {
		super();
	}

	
	
	public Contract(ContractModel typeCont, Date enddate) {
		super();
		this.typeCont = typeCont;
		this.enddate = enddate;
	}



	public Contract(int id, ContractModel typeCont, String description, int price, String details, Date startdate,
			Date enddate, User user) {
		super();
		this.id = id;
		this.typeCont = typeCont;
		this.description = description;
		this.price = price;
		this.details = details;
		this.startdate = startdate;
		this.enddate = enddate;
		this.user = user;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ContractModel getTypeCont() {
		return typeCont;
	}

	public void setTypeCont(ContractModel typeCont) {
		this.typeCont = typeCont;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public Date getStartdate() {
		return startdate;
	}

	public void setStartdate(Date startdate) {
		this.startdate = startdate;
	}

	public Date getEnddate() {
		return enddate;
	}

	public void setEnddate(Date enddate) {
		this.enddate = enddate;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "Contract [id=" + id + ", typeCont=" + typeCont + ", description=" + description + ", price=" + price
				+ ", details=" + details + ",startdate=" + startdate + ", enddate=" + enddate + ", user=" + user + "]";
	}

}
