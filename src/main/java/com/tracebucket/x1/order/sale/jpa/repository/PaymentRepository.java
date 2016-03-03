package com.tracebucket.x1.order.sale.jpa.repository;

import com.tracebucket.x1.order.sale.domain.Payment;
import com.tracebucket.x1.order.sale.domain.SaleOrder;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

/**
 * Repository interface to manage {@link Payment} instances.
 * 
 * @author ffazil
 */
public interface PaymentRepository extends PagingAndSortingRepository<Payment, String> {

	/**
	 * Returns the payment registered for the given {@link SaleOrder}.
	 * 
	 * @param saleOrder
	 * @return
	 */
	Optional<Payment> findBySaleOrder(SaleOrder saleOrder);
}
