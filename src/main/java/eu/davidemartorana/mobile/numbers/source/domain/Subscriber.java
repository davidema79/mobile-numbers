package eu.davidemartorana.mobile.numbers.source.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import eu.davidemartorana.mobile.numbers.domain.ServiceType;

@Entity
@Table(name="subscriber")
public class Subscriber {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@Column(name="msisdn")
	private String number;
	
	@Column(name="customer_id_owner")
	private Long customerIdOwner;
	
	@Column(name="customer_id_user")
	private Long customerIdUser;
	
	@Column(name="service_type")
	private ServiceType serviceType;
	
	@Column(name="service_start_date")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long serviceStartDate;

	
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

	public Long getServiceStartDate() {
		return serviceStartDate;
	}

	public void setServiceStartDate(Long serviceStartDate) {
		this.serviceStartDate = serviceStartDate;
	}
	
	

}
