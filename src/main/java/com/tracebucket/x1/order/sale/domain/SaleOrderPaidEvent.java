package com.tracebucket.x1.order.sale.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Event to be published when an {@link SaleOrder} has been paid.
 * 
 * @author ffazil
 */
@Getter
@EqualsAndHashCode
@ToString
public class SaleOrderPaidEvent {

	private final String saleOrderId;

	/**
	 * Creates a new {@link SaleOrderPaidEvent}
	 *
	 * @param saleOrderId the id of the order that just has been paid
	 */
	public SaleOrderPaidEvent(String saleOrderId) {
		this.saleOrderId = saleOrderId;
	}
}
