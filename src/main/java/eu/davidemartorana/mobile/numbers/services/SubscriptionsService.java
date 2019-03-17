package eu.davidemartorana.mobile.numbers.services;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.Flow.Subscriber;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import eu.davidemartorana.mobile.numbers.rest.dto.Subscription;
import eu.davidemartorana.mobile.numbers.source.domain.MobileSubscription;
import eu.davidemartorana.mobile.numbers.source.repositories.MobileSubscriptionsRepository;

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
		
		final MobileSubscription mobileSubscription = SubscriptionConverter.convertToMobileSubscription(subscription);
		
		mobileSubscription.setServiceStartDate(new Date());
		
		final Optional<MobileSubscription> optionalMobile = repoMobileSubscriptionsRepository.findByNumber(mobileSubscription.getNumber());
			
		if(optionalMobile.isPresent()) {
			throw new IllegalArgumentException("Mobile Number already Exists: " + mobileSubscription.getNumber());
		}
		
		
		final MobileSubscription mobileSubscriptionResult = repoMobileSubscriptionsRepository.save(mobileSubscription);
		
		return SubscriptionConverter.convertToSubscription(mobileSubscriptionResult);
	}
	
	public void removeSubcription(final long id) {
		try {
			repoMobileSubscriptionsRepository.deleteById(id);
		} catch (final EmptyResultDataAccessException e) {
			LOGGER.debug("Attempting to delete a record that is not in the database. No error needs to be returned.");
		}
	}
	
}
