package com.tracebucket.x1.order.base.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.javamoney.moneta.Money;

import javax.money.MonetaryAmount;
import javax.persistence.Entity;
import javax.persistence.Lob;

/**
 * Realization of a basic line item, which may be extended at a later stage for specific implementations.
 *
 * @author ffazil
 * @since 18/01/16
 */
@Entity
@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseLineItem extends BaseEntity implements Comparable<BaseLineItem>{

    private final Integer sequenceNumber;

    private final String productReference;

    @Lob
    private final Money unitPrice;

    private final int quantity;

    private final String comment;

    private final String orderReference;

    protected BaseLineItem(){
        this(0, null, null, 0, null, null);
    }



    public MonetaryAmount getSubTotal(){
        return unitPrice != null ? unitPrice.multiply(quantity) : null;
    }


    @Override
    public int compareTo(BaseLineItem o) {
        return this.getProductReference().compareTo(o.getProductReference());
    }
}
