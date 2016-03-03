package com.tracebucket.x1.order.sale.rest;

import com.tracebucket.x1.order.base.domain.BaseOrder;
import com.tracebucket.x1.order.sale.domain.SaleOrder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

/**
 * Helper component to create links for {@link com.tracebucket.x1.order.sale.domain.Payment} and {@link com.tracebucket.x1.order.sale.domain.Payment.Receipt}.
 * 
 * @author ffazil
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class PaymentLinks {

	static final String PAYMENT = "/payment";
	static final String RECEIPT = "/receipt";
	static final String PAYMENT_REL = "payment";
	static final String RECEIPT_REL = "receipt";

	private final @NonNull
	EntityLinks entityLinks;

	/**
	 * Returns the {@link Link} to point to the {@link com.tracebucket.x1.order.sale.domain.Payment} for the given {@link SaleOrder}.
	 * 
	 * @param saleOrder must not be {@literal null}.
	 * @return
	 */
	Link getPaymentLink(SaleOrder saleOrder) {
		return entityLinks.linkForSingleResource(BaseOrder.class, saleOrder.getId()).slash(PAYMENT).withRel(PAYMENT_REL);
	}

	/**
	 * Returns the {@link Link} to the {@link com.tracebucket.x1.order.sale.domain.Payment.Receipt} of the given {@link SaleOrder}.
	 * 
	 * @param saleOrder
	 * @return
	 */
	Link getReceiptLink(SaleOrder saleOrder) {
		return entityLinks.linkForSingleResource(BaseOrder.class, saleOrder.getId()).slash(RECEIPT).withRel(RECEIPT_REL);
	}
}
