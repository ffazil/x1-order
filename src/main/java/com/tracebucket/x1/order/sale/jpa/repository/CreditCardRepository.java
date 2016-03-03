package com.tracebucket.x1.order.sale.jpa.repository;

import com.tracebucket.x1.order.sale.domain.CreditCard;
import com.tracebucket.x1.order.sale.domain.CreditCardNumber;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * Repository to access {@link CreditCard} instances.
 * 
 * @author ffazil
 */
public interface CreditCardRepository extends CrudRepository<CreditCard, String> {

	/**
	 * Returns the {@link CreditCard} associated with the given {@link CreditCardNumber}.
	 * 
	 * @param number must not be {@literal null}.
	 * @return
	 */
	Optional<CreditCard> findByNumber(CreditCardNumber number);
}
