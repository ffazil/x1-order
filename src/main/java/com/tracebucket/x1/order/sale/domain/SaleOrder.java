package com.tracebucket.x1.order.sale.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tracebucket.x1.order.base.domain.BaseLineItem;
import com.tracebucket.x1.order.base.domain.BaseOrder;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.Entity;
import java.util.Arrays;
import java.util.Collection;

/**
 * @author ffazil
 * @since 03/02/16
 *
 * Implementation of a sales order
 */
@Entity
@Getter
@ToString(callSuper = true)
public class SaleOrder extends BaseOrder{
    private static final String type = "Sale order";

    private SaleOrderStatus saleOrderStatus;

    @JsonCreator
    public SaleOrder(@JsonProperty("lineItems") Collection<BaseLineItem> lineItems){
        super(lineItems);
        this.saleOrderStatus = SaleOrderStatus.PAYMENT_EXPECTED;
    }

    public SaleOrder(BaseLineItem... lineItems){
        this(Arrays.asList(lineItems));
    }

    //For Jpa
    public SaleOrder(){
        this(new BaseLineItem[0]);
    }

    /**
     * Returns whether the {@link SaleOrder} has been paid already.
     *
     * @return
     */
    public boolean isPaid() {
        return !this.saleOrderStatus.equals(SaleOrderStatus.PAYMENT_EXPECTED);
    }

    /**
     * Returns if the {@link SaleOrder} is ready to be taken.
     *
     * @return
     */
    public boolean isReady() {
        return this.saleOrderStatus.equals(SaleOrderStatus.READY);
    }

    public boolean isTaken() {
        return this.saleOrderStatus.equals(SaleOrderStatus.TAKEN);
    }

    /**
     * Marks the {@link SaleOrder} as paid.
     */
    public void markPaid() {

        if (isPaid()) {
            throw new IllegalStateException("Paid order cannot be paid again!");
        }

        this.saleOrderStatus = SaleOrderStatus.PAID;
    }

    /**
     * Marks the {@link SaleOrder} as in preparation.
     */
    public void markInPreparation() {

        if (this.saleOrderStatus != SaleOrderStatus.PAID) {
            throw new IllegalStateException(
                    String.format("Order must be in state payed to start preparation! Current status: %s", this.saleOrderStatus));
        }

        this.saleOrderStatus = SaleOrderStatus.PROCESSING;
    }

    /**
     * Marks the {@link SaleOrder} as processed.
     */
    public void markProcessed() {

        if (this.saleOrderStatus != SaleOrderStatus.PROCESSING) {
            throw new IllegalStateException(String
                    .format("Cannot mark Order prepared that is currently not preparing! Current status: %s.", this.saleOrderStatus));
        }

        this.saleOrderStatus = SaleOrderStatus.READY;
    }

    public void markTaken() {

        if (this.saleOrderStatus != SaleOrderStatus.READY) {
            throw new IllegalStateException(
                    String.format("Cannot mark Order taken that is currently not paid! Current status: %s.", this.saleOrderStatus));
        }

        this.saleOrderStatus = SaleOrderStatus.TAKEN;
    }

    @Override
    public String getOrderType(){
        if(super.getOrderType() == null || super.getOrderType().isEmpty())
            return "SaleOrder";
        else
            return super.getOrderType();
    }





    @Override
    public String type() {
        return type;
    }
}
