package com.tracebucket.x1.order.base.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.Entity;

/**
 * @author ffazil
 * @since 10/02/16
 */
@Entity
@Getter
@AllArgsConstructor
@ToString(callSuper = true)
public class Organization extends BaseEntity{

    private final String reference;

    protected Organization(){
        this.reference = null;
    }

}
