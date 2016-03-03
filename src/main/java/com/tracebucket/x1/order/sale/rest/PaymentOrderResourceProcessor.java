package com.tracebucket.x1.order.sale.rest;

import com.tracebucket.x1.order.sale.domain.SaleOrder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.stereotype.Component;

/**
 * {@link ResourceProcessor} to enrich {@link SaleOrder} {@link Resource}s with links to the {@link PaymentController}.
 * Ah Nirvana !!!
 * 
 * @author ffazil
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class PaymentOrderResourceProcessor implements ResourceProcessor<Resource<SaleOrder>> {

	private final @NonNull PaymentLinks paymentLinks;

	/* 
	 * (non-Javadoc)
	 * @see org.springframework.hateoas.ResourceProcessor#process(org.springframework.hateoas.ResourceSupport)
	 */
	@Override
	public Resource<SaleOrder> process(Resource<SaleOrder> resource) {

		SaleOrder saleOrder = resource.getContent();

		if (!saleOrder.isPaid()) {
			resource.add(paymentLinks.getPaymentLink(saleOrder));
		}

		if (saleOrder.isReady()) {
			resource.add(paymentLinks.getReceiptLink(saleOrder));
		}

		return resource;
	}
}
