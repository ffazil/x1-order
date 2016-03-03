package com.tracebucket.x1.order.sale.service;

import com.tracebucket.x1.order.sale.domain.CreditCardNumber;
import com.tracebucket.x1.order.sale.domain.CreditCardPayment;
import com.tracebucket.x1.order.sale.domain.Payment;
import com.tracebucket.x1.order.sale.domain.SaleOrder;

import java.util.Optional;

/**
 * Contract for payments of a sale order.
 * 
 * @author ffazil
 */
public interface PaymentService {

	/**
	 * Pay the given {@link SaleOrder} with the {@link com.tracebucket.x1.order.sale.domain.CreditCard} identified by the given {@link CreditCardNumber}.
	 * 
	 * @param saleOrder
	 * @param creditCardNumber
	 * @return
	 */
	CreditCardPayment pay(SaleOrder saleOrder, CreditCardNumber creditCardNumber);

	/**
	 * Returns the {@link Payment} for the given {@link SaleOrder}.
	 * 
	 * @param saleOrder
	 * @return the {@link Payment} for the given {@link SaleOrder} or {@link Optional#empty()} if the Order hasn't been payed
	 *         yet.
	 */
	Optional<Payment> getPaymentFor(SaleOrder saleOrder);

	/**
	 * Takes the receipt
	 * 
	 * @param saleOrder
	 * @return
	 */
	Optional<Payment.Receipt> takeReceiptFor(SaleOrder saleOrder);
}
