package com.tracebucket.x1.order.base.domain;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToOne;

/**
 * Realization of labeling functions of a partner from the context of orders.
 *
 * @author ffazil
 * @since 18/01/16
 */
@Entity
@Getter
public class PartnerFunction extends BaseEntity{

    @OneToOne
    private Partner partner;

    @Enumerated(EnumType.STRING)
    private OrderFunction orderFunction;
}
