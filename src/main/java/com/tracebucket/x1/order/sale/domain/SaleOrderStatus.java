package com.tracebucket.x1.order.sale.domain;

/**
 * States of a {@link SaleOrder}
 *
 * @author ffazil
 * @since 04/02/16
 */
public enum SaleOrderStatus {

    /**
     * Placed, but not paid yet. Still changeable.
     */
    PAYMENT_EXPECTED,

    /**
     * {@link SaleOrder} was paid. No changes allowed to it anymore.
     */
    PAID,

    /**
     * The {@link SaleOrder} is currently processed.
     */
    PROCESSING,

    /**
     * The {@link SaleOrder} is ready to be picked up by the customer.
     */
    READY,

    /**
     * The {@link SaleOrder} was completed.
     */
    TAKEN;
}
