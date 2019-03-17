package eu.davidemartorana.mobile.numbers.rest;

import java.util.List;

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

import eu.davidemartorana.mobile.numbers.rest.dto.Subscription;
import eu.davidemartorana.mobile.numbers.services.SubscriptionsService;



/**
 * Main Controller 
 * 
 * @author davidemartorana
 *
 */
@RestController
@RequestMapping("/mobile-numbers")
public class SubscriptionsController {

	@Autowired
	private SubscriptionsService subscriptionService; 
	
	
	@GetMapping(path= {"","/"})
	public List<Subscription> getSubscribers(@RequestParam(name="ownerId", required=false) final Long ownerId, @RequestParam(name="userId", required=false) final Long userId) {
		return null;
	}
	
	@GetMapping("/{id}")
	public Subscription getSubscriberById(@PathVariable final long id) {
		return null;
	}
	
	@PostMapping("/")
	@ResponseStatus(code=HttpStatus.CREATED)
	public Subscription addSubscriber(@RequestBody final Subscription subscription) {
		 final Subscription returnedSubscription = this.subscriptionService.addNewSubscriptor(subscription);
		 
		 return returnedSubscription;
	}
	
	@PatchMapping("/{id}")
	public Subscription modifySubscriber(@PathVariable final long id, @RequestBody final Subscription subscriber) {
		return null;
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(code=HttpStatus.NO_CONTENT)
	public void deleteSubscriber(@PathVariable final long id) {
		this.subscriptionService.removeSubcription(id);
	}
}
