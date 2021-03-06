package eu.davidemartorana.mobile.numbers.services;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.davidemartorana.mobile.numbers.domain.ServiceType;
import eu.davidemartorana.mobile.numbers.rest.dto.Subscription;
import eu.davidemartorana.mobile.numbers.source.domain.MobileSubscription;
import io.micrometer.core.instrument.util.StringUtils;

/**
 * 
 * @author davidemartorana
 *
 */
public class SubscriptionConverter {

	private static final Logger LOGGER = LoggerFactory.getLogger(SubscriptionConverter.class);

	private SubscriptionConverter() {
		throw new IllegalAccessError("SubscriptionConverter cannot be instaciated");
	}
	
	public static MobileSubscription convertToMobileSubscription(final Subscription subscription) {
		LOGGER.info("Converting DTO to Data Domain {}", subscription);
		
		final MobileSubscription mobileSubscription = new MobileSubscription();
		
		mobileSubscription.setId(subscription.getId());
		mobileSubscription.setCustomerIdOwner(subscription.getOwnerId());
		mobileSubscription.setCustomerIdUser(subscription.getUserId());
		mobileSubscription.setNumber(subscription.getMobileNumber());
		if(StringUtils.isNotEmpty(subscription.getServiceType())) {
			mobileSubscription.setServiceType(ServiceType.fromLabelService(subscription.getServiceType()));
		}
		
		LOGGER.debug("Converted DTO to Data Domain {} -> {}", subscription, mobileSubscription);
		
		return mobileSubscription;
	}
	
	public static Subscription convertToSubscription(final MobileSubscription mobileSubscription) {
		LOGGER.info("Converting Data Domain to DTO {}", mobileSubscription);
		
		final Date dateToConvert = mobileSubscription.getServiceStartDate();
		
		final LocalDateTime subscriptionDate = dateToConvert.toInstant()
				.atZone(ZoneId.systemDefault())
				.toLocalDateTime();
		
		final Subscription subscription = new Subscription()
				.setId(mobileSubscription.getId())
				.setMobileNumber(mobileSubscription.getNumber())
				.setOwnerId(mobileSubscription.getCustomerIdOwner())
				.setUserId(mobileSubscription.getCustomerIdUser())
				.setServiceType(mobileSubscription.getServiceType().toLabelService())
				.setSubscriptionDate(subscriptionDate);
		
		LOGGER.debug("Converted Data Domain to DTO {} -> {}", mobileSubscription, subscription);
		
		return subscription;
	}
	
}
