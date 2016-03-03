package com.tracebucket.x1.order.sale.domain;

import lombok.Getter;
import org.springframework.util.Assert;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * A {@link Payment} done through a {@link CreditCard}.
 * 
 * @author ffazil
 */
@Entity
@Getter
public class CreditCardPayment extends Payment {

	private final @ManyToOne CreditCard creditCard;

	//For Jpa
	protected CreditCardPayment() {
		this.creditCard = null;
	}

	/**
	 * Creates a new {@link CreditCardPayment} for the given {@link CreditCard} and {@link CreditCard}.
	 * 
	 * @param creditCard must not be {@literal null}.
	 * @param saleOrder
	 */
	public CreditCardPayment(CreditCard creditCard, SaleOrder saleOrder) {

		super(saleOrder);
		Assert.notNull(creditCard);
		this.creditCard = creditCard;
	}
}
