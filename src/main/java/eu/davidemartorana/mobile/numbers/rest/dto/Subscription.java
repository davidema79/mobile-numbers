package eu.davidemartorana.mobile.numbers.rest.dto;

import java.time.LocalDateTime;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Pattern.Flag;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * 
 * @author davidemartorana
 *
 */
public class Subscription {

	@Null
	private Long id;

	@NotNull
	@Pattern(regexp = "^[1-9]\\d{1,14}$")
	private String mobileNumber;

	private Long userId;

	private Long ownerId;
	
	@Pattern(regexp = "^(Prepaid|Postpaid)$", flags=Flag.CASE_INSENSITIVE)
	private String serviceType;

	private LocalDateTime subscriptionDate;

	
	
	public Long getId() {
		return id;
	}

	public Subscription setId(Long id) {
		this.id = id;
		return this;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public Subscription setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
		return this;
	}

	public Long getUserId() {
		return userId;
	}

	public Subscription setUserId(Long userId) {
		this.userId = userId;
		return this;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public Subscription setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
		return this;
	}

	public LocalDateTime getSubscriptionDate() {
		return subscriptionDate;
	}

	public Subscription setSubscriptionDate(LocalDateTime subscriptionDate) {
		this.subscriptionDate = subscriptionDate;
		return this;
	}

	public String getServiceType() {
		return serviceType;
	}

	public Subscription setServiceType(String serviceType) {
		this.serviceType = serviceType;
		return this;
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this, true);
	}
	
	@Override
	public boolean equals(Object other) {
		return EqualsBuilder.reflectionEquals(this, other, true);
	}

}
