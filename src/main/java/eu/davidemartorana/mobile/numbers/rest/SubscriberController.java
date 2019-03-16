package eu.davidemartorana.mobile.numbers.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import eu.davidemartorana.mobile.numbers.rest.dto.Subscriber;



/**
 * Main Controller 
 * 
 * @author davidemartorana
 *
 */
@RestController
@RequestMapping("/subscribers")
public class SubscriberController {

	
	@GetMapping(path= {"","/"})
	public List<Subscriber> getSubscribers(@RequestParam(name="ownerId", required=false) final Long ownerId, @RequestParam(name="userId", required=false) final Long userId) {
		return null;
	}
	
	@GetMapping("/{id}")
	public Subscriber getSubscriberById(@PathVariable final long id) {
		return null;
	}
	
	@PostMapping("/")
	public Subscriber addSubscriber(@RequestBody final Subscriber subscriber) {
		return null;
	}
	
	@PatchMapping("/{id}")
	public Subscriber modifySubscriber(@RequestBody final Subscriber subscriber) {
		return null;
	}
	
	@DeleteMapping("/{id}")
	public Subscriber deleteSubscriber(@RequestBody final Subscriber subscriber) {
		return null;
	}
}
