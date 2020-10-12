package tn.esprit.dari.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import tn.esprit.dari.entity.Role;

@Entity
@JsonInclude(Include.NON_NULL)
public class User implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_user")
	@JsonInclude(Include.NON_DEFAULT)
	private int id_user;
	@Column(name = "nom")
	private String nom;
	@Column(name = "prenom")
	private String prenom;
	@Column(name = "email")
	private String email;
	@Column(name = "password")
	private String password;
	@Column(name = "profile_image")
	private String profile_image;
	@Temporal (TemporalType.TIMESTAMP)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private Date creation_date;
	@Temporal (TemporalType.TIMESTAMP)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private Date last_login;
	@Column(name = "status")
	private boolean status;
	@Enumerated(EnumType.STRING)
	Role role;
	@OneToMany(mappedBy="user", cascade = {CascadeType.MERGE,CascadeType.REMOVE}, fetch=FetchType.EAGER)
	//@JsonBackReference
	@JsonIgnoreProperties({"user"})
    private List<Furniture> furnitures;
	
	//private List<Furniture> cart;
	
	@Override
	public String toString() {
		return "User [id=" + id_user + ", nom=" + nom + ", prenom=" + prenom +  "]";
	}
	
	public User(String nom, String prenom, String email, String password) {
		this.nom = nom;
		this.prenom = prenom;
		this.email = email;
		this.password = password;
	}

	public int getId_user() {
		return id_user;
	}
	public void setId_user(int id_user) {
		this.id_user = id_user;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getPrenom() {
		return prenom;
	}
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getProfile_image() {
		return profile_image;
	}
	public void setProfile_image(String profile_image) {
		this.profile_image = profile_image;
	}
	public Date getCreation_date() {
		return creation_date;
	}
	public void setCreation_date(Date creation_date) {
		this.creation_date = creation_date;
	}
	public Date getLast_login() {
		return last_login;
	}
	public void setLast_login(Date last_login) {
		this.last_login = last_login;
	}
	
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public User() {

	}
	public User(String nom, String prenom, String email, String password, String profile_image) {
		super();
		this.nom = nom;
		this.prenom = prenom;
		this.email = email;
		this.password = password;
		this.profile_image = profile_image;
	}
	
	public Role getRole() {
		return role;
	}
	
	public void setRole(Role role) {
		this.role = role;
	}
	
	public List<Furniture> getFurnitures() {
		return furnitures;
	}
	
	public void setFurnitures(List<Furniture> furnitures) {
		this.furnitures = furnitures;
	}

	public User(String nom, String prenom) {
		this.nom = nom;
		this.prenom = prenom;
	}
	
	
	
}
