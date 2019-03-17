package eu.davidemartorana.mobile.numbers.source.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import eu.davidemartorana.mobile.numbers.source.domain.MobileSubscription;

/**
 * Repository for class {@link MobileSubscription}. 
 * 
 * @author davidemartorana
 *
 */
@Repository
public interface MobileSubscriptionsRepository extends PagingAndSortingRepository<MobileSubscription, Long>,JpaSpecificationExecutor<MobileSubscription> {
	
	
	Optional<MobileSubscription> findByNumber(String number);
	
}
