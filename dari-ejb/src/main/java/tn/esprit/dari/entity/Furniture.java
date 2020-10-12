package tn.esprit.dari.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Entity
@JsonInclude(Include.NON_NULL)
public class Furniture implements Serializable{

private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_furniture")
	@JsonInclude(Include.NON_DEFAULT)
	private int id_furniture;
	@Column(name = "nom")
	private String nom;
	@Column(name = "description")
	private String description;
	@Column(name = "price")
	private Float price;
	@Column(name = "picture")
	private String picture;
	@Temporal (TemporalType.TIMESTAMP)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private Date creation_date;
	@Column(name = "status")
	private boolean status;
	@Column(name = "sold")
	private boolean sold;
	@Column(name = "report")
	private int report;
	@Column(name = "soldu")
	private boolean soldu;
	//@ManyToOne(cascade = {CascadeType.MERGE,CascadeType.REMOVE})
	@ManyToOne
	@JoinColumn(name="id_user")
	//@JsonManagedReference
	User user;
	
	@Override
	public String toString() {
		return "Furniture [id=" + id_furniture + ", nom=" + nom + ", price=" + price +  "]";
	}
	
	public Furniture() {
		// TODO Auto-generated constructor stub
	}

	public Furniture(String nom, String description, Float price, String picture) {
		
		this.nom = nom;
		this.description = description;
		this.price = price;
		this.picture = picture;
	}
	
public Furniture(int id_furniture,String nom, String description, Float price, String picture) {
		this.id_furniture= id_furniture;
		this.nom = nom;
		this.description = description;
		this.price = price;
		this.picture = picture;
	}
	
	public int getId_furniture() {
		return id_furniture;
	}
	
	public void setId_furniture(int id_furniture) {
		this.id_furniture = id_furniture;
	}
	
	public String getNom() {
		return nom;
	}
	
	public void setNom(String nom) {
		this.nom = nom;
	}
	
	public Float getPrice() {
		return price;
	}
	
	public void setPrice(Float price) {
		this.price = price;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getPicture() {
		return picture;
	}
	
	public void setPicture(String picture) {
		this.picture = picture;
	}
	
	public boolean isStatus() {
		return status;
	}
	
	public void setStatus(boolean status) {
		this.status = status;
	}
	
	public Date getCreation_date() {
		return creation_date;
	}
	
	public void setCreation_date(Date creation_date) {
		this.creation_date = creation_date;
	}
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public boolean isSold() {
		return sold;
	}
	
	public void setSold(boolean sold) {
		this.sold = sold;
	}

	public Furniture(String nom, String description, Float price, String picture, tn.esprit.dari.entity.User user) {
		super();
		this.nom = nom;
		this.description = description;
		this.price = price;
		this.picture = picture;
		this.user = user;
	}
	
	public Furniture (int id_furniture, String nom, String description, Float price, String picture, Date Creation_date ,tn.esprit.dari.entity.User user) {
		this.id_furniture = id_furniture;
		this.nom = nom;
		this.description = description;
		this.price = price;
		this.picture = picture;
		this.creation_date = Creation_date;
		this.user = user;
	}
	
	public int getReport() {
		return report;
	}
	public void setReport(int report) {
		this.report = report;
	}
	
  public boolean isSoldu() {
	return soldu;
}
  
  public void setSoldu(boolean soldu) {
	this.soldu = soldu;
}
	
	
}
