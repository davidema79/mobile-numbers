package eu.davidemartorana.mobile.numbers.source.domain;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import eu.davidemartorana.mobile.numbers.domain.ServiceType;

@Entity
@Table(name="mobile_subscriptions")
public class MobileSubscription {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="msisdn", unique=true)
	private String number;
	
	@Column(name="customer_id_owner")
	private Long customerIdOwner;
	
	@Column(name="customer_id_user")
	private Long customerIdUser;
	
	@Column(name="service_type")
	private ServiceType serviceType;
	
	@Column(name="service_start_date")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Date serviceStartDate;

	
	public MobileSubscription() {
		super();
	}
	
	public MobileSubscription(String number, Long customerIdOwner, Long customerIdUser, ServiceType serviceType) {
		super();
		this.number = number;
		this.customerIdOwner = customerIdOwner;
		this.customerIdUser = customerIdUser;
		this.serviceType = serviceType;
		this.serviceStartDate = new Date();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public Long getCustomerIdOwner() {
		return customerIdOwner;
	}

	public void setCustomerIdOwner(Long customerIdOwner) {
		this.customerIdOwner = customerIdOwner;
	}

	public Long getCustomerIdUser() {
		return customerIdUser;
	}

	public void setCustomerIdUser(Long customerIdUser) {
		this.customerIdUser = customerIdUser;
	}

	public ServiceType getServiceType() {
		return serviceType;
	}

	public void setServiceType(ServiceType serviceType) {
		this.serviceType = serviceType;
	}

	public Date getServiceStartDate() {
		return serviceStartDate;
	}

	public void setServiceStartDate(Date serviceStartDate) {
		this.serviceStartDate = serviceStartDate;
	}
	
	

}
