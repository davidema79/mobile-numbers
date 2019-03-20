package eu.davidemartorana.mobile.numbers.source.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import eu.davidemartorana.mobile.numbers.source.domain.MobileSubscription;

/**
 * Repository for class {@link MobileSubscription}. 
 * 
 * @author davidemartorana
 *
 */
@Repository
public interface MobileSubscriptionsRepository extends JpaRepository<MobileSubscription, Long> {
	
	
	Optional<MobileSubscription> findByNumber(String number);
	
}
