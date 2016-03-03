package com.tracebucket.x1.order.sale.rest;

import com.tracebucket.x1.order.base.domain.BaseOrder;
import com.tracebucket.x1.order.sale.domain.*;
import com.tracebucket.x1.order.sale.jpa.repository.SaleOrderRepository;
import com.tracebucket.x1.order.sale.service.PaymentService;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.support.DomainClassConverter;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.money.MonetaryAmount;

import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * Spring MVC controller to handle payments for an {@link SaleOrder}.
 * 
 * @author ffazil
 */
@Controller
@RequestMapping("/orders/{id}")
@ExposesResourceFor(Payment.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired) )
public class PaymentController {

	private final @NonNull
	PaymentService paymentService;
	private final @NonNull
	EntityLinks entityLinks;
	@NonNull
	private final SaleOrderRepository saleOrderRepository;

	/**
	 * Accepts a payment for an {@link SaleOrder}
	 * 
	 * @param orderId the {@link SaleOrder} to process the payment for. Retrieved from the path variable and converted into an
	 *          {@link SaleOrder} instance by Spring Data's {@link DomainClassConverter}. Will be {@literal null} in case no
	 *          {@link SaleOrder} with the given id could be found.
	 * @param number the {@link CreditCardNumber} unmarshalled from the request payload.
	 * @return
	 */
	//Unable to resolve an Entity from {@link org.springframework.hateoas.Link} while injecting as a path variable. Will be fixed in the later versions of spring hateoas
	@RequestMapping(path = PaymentLinks.PAYMENT, method = PUT)
	ResponseEntity<?> submitPayment(@PathVariable("id") String orderId, @RequestBody CreditCardNumber number) {

		SaleOrder saleOrder = (SaleOrder) saleOrderRepository.findById(orderId);

		if (saleOrder == null || saleOrder.isPaid()) {
			return ResponseEntity.notFound().build();
		}

		CreditCardPayment payment = paymentService.pay(saleOrder, number);

		PaymentResource resource = new PaymentResource(saleOrder.getTotalAmount(), payment.getCreditCard());
		resource.add(entityLinks.linkToSingleResource(BaseOrder.class, saleOrder.getId()).withRel("saleOrder"));

		return new ResponseEntity<>(resource, HttpStatus.CREATED);
	}

	/**
	 * Shows the {@link com.tracebucket.x1.order.sale.domain.Payment.Receipt} for the given order.
	 * 
	 * @param orderId
	 * @return
	 */
	//Unable to resolve an Entity from {@link org.springframework.hateoas.Link} while injecting as a path variable. Will be fixed in the later versions of spring hateoas
	@RequestMapping(path = PaymentLinks.RECEIPT, method = GET)
	HttpEntity<?> showReceipt(@PathVariable("id") String orderId) {

		SaleOrder saleOrder = (SaleOrder) saleOrderRepository.findById(orderId);

		if (saleOrder == null || !saleOrder.isPaid() || saleOrder.isTaken()) {
			return ResponseEntity.notFound().build();
		}

		return paymentService.getPaymentFor(saleOrder).//
				map(payment -> createReceiptResponse(payment.getReceipt())).//
				orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}

	/**
	 * Takes the {@link com.tracebucket.x1.order.sale.domain.Payment.Receipt} for the given {@link SaleOrder} and thus completes the process.
	 * 
	 * @param orderId
	 * @return
	 */
	//Unable to resolve an Entity from {@link org.springframework.hateoas.Link} while injecting as a path variable. Will be fixed in the later versions of spring hateoas
	@RequestMapping(path = PaymentLinks.RECEIPT, method = DELETE)
	HttpEntity<?> takeReceipt(@PathVariable("id") String orderId) {

		SaleOrder saleOrder = (SaleOrder) saleOrderRepository.findById(orderId);

		if (saleOrder == null || !saleOrder.isPaid()) {
			return ResponseEntity.notFound().build();
		}

		return paymentService.takeReceiptFor(saleOrder).//
				map(receipt -> createReceiptResponse(receipt)).//
				orElseGet(() -> new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED));
	}

	/**
	 * Renders the given {@link com.tracebucket.x1.order.sale.domain.Payment.Receipt} including links to the associated {@link SaleOrder} as well as a self link in case
	 * the {@link com.tracebucket.x1.order.sale.domain.Payment.Receipt} is still available.
	 * 
	 * @param receipt
	 * @return
	 */
	private HttpEntity<Resource<Payment.Receipt>> createReceiptResponse(Payment.Receipt receipt) {

		SaleOrder saleOrder = receipt.getSaleOrder();

		Resource<Payment.Receipt> resource = new Resource<>(receipt);
		resource.add(entityLinks.linkToSingleResource(BaseOrder.class, saleOrder.getId()).withRel("SaleOrder"));

		if (!saleOrder.isTaken()) {
			resource.add(entityLinks.linkForSingleResource(BaseOrder.class, saleOrder.getId()).slash("receipt").withSelfRel());
		}

		return ResponseEntity.ok(resource);
	}

	/**
	 * Resource implementation for payment results.
	 * 
	 * @author ffazil
	 */
	@Data
	@EqualsAndHashCode(callSuper = true)
	static class PaymentResource extends ResourceSupport {

		private final MonetaryAmount amount;
		private final CreditCard creditCard;
	}
}
