package tn.esprit.dari.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;




@Entity
public class Subscription implements Serializable {

	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idsub;
	
	@Column(name="subtype")
	private String subtype;
	
	@OneToOne
	private User user;
	
	@Temporal(TemporalType.DATE)
	@Column(name="SubDate")
	private Date subdate;
	
	@Temporal(TemporalType.DATE)
	@Column(name="Expiration_date")
	private Date expdate;

	@Column(name="Sub_price")
	private long price;
	
	@Column(name="Sub_Description")
	private String subdescription;

	public Subscription() {
		super();
	}

	public Subscription(int idsub, String subtype, User user, Date subdate, Date expdate, long price,
			String subdescription) {
		super();
		this.idsub = idsub;
		this.subtype = subtype;
		this.user = user;
		this.subdate = subdate;
		this.expdate = expdate;
		this.price = price;
		this.subdescription = subdescription;
	}

	public int getIdsub() {
		return idsub;
	}

	public void setIdsub(int idsub) {
		this.idsub = idsub;
	}

	public String getSubtype() {
		return subtype;
	}

	public void setSubtype(String subtype) {
		this.subtype = subtype;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getSubdate() {
		return subdate;
	}

	public void setSubdate(Date subdate) {
		this.subdate = subdate;
	}

	public Date getExpdate() {
		return expdate;
	}

	public void setExpdate(Date expdate) {
		this.expdate = expdate;
	}

	public long getPrice() {
		return price;
	}

	public void setPrice(long price) {
		this.price = price;
	}

	public String getSubdescription() {
		return subdescription;
	}

	public void setSubdescription(String subdescription) {
		this.subdescription = subdescription;
	}

	@Override
	public String toString() {
		return "Subscription [" + "fo Mr=" + user.getNom()+ ", of type =" + subtype   + ", date of the sub=" + subdate
				+ ", the end " + expdate + ", its price is=" + price + ", subdescription=" + subdescription + "]";
	}
	
	
		
}
