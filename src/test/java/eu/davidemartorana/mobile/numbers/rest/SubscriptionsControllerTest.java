package eu.davidemartorana.mobile.numbers.rest;


import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import eu.davidemartorana.mobile.numbers.domain.ServiceType;
import eu.davidemartorana.mobile.numbers.rest.dto.Subscription;
import eu.davidemartorana.mobile.numbers.services.SubscriptionsService;

@RunWith(SpringRunner.class)
@WebMvcTest(SubscriptionsController.class)
public class SubscriptionsControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private SubscriptionsService subscriptionsService;
	
	private static final ObjectMapper MAPPER = new ObjectMapper();

	
	private Subscription createSubscription(final Long id, final String mobileNumber, final long ownerId, final long userId, final ServiceType serviceType) {
		return new Subscription().setId(id)
					.setMobileNumber(mobileNumber)
					.setOwnerId(ownerId)
					.setUserId(userId)
					.setServiceType(serviceType.toLabelService())
					.setSubscriptionDate(LocalDateTime.now());
	}
	
	private Subscription createSubscriptionPost(final String mobileNumber, final long ownerId, final long userId, final ServiceType serviceType) {
		return new Subscription()
					.setMobileNumber(mobileNumber)
					.setOwnerId(ownerId)
					.setUserId(userId)
					.setServiceType(serviceType.toLabelService());
	}
	
	
	@Test
	public void testGetSubscriptionsList_statusOK() throws Exception {
		final Long id = 10L;
		final String mobileNumber = "3569987646382";
		final Long ownerId = 345L;
		final Long userId = 1245L;
		
		final Subscription mockedSubscription = createSubscription(id, mobileNumber, ownerId, userId, ServiceType.MOBILE_PREPAID);

		final Long id2 = 20L;
		final String mobileNumber2 = "3569987646000";
		final Long ownerId2 = 987L;
		final Long userId2 = 4567L;
		
		final Subscription mockedSubscription2 = createSubscription(id2, mobileNumber2, ownerId2, userId2, ServiceType.MOBILE_POSTPAID);

		final List<Subscription> list = Lists.list(mockedSubscription, mockedSubscription2);

		BDDMockito.given(this.subscriptionsService.getSubscriptions(Mockito.any()))
			.willReturn(list);
		
		this.mvc.perform(MockMvcRequestBuilders.get("/mobile-numbers").accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(id))
				.andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(id2));
	}

	
	@Test
	public void testGetSubscriptionsList_statusBadRequest() throws Exception {
		
		this.mvc.perform(MockMvcRequestBuilders.get("/mobile-numbers?").param("mobileNumber", "234fre679"))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
		

		this.mvc.perform(MockMvcRequestBuilders.get("/mobile-numbers?").param("serviceType", "MobilePlanNoValid"))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}
	
	@Test
	public void testGetSubscriptionsList_statusIsOK_Empty() throws Exception {
		
		final List<Subscription> list = new ArrayList<>();

		BDDMockito.given(this.subscriptionsService.getSubscriptions(Mockito.any()))
			.willReturn(list);
		
		this.mvc.perform(MockMvcRequestBuilders.get("/mobile-numbers").accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(0)));
	}
	
	
	@Test
	public void testGetSubscriptionById_statusOK() throws Exception {
		final Long id = 10L;
		final String mobileNumber = "3569987646382";
		final Long ownerId = 345L;
		final Long userId = 1245L;
		
		final Subscription mockedSubscription = createSubscription(id, mobileNumber, ownerId, userId, ServiceType.MOBILE_PREPAID);

		final LocalDateTime subscriptionDate = mockedSubscription.getSubscriptionDate();
		
		BDDMockito.given(this.subscriptionsService.getSubscription(id))
				.willReturn(mockedSubscription);
		
		this.mvc.perform(MockMvcRequestBuilders.get("/mobile-numbers/" + id).accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id.intValue()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.ownerId").value(ownerId.intValue()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.userId").value(userId.intValue()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.mobileNumber").value(mobileNumber))
				.andExpect(MockMvcResultMatchers.jsonPath("$.serviceType").value(ServiceType.MOBILE_PREPAID.toLabelService()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.subscriptionDate").value(subscriptionDate.toString()));
				
	}
	
	
	@Test
	public void testGetSubscriptionById_statusNotFound() throws Exception {
		final Long id = 10L;
		final String messageError = String.format("Subscription with id '%s' not found", id);
				
		BDDMockito.given(this.subscriptionsService.getSubscription(id))
			.willThrow(new EmptyResultDataAccessException(messageError, 1));
		
		this.mvc.perform(MockMvcRequestBuilders.get("/mobile-numbers/" + id).accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isNotFound())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value(messageError))
				.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(404))
				.andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Not Found."))
				.andExpect(MockMvcResultMatchers.jsonPath("$.path").value("/mobile-numbers/" + id));
	}
	
	@Test
	public void testGetSubscriptionById_statusInternalError() throws Exception {
		final Long id = 10L;
		final String messageError = String.format("GenericException", id);
				
		BDDMockito.given(this.subscriptionsService.getSubscription(id))
			.willThrow(new RuntimeException(messageError));
		
		this.mvc.perform(MockMvcRequestBuilders.get("/mobile-numbers/" + id).accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isInternalServerError())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value(messageError))
				.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(500))
				.andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Internal Server Error."))
				.andExpect(MockMvcResultMatchers.jsonPath("$.path").value("/mobile-numbers/" + id));
	}	
	
	
	@Test
	public void testPostSubscription_isCreated() throws Exception {
		final Long id = 10L;
		final String mobileNumber = "3569987646382";
		final Long ownerId = 345L;
		final Long userId = 1245L;
		
		final Subscription mockedSubscription = createSubscription(id, mobileNumber, ownerId, userId, ServiceType.MOBILE_PREPAID);
		final Subscription postedSubscription = createSubscriptionPost(mobileNumber, ownerId, userId, ServiceType.MOBILE_PREPAID);
		
		final LocalDateTime subscriptionDate = mockedSubscription.getSubscriptionDate();
				
		BDDMockito.given(this.subscriptionsService.addNewSubscriptor(Mockito.any()))
			.willReturn(mockedSubscription);
		
		final String jsonSubscription = MAPPER.writeValueAsString(postedSubscription);
		
		this.mvc.perform(MockMvcRequestBuilders.post("/mobile-numbers")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(jsonSubscription))
			.andExpect(MockMvcResultMatchers.status().isCreated())
			.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
			.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id.intValue()))
			.andExpect(MockMvcResultMatchers.jsonPath("$.ownerId").value(ownerId.intValue()))
			.andExpect(MockMvcResultMatchers.jsonPath("$.userId").value(userId.intValue()))
			.andExpect(MockMvcResultMatchers.jsonPath("$.mobileNumber").value(mobileNumber))
			.andExpect(MockMvcResultMatchers.jsonPath("$.serviceType").value(ServiceType.MOBILE_PREPAID.toLabelService()))
			.andExpect(MockMvcResultMatchers.jsonPath("$.subscriptionDate").value(subscriptionDate.toString()));
	}	
	
	
	@Test
	public void testPostSubscription_isBadRequest() throws Exception {
		final String mobileNumber = "3569987646382";
		final Long ownerId = 345L;
		final Long userId = 1245L;
		
		final Subscription postedSubscription = createSubscriptionPost(mobileNumber, ownerId, userId, ServiceType.MOBILE_PREPAID);
		
		final String messageError = "Mobile Number already Exists: " + mobileNumber;
				
		BDDMockito.given(this.subscriptionsService.addNewSubscriptor(Mockito.any()))
			.willThrow(new IllegalArgumentException(messageError));
		
		final String jsonSubscription = MAPPER.writeValueAsString(postedSubscription);
		
		this.mvc.perform(MockMvcRequestBuilders.post("/mobile-numbers/")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(jsonSubscription))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(MockMvcResultMatchers.jsonPath("$.message").value(messageError))
				.andExpect(MockMvcResultMatchers.jsonPath("$.status").value(400))
				.andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Bad Request."))
				.andExpect(MockMvcResultMatchers.jsonPath("$.path").value("/mobile-numbers/"));
	}
	
	@Test
	public void testDelete_isOK() throws Exception {
		
		this.mvc.perform(MockMvcRequestBuilders.delete("/mobile-numbers/10")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isNoContent());		
				
	}
	
}
