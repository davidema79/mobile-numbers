package eu.davidemartorana.mobile.numbers.rest;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.notNullValue;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.collection.IsCollectionWithSize;
import org.hamcrest.collection.IsEmptyCollection;
import org.hamcrest.core.IsCollectionContaining;
import org.hamcrest.core.IsEqual;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import eu.davidemartorana.mobile.numbers.config.DataSourceConfig;
import eu.davidemartorana.mobile.numbers.domain.ServiceType;
import eu.davidemartorana.mobile.numbers.rest.dto.Subscription;
import eu.davidemartorana.mobile.numbers.services.SubscriptionConverter;
import eu.davidemartorana.mobile.numbers.services.SubscriptionsService;
import eu.davidemartorana.mobile.numbers.source.domain.MobileSubscription;
import eu.davidemartorana.mobile.numbers.source.repositories.MobileSubscriptionsRepository;

/**
 * Tests
 * 
 * @author davidemartorana
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@DataJpaTest
@ContextConfiguration(classes = { SubscriptionsControllerTest.Config.class })
@AutoConfigureTestDatabase(replace=Replace.NONE)
public class SubscriptionsControllerTest {

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

	@Before
	public void populateDataBase() {

		mobileSubscriptinList.add(new MobileSubscription("35677986001", 2l, 2l, ServiceType.MOBILE_POSTPAID));
		mobileSubscriptinList.add(new MobileSubscription("35677986002", 2l, 3l, ServiceType.MOBILE_PREPAID));
		mobileSubscriptinList.add(new MobileSubscription("35677986003", 4l, 5l, ServiceType.MOBILE_POSTPAID));
		mobileSubscriptinList.add(new MobileSubscription("35677986004", 10l, 10l, ServiceType.MOBILE_PREPAID));

		this.testEntityManager.persist(mobileSubscriptinList.get(0));
		this.testEntityManager.persist(mobileSubscriptinList.get(1));
		this.testEntityManager.persist(mobileSubscriptinList.get(2));
		this.testEntityManager.persist(mobileSubscriptinList.get(3));
		//this.testEntityManager.flush();
	}
	
	@After
	public void afterTest() {
		mobileSubscriptinList.clear();
	}

	@Test
	public void getSubscriberTest() {
		final List<Subscription> allList = this.controller.getSubscribers(null, null);

		Assert.assertThat(allList, notNullValue());
		Assert.assertEquals(allList, hasItems(mobileSubscriptinList.toArray()));

		final List<Subscription> emptyList = this.controller.getSubscribers(1l, 1l);
		Assert.assertThat(emptyList, notNullValue());
		Assert.assertThat(emptyList, IsEmptyCollection.empty());
		
		final List<Subscription> twoItemsInList = this.controller.getSubscribers(2l, null);
		Assert.assertThat(twoItemsInList, notNullValue());
		Assert.assertThat(twoItemsInList, IsCollectionWithSize.hasSize(2));
		
		final Subscription first = SubscriptionConverter.convertToSubscription(mobileSubscriptinList.get(0));
		final Subscription second = SubscriptionConverter.convertToSubscription(mobileSubscriptinList.get(1));
		
		Assert.assertThat(twoItemsInList, IsCollectionContaining.hasItems(first, second));
	}

	@Test
	public void getSubscriberByIdTest() {
		final Subscription subscription = this.controller.getSubscriberById(1l);

		Assert.assertNotNull(subscription);
		final Subscription first = SubscriptionConverter.convertToSubscription(mobileSubscriptinList.get(0));
		
		Assert.assertThat(subscription, IsEqual.equalTo(first));
		
		final Subscription subscriptionNotExist = this.controller.getSubscriberById(10l);

		Assert.assertNull(subscriptionNotExist);
		
	}

	@Test
	public void addSubscriberTest() {
		final Subscription subscription = this.controller.addSubscriber(new Subscription().setMobileNumber("356789654")
				.setOwnerId(20l).setUserId(21l).setServiceType(ServiceType.MOBILE_POSTPAID.toLabelService()));

		Assert.assertNotNull(subscription);
	}

	@Test
	public void deleteSubscriberTest() {
		this.controller.deleteSubscriber(4l);
		this.controller.deleteSubscriber(5l); //It does not exist, but no exception is thrown.
	}

	@Test
	public void modifySubscriberTest() {
		final Subscription subscription = this.controller.modifySubscriber(4l, new Subscription().setOwnerId(11l)
				.setUserId(12l).setServiceType(ServiceType.MOBILE_POSTPAID.toLabelService()));

		Assert.assertNotNull(subscription);
	}
}
