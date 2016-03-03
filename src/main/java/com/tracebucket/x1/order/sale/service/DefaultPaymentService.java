package com.tracebucket.x1.order.sale.service;

import com.tracebucket.x1.order.base.jpa.repository.OrderRepository;
import com.tracebucket.x1.order.sale.PaymentException;
import com.tracebucket.x1.order.sale.domain.*;
import com.tracebucket.x1.order.sale.jpa.repository.CreditCardRepository;
import com.tracebucket.x1.order.sale.jpa.repository.PaymentRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Implementation of {@link PaymentService}.
 *
 * @author ffazil
 * @since 04/02/16
 */
@Service
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Autowired) )
public class DefaultPaymentService implements PaymentService{

    @NonNull
    private final CreditCardRepository creditCardRepository;
    @NonNull
    private final PaymentRepository paymentRepository;
    @NonNull
    private final OrderRepository orderRepository;
    @NonNull
    private final ApplicationEventPublisher publisher;

    @Override
    public CreditCardPayment pay(SaleOrder saleOrder, CreditCardNumber creditCardNumber) {
        if (saleOrder.isPaid()) {
            throw new PaymentException(saleOrder, "Order already paid!");
        }

        Optional<CreditCard> creditCardResult = creditCardRepository.findByNumber(creditCardNumber);

        if (!creditCardResult.isPresent()) {
            throw new PaymentException(saleOrder,
                    String.format("No credit card found for number: %s", creditCardNumber.getNumber()));
        }

        CreditCard creditCard = creditCardResult.get();

        if (!creditCard.isValid()) {
            throw new PaymentException(saleOrder, String.format("Invalid credit card with number %s, expired %s!",
                    creditCardNumber.getNumber(), creditCard.getExpirationDate()));
        }

        saleOrder.markPaid();
        CreditCardPayment payment = paymentRepository.save(new CreditCardPayment(creditCard, saleOrder));

        publisher.publishEvent(new SaleOrderPaidEvent(saleOrder.getId()));

        return payment;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Payment> getPaymentFor(SaleOrder saleOrder) {
        return paymentRepository.findBySaleOrder(saleOrder);
    }

    @Override
    public Optional<Payment.Receipt> takeReceiptFor(SaleOrder saleOrder) {
        saleOrder.markTaken();
        orderRepository.save(saleOrder);

        return getPaymentFor(saleOrder).map(Payment::getReceipt);
    }
}
