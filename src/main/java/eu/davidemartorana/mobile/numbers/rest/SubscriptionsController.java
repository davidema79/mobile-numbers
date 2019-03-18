package eu.davidemartorana.mobile.numbers.rest;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import eu.davidemartorana.mobile.numbers.domain.ServiceType;
import eu.davidemartorana.mobile.numbers.rest.dto.Subscription;
import eu.davidemartorana.mobile.numbers.services.SubscriptionsService;
import io.micrometer.core.instrument.util.StringUtils;



/**
 * Main Controller 
 * 
 * @author davidemartorana
 *
 */
@RestController
@RequestMapping("/mobile-numbers")
public class SubscriptionsController {

	private static final Logger LOGGER = LoggerFactory.getLogger(SubscriptionsController.class);
	
	private static final Pattern PATTERN = Pattern.compile("^[1-9][0-9]{1,14}$");
	
	
	@Autowired
	private SubscriptionsService subscriptionService; 
	
	private boolean isValid(final String mobileNumber) {
		final Matcher matcher = PATTERN.matcher(mobileNumber);
		
		return matcher.matches();
	}
	
	@GetMapping(path= {"","/"})
	public List<Subscription> getSubscribers(@RequestParam(name="ownerId", required=false) final Long ownerId, @RequestParam(name="userId", required=false) final Long userId, @RequestParam(name="mobileNumber", required=false)String mobileNumber, @RequestParam(name="serviceType", required=false)String serviceType) {
		if(StringUtils.isNotEmpty(mobileNumber) && !isValid(mobileNumber)) {
			throw new IllegalArgumentException("Mobile number is not valid. Please insert a mobile number in E164 format.");
		}
		if(StringUtils.isNotEmpty(serviceType)) {
			ServiceType.fromLabelService(serviceType); //Input Validation
		}
		
		final Subscription subscriptionModel = new Subscription().setMobileNumber(mobileNumber).setOwnerId(ownerId).setUserId(userId).setServiceType(serviceType);
		
		return this.subscriptionService.getSubscriptions(subscriptionModel);
	}
	
	@GetMapping("/{id}")
	public Subscription getSubscriberById(@PathVariable final long id) {
		return this.subscriptionService.getSubscription(id);
	}
	
	@PostMapping("/")
	@ResponseStatus(code=HttpStatus.CREATED)
	public Subscription addSubscriber(@RequestBody final Subscription subscription) {
		 final Subscription returnedSubscription = this.subscriptionService.addNewSubscriptor(subscription);
		 
		 return returnedSubscription;
	}
	
	@PatchMapping("/{id}")
	public Subscription modifySubscriber(@PathVariable(required=true) final long id, @RequestBody(required=true) final Subscription subscription) {
		if(StringUtils.isNotEmpty(subscription.getMobileNumber())) {
			throw new IllegalArgumentException("Mobile Number cannot be modified");
		}
		
		if(subscription.getSubscriptionDate() != null) {
			throw new IllegalArgumentException("Subscription date cannot be modified");
		}
		
		if(subscription.getId() != null) {
			throw new IllegalArgumentException("Subscription ID cannot be modified");
		}
		
		return this.subscriptionService.modifySubscription(id, subscription);
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(code=HttpStatus.NO_CONTENT)
	public void deleteSubscriber(@PathVariable final long id) {
		this.subscriptionService.removeSubcription(id);
	}
}
