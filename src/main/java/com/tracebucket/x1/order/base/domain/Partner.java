package com.tracebucket.x1.order.base.domain;

import lombok.Getter;

import javax.persistence.*;
import java.util.Set;

/**
 * Wrapper class of partner in the order context. Partner's of an {@link Organization} interact in an Order. Examples of partners are
 * Supplier, Employee and Customer.
 *
 * @author ffazil
 * @since 18/01/16
 */
@Entity
@Getter
public class Partner extends BaseEntity{
    private String partnerReference;

    @ElementCollection(targetClass = OrderFunction.class)
    @CollectionTable(name = "order_functions", joinColumns = @JoinColumn(name = "function_id"))
    @Column(name = "partnerFunctionsofOrder", nullable = false)
    @Enumerated(EnumType.STRING)
    private Set<OrderFunction> orderFunctions;
}
