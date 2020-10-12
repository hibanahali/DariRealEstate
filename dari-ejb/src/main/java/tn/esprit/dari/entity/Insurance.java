package tn.esprit.dari.entity;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Insurance {

	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	@Column(name="Insurance_ID")
	private int id;
	
	@Column(name="Activation")
	private Boolean activation ;
	
	@Temporal(TemporalType.DATE)
	@Column(name="Start_Date")
	private Date startdate;
	
	@Temporal(TemporalType.DATE)
	@Column(name="End_Date")
	private Date enddate;
	
	@Column(name="Validation_Code")
	private String code;
	
	@Column(name="Price")
	private Float price;
	
	@Column(name="entreprise_insurance")
	private String entreprise;
	
	@Column(name="numtlp")
	private String num;


	@ManyToOne
	User user;
	
	public Insurance() {
		super();
	}

	public Insurance(int id, Boolean activation, User user) {
		super();
		this.id = id;
		this.activation = activation;
		this.user = user;
	}

	public Insurance(int id, Boolean activation, Date startdate, Date enddate, String code, Float price,
			String entreprise, String num, User user) {
		super();
		this.id = id;
		this.activation = activation;
		this.startdate = startdate;
		this.enddate = enddate;
		this.code = code;
		this.price = price;
		this.entreprise = entreprise;
		this.num = num;
		this.user = user;
	}

	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public Boolean getActivation() {
		return activation;
	}


	public void setActivation(Boolean activation) {
		this.activation = activation;
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


	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}


	public Float getPrice() {
		return price;
	}


	public void setPrice(Float price) {
		this.price = price;
	}


	public String getEntreprise() {
		return entreprise;
	}


	public void setEntreprise(String entreprise) {
		this.entreprise = entreprise;
	}


	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}

	public String getNum() {
		return num;
	}
	
	
	public void setNum(String num) {
		this.num = num;
	}

	@Override
	public String toString() {
		return "Insurance [id=" + id + ", dateDebut=" + startdate + ", dateFin=" + enddate + ", code=" + code
				+ ", price=" + price + ", entreprise=" + entreprise + ", user=" + user + "numero de telephone" + num + ",]";
	}
	
	
	
}
