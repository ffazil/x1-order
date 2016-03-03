package com.tracebucket.x1.order.sale;

import com.tracebucket.x1.order.sale.domain.SaleOrder;
import lombok.Getter;
import org.springframework.util.Assert;

/**
 * Exception for payment related race conditions.
 *
 * @author ffazil
 */
@Getter
public class PaymentException extends RuntimeException {

	private static final long serialVersionUID = -4929826142920520541L;
	private final SaleOrder saleOrder;

	public PaymentException(SaleOrder saleOrder, String message) {

		super(message);

		Assert.notNull(saleOrder);
		this.saleOrder = saleOrder;
	}
}
