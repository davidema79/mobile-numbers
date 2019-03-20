package eu.davidemartorana.mobile.numbers.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import eu.davidemartorana.mobile.numbers.domain.ServiceType;
import eu.davidemartorana.mobile.numbers.rest.dto.Subscription;
import eu.davidemartorana.mobile.numbers.source.domain.MobileSubscription;
import eu.davidemartorana.mobile.numbers.source.repositories.MobileSubscriptionsRepository;
import io.micrometer.core.instrument.util.StringUtils;

/**
 * 
 * @author davidemartorana
 *
 */
@Service
public class SubscriptionsService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SubscriptionsService.class);

	@Autowired
	private MobileSubscriptionsRepository repoMobileSubscriptionsRepository;
	
	@Transactional
	public Subscription addNewSubscriptor(final Subscription subscription) {
		LOGGER.info("Creating a new subscription for number '{}'", subscription.getMobileNumber());
		
		final MobileSubscription mobileSubscription = SubscriptionConverter.convertToMobileSubscription(subscription);
		
		mobileSubscription.setServiceStartDate(new Date());
		
		LOGGER.debug("Checking if the number '{}' already exists", subscription.getMobileNumber());
		
		final Optional<MobileSubscription> optionalMobile = repoMobileSubscriptionsRepository.findByNumber(mobileSubscription.getNumber());
			
		if(optionalMobile.isPresent()) {
			LOGGER.info("The number '{}' already exists", subscription.getMobileNumber());
			throw new IllegalArgumentException("Mobile Number already Exists: " + mobileSubscription.getNumber());
		}
		
		LOGGER.debug("The number '{}' does not exists", subscription.getMobileNumber());
		
		final MobileSubscription mobileSubscriptionResult = repoMobileSubscriptionsRepository.save(mobileSubscription);
		
		LOGGER.debug("New subscription stored in the databas: [{}]", mobileSubscriptionResult);
		
		return SubscriptionConverter.convertToSubscription(mobileSubscriptionResult);
	}
	
	@Transactional
	public Subscription modifySubscription(final long id, final Subscription subscription) {
		final Optional<MobileSubscription> mobileSubscriptionOptional = repoMobileSubscriptionsRepository.findById(id);
		
		final MobileSubscription mobileSubscription = mobileSubscriptionOptional
				.orElseThrow(() -> new EmptyResultDataAccessException(String.format("Subscription with id '%s' not found", id), 1));
		
		if(StringUtils.isNotEmpty(subscription.getServiceType())) {
			final ServiceType serviceType = ServiceType.fromLabelService(subscription.getServiceType());
			mobileSubscription.setServiceType(serviceType);
		}
		
		if(subscription.getOwnerId() != null) {
			mobileSubscription.setCustomerIdOwner(subscription.getOwnerId());
		}
		
		if(subscription.getUserId() != null) {
			mobileSubscription.setCustomerIdUser(subscription.getUserId());
		}
		
		final MobileSubscription mobileSubscriptionResult = this.repoMobileSubscriptionsRepository.save(mobileSubscription);
		
		return SubscriptionConverter.convertToSubscription(mobileSubscriptionResult);
	}
	
	public void removeSubcription(final long id) {
		try {
			repoMobileSubscriptionsRepository.deleteById(id);
		} catch (final EmptyResultDataAccessException e) {
			LOGGER.info("Attempting to delete a record that is not in the database. No error needs to be returned.");
		}
	}
	
	public Subscription getSubscription(long id) {
		final Optional<MobileSubscription> mobileSubscriptionOptional = this.repoMobileSubscriptionsRepository.findById(id);
		
		final MobileSubscription mobileSubscription = mobileSubscriptionOptional
				.orElseThrow(() -> new EmptyResultDataAccessException(String.format("Subscription with id '%s' not found", id), 1));
		final Subscription subscription = SubscriptionConverter.convertToSubscription(mobileSubscription);
		
		return subscription;
	}
	
	public List<Subscription> getSubscriptions(final Subscription subscriptionModel) {
	
		final MobileSubscription mobileSubscriptionExample = SubscriptionConverter.convertToMobileSubscription(subscriptionModel);
		
		final Example<MobileSubscription> example = Example.of(mobileSubscriptionExample);
		
		final List<MobileSubscription> listMobileSubscription = this.repoMobileSubscriptionsRepository.findAll(example);
		
		return listMobileSubscription.stream().map(item -> SubscriptionConverter.convertToSubscription(item)).collect(Collectors.toList());
	}
}
