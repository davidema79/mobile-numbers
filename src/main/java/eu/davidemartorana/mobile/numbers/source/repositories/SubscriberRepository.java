package eu.davidemartorana.mobile.numbers.source.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;

import eu.davidemartorana.mobile.numbers.source.domain.Subscriber;

/**
 * Repository for class {@link Subscriber}. 
 * 
 * @author davidemartorana
 *
 */
public interface SubscriberRepository extends PagingAndSortingRepository<Subscriber, Long> {

}
