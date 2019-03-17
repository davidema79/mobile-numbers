package eu.davidemartorana.mobile.numbers.source.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;

import eu.davidemartorana.mobile.numbers.source.domain.MobileSubscription;

/**
 * Repository for class {@link MobileSubscription}. 
 * 
 * @author davidemartorana
 *
 */
public interface MobileSubscriptionsRepository extends PagingAndSortingRepository<MobileSubscription, Long> {

}
