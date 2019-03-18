package eu.davidemartorana.mobile.numbers.rest;

import static org.hamcrest.CoreMatchers.notNullValue;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.assertj.core.util.Lists;
import org.hamcrest.collection.IsCollectionWithSize;
import org.hamcrest.collection.IsEmptyCollection;
import org.hamcrest.core.IsCollectionContaining;
import org.hamcrest.core.IsEqual;
import org.hamcrest.text.StringContainsInOrder;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import eu.davidemartorana.mobile.numbers.config.DataSourceConfig;
import eu.davidemartorana.mobile.numbers.domain.ServiceType;
import eu.davidemartorana.mobile.numbers.rest.dto.Subscription;
import eu.davidemartorana.mobile.numbers.services.SubscriptionConverter;
import eu.davidemartorana.mobile.numbers.services.SubscriptionsService;
import eu.davidemartorana.mobile.numbers.source.domain.MobileSubscription;

/**
 * Tests
 * 
 * @author davidemartorana
 *
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@ContextConfiguration(classes = { SubscriptionsControllerIT.Config.class })
@AutoConfigureTestDatabase(replace=Replace.NONE)
public class SubscriptionsControllerIT {

	@Import(DataSourceConfig.class)
	public static class Config {

		@Bean
		public SubscriptionsService getSubscriptionService() {
			return new SubscriptionsService();
		}

		@Bean
		public SubscriptionsController getSubscriptionsController() {
			return new SubscriptionsController();
		}

	}

	@Autowired
	private SubscriptionsController controller;

	@Autowired
	private TestEntityManager testEntityManager;

	private final List<MobileSubscription> mobileSubscriptinList = new ArrayList<>();
	private final List<Long> mobileSubscriptinIdList = new ArrayList<>();


	@Before
	public void populateDataBase() {

		mobileSubscriptinList.add(new MobileSubscription("35677986001", 2l, 2l, ServiceType.MOBILE_POSTPAID));
		mobileSubscriptinList.add(new MobileSubscription("35677986002", 2l, 3l, ServiceType.MOBILE_PREPAID));
		mobileSubscriptinList.add(new MobileSubscription("35677986003", 4l, 5l, ServiceType.MOBILE_POSTPAID));
		mobileSubscriptinList.add(new MobileSubscription("35677986004", 10l, 10l, ServiceType.MOBILE_PREPAID));

		mobileSubscriptinIdList.add((Long) this.testEntityManager.persistAndGetId(mobileSubscriptinList.get(0)));
		mobileSubscriptinIdList.add((Long) this.testEntityManager.persistAndGetId(mobileSubscriptinList.get(1)));
		mobileSubscriptinIdList.add((Long) this.testEntityManager.persistAndGetId(mobileSubscriptinList.get(2)));
		mobileSubscriptinIdList.add((Long) this.testEntityManager.persistAndGetId(mobileSubscriptinList.get(3)));
		//this.testEntityManager.flush();
	}
	
	@After
	public void afterTest() {
		mobileSubscriptinList.clear();
		mobileSubscriptinIdList.clear();
		
		this.testEntityManager.clear();
	}

	@Test
	public void getSubscribersTest() {
		final List<Subscription> allList = this.controller.getSubscribers(null, null, null, null);

		Assert.assertThat(allList, notNullValue());
		Assert.assertThat(allList, IsCollectionWithSize.hasSize(mobileSubscriptinList.size()));
		
		//Assert.assertEquals(allList, hasItems(mobileSubscriptinList.stream().map(item -> SubscriptionConverter.convertToSubscription(item)).collect(Collectors.toList()).toArray()));

		final List<Subscription> emptyList = this.controller.getSubscribers(1l, 1l, null, null);
		Assert.assertThat(emptyList, notNullValue());
		Assert.assertThat(emptyList, IsEmptyCollection.empty());
		
		final List<Subscription> twoItemsInList = this.controller.getSubscribers(2l, null, null, null);
		Assert.assertThat(twoItemsInList, notNullValue());
		Assert.assertThat(twoItemsInList, IsCollectionWithSize.hasSize(2));
		
		final Subscription first = SubscriptionConverter.convertToSubscription(mobileSubscriptinList.get(0));
		final Subscription second = SubscriptionConverter.convertToSubscription(mobileSubscriptinList.get(1));
		
		Assert.assertThat(twoItemsInList, IsCollectionContaining.hasItems(first, second));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void getSubscriberTest_MobileNumberNotValid() {
		final String mobileNumber = "0098766789";
		try {
			this.controller.getSubscribers(null, null, mobileNumber, null);
		} catch (final IllegalArgumentException e) {
			Assert.assertThat(e.getMessage(),StringContainsInOrder.stringContainsInOrder(Lists.list("Mobile", "number", "not valid", "E164 format")));
			throw e;
		}
	}

	@Test
	public void getSubscriberByIdTest() {
		final Long id = this.mobileSubscriptinIdList.get(1);
		final Subscription subscription = this.controller.getSubscriberById(id);

		Assert.assertNotNull(subscription);
		final Subscription first = SubscriptionConverter.convertToSubscription(mobileSubscriptinList.get(1));
		
		Assert.assertThat(subscription, IsEqual.equalTo(first));
	}
	
	@Test(expected=EmptyResultDataAccessException.class)
	public void getSubscriberByIdTestNotExists() {
		
		try {
			this.controller.getSubscriberById(112345l);
		} catch (final EmptyResultDataAccessException e) {
			Assert.assertThat(e.getMessage(),StringContainsInOrder.stringContainsInOrder(Lists.list("Subscription", "11234")));
			throw e;
		}
		
	}

	@Test
	public void addSubscriberTest() throws Exception {
		final String mobileNumber = "356789654";
		final LocalDateTime before = LocalDateTime.now();
		Thread.sleep(2);
		final Subscription subscriptionToAdd = new Subscription()
				.setMobileNumber(mobileNumber)
				.setOwnerId(20l).setUserId(21l)
				.setServiceType(ServiceType.MOBILE_POSTPAID.toLabelService());
		
		final Subscription subscriptionResult = this.controller.addSubscriber(subscriptionToAdd);

		Assert.assertNotNull(subscriptionResult);
		
		Assert.assertThat(subscriptionResult.getMobileNumber(), IsEqual.equalTo(mobileNumber));
		Assert.assertThat(subscriptionResult.getOwnerId(), IsEqual.equalTo(20l));
		Assert.assertThat(subscriptionResult.getUserId(), IsEqual.equalTo(21l));
		Assert.assertThat(subscriptionResult.getServiceType(), IsEqual.equalTo(ServiceType.MOBILE_POSTPAID.toLabelService()));
		
		Thread.sleep(2);
		
		final LocalDateTime after = LocalDateTime.now();
		Assert.assertTrue(subscriptionResult.getSubscriptionDate().isAfter(before));
		Assert.assertTrue(subscriptionResult.getSubscriptionDate().isBefore(after));
	}

	@Test(expected=IllegalArgumentException.class)
	public void addSubscriptionExist() {
		final String mobileNumber = "356789654";
		
		final Subscription subscriptionToAdd = new Subscription()
				.setMobileNumber(mobileNumber)
				.setOwnerId(20l).setUserId(21l)
				.setServiceType(ServiceType.MOBILE_POSTPAID.toLabelService());
		
		final Subscription subscriptionResult = this.controller.addSubscriber(subscriptionToAdd);

		Assert.assertNotNull(subscriptionResult);
	
		try {
			this.controller.addSubscriber(subscriptionToAdd);
		} catch(final IllegalArgumentException e) {
			Assert.assertThat(e.getMessage(),StringContainsInOrder.stringContainsInOrder(Lists.list("Number", "Exists", mobileNumber)));
			throw e;
		}
		Assert.fail();
	}
	

	@Test
	public void deleteSubscriberTest() {
		final Long  id = mobileSubscriptinIdList.get(3);
		final MobileSubscription subscription = this.testEntityManager.find(MobileSubscription.class, id);
		
		Assert.assertNotNull(subscription);
		
		this.controller.deleteSubscriber(id);
		this.controller.deleteSubscriber(123456l); //It does not exist, but no exception is thrown.
		
		
		final MobileSubscription  subscription2 = this.testEntityManager.find(MobileSubscription.class, id);
		

		Assert.assertNull(subscription2);
	}

	@Test
	public void modifySubscriberTest_modifyAll() {
		final Long  id = mobileSubscriptinIdList.get(3);
		final MobileSubscription subscriptionBefore = this.testEntityManager.find(MobileSubscription.class, id);
		
		Assert.assertNotNull(subscriptionBefore);
		
		final Subscription newSubscriptionDetails =  new Subscription()
				.setOwnerId(11l)
				.setUserId(12l)
				.setServiceType(ServiceType.MOBILE_POSTPAID.toLabelService());
		
		final Subscription subscription = this.controller.modifySubscriber(id, newSubscriptionDetails);

		Assert.assertNotNull(subscription);
		Assert.assertThat(subscription.getId(), IsEqual.equalTo(id));
		Assert.assertThat(subscription.getMobileNumber(), IsEqual.equalTo(subscriptionBefore.getNumber()));
		Assert.assertThat(subscription.getOwnerId(), IsEqual.equalTo(11L));
		Assert.assertThat(subscription.getUserId(), IsEqual.equalTo(12L));
		Assert.assertThat(subscription.getServiceType(), IsEqual.equalTo(ServiceType.MOBILE_POSTPAID.toLabelService()));
	}
	
	@Test(expected=EmptyResultDataAccessException.class)
	public void modifySubscriberTest_modifyWrongID() {
		final Long  id = 12345678l;
		final MobileSubscription subscriptionBefore = this.testEntityManager.find(MobileSubscription.class, id);
		
		Assert.assertNull(subscriptionBefore);
		
		final Subscription newSubscriptionDetails =  new Subscription()
				.setOwnerId(11l)
				.setUserId(12l)
				.setServiceType(ServiceType.MOBILE_POSTPAID.toLabelService());
		
		try {
			this.controller.modifySubscriber(id, newSubscriptionDetails);
		} catch(final EmptyResultDataAccessException e) {
			Assert.assertThat(e.getMessage(), StringContainsInOrder.stringContainsInOrder(Lists.list("Subscription" , String.valueOf(id), "not found")));
			throw e;
		}
	}
	
	
	@Test
	public void modifySubscriberTest_modifyOwnerId() {
		final Long  id = mobileSubscriptinIdList.get(3);
		final MobileSubscription subscriptionBefore = this.testEntityManager.find(MobileSubscription.class, id);
		
		Assert.assertNotNull(subscriptionBefore);
		
		final Subscription newSubscriptionDetails =  new Subscription().setOwnerId(11l);
		
		final Subscription subscription = this.controller.modifySubscriber(id, newSubscriptionDetails);

		Assert.assertNotNull(subscription);
		Assert.assertThat(subscription.getId(), IsEqual.equalTo(id));
		Assert.assertThat(subscription.getMobileNumber(), IsEqual.equalTo(subscriptionBefore.getNumber()));
		Assert.assertThat(subscription.getOwnerId(), IsEqual.equalTo(11L));
		Assert.assertThat(subscription.getUserId(), IsEqual.equalTo(subscriptionBefore.getCustomerIdUser()));
		Assert.assertThat(subscription.getServiceType(), IsEqual.equalTo(subscriptionBefore.getServiceType().toLabelService()));
	}

	@Test
	public void modifySubscriberTest_modifyUserId() {
		final Long  id = mobileSubscriptinIdList.get(3);
		final MobileSubscription subscriptionBefore = this.testEntityManager.find(MobileSubscription.class, id);
		
		Assert.assertNotNull(subscriptionBefore);
		
		final Subscription newSubscriptionDetails =  new Subscription().setUserId(12l);
		
		final Subscription subscription = this.controller.modifySubscriber(id, newSubscriptionDetails);

		Assert.assertNotNull(subscription);
		Assert.assertThat(subscription.getId(), IsEqual.equalTo(id));
		Assert.assertThat(subscription.getMobileNumber(), IsEqual.equalTo(subscriptionBefore.getNumber()));
		Assert.assertThat(subscription.getOwnerId(), IsEqual.equalTo(subscriptionBefore.getCustomerIdOwner()));
		Assert.assertThat(subscription.getUserId(), IsEqual.equalTo(12L));
		Assert.assertThat(subscription.getServiceType(), IsEqual.equalTo(subscriptionBefore.getServiceType().toLabelService()));
	}

	@Test
	public void modifySubscriberTest_modifyPlan() {
		final Long  id = mobileSubscriptinIdList.get(3);
		final MobileSubscription subscriptionBefore = this.testEntityManager.find(MobileSubscription.class, id);
		
		Assert.assertNotNull(subscriptionBefore);
		
		final Subscription newSubscriptionDetails =  new Subscription().setServiceType(ServiceType.MOBILE_POSTPAID.toLabelService());
		
		final Subscription subscription = this.controller.modifySubscriber(id, newSubscriptionDetails);

		Assert.assertNotNull(subscription);
		Assert.assertThat(subscription.getId(), IsEqual.equalTo(id));
		Assert.assertThat(subscription.getMobileNumber(), IsEqual.equalTo(subscriptionBefore.getNumber()));
		Assert.assertThat(subscription.getOwnerId(), IsEqual.equalTo(subscriptionBefore.getCustomerIdOwner()));
		Assert.assertThat(subscription.getUserId(), IsEqual.equalTo(subscriptionBefore.getCustomerIdUser()));
		Assert.assertThat(subscription.getServiceType(), IsEqual.equalTo(ServiceType.MOBILE_POSTPAID.toLabelService()));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void modifySubscriberTest_IllegalModifyID() {
		final Long id = 12390l;
		final Subscription newSubscriptionDetails =  new Subscription().setId(12345l);
		
		try {
			this.controller.modifySubscriber(id, newSubscriptionDetails);
		} catch(final IllegalArgumentException e) {
			Assert.assertThat(e.getMessage(),StringContainsInOrder.stringContainsInOrder(Lists.list("ID", "cannot be", "modified")));
			throw e;
		}

	}
	
	@Test(expected=IllegalArgumentException.class)
	public void modifySubscriberTest_IllegalModifyMobileNumber() {
		final Long id = 12390l;
		final Subscription newSubscriptionDetails =  new Subscription().setMobileNumber("356997803563");
		
		try {
			this.controller.modifySubscriber(id, newSubscriptionDetails);
		} catch(final IllegalArgumentException e) {
			Assert.assertThat(e.getMessage(),StringContainsInOrder.stringContainsInOrder(Lists.list("Mobile", "Number", "cannot be", "modified")));
			throw e;
		}

	}
	
	@Test(expected=IllegalArgumentException.class)
	public void modifySubscriberTest_IllegalModifySubscriptionDate() {
		final Long id = 12390l;
		final Subscription newSubscriptionDetails =  new Subscription()
				.setSubscriptionDate(LocalDateTime.now());
		
		try {
			this.controller.modifySubscriber(id, newSubscriptionDetails);
		} catch(final IllegalArgumentException e) {
			Assert.assertThat(e.getMessage(),StringContainsInOrder.stringContainsInOrder(Lists.list("date", "cannot be", "modified")));
			throw e;
		}

	}
	
}
