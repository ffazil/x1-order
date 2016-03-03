package com.tracebucket.x1.order.base.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.hateoas.Identifiable;

import javax.persistence.*;
import java.util.UUID;

/**
 * Abstraction of an entity for Order Framework.
 *
 * @author ffazil
 * @since 19/01/16
 */
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@MappedSuperclass
@Getter
@ToString
public abstract class BaseEntity implements Identifiable<String> {

    @Id
    @Column(name = "UID")
    @Setter
    protected String id;

    @JsonIgnore
    @Version
    private Long version;

    @JsonIgnore
    @Column(name = "PASSIVE", nullable = false, columnDefinition = "boolean default false")
    @Basic(fetch = FetchType.EAGER)
    private boolean passive;

    public BaseEntity() {
        this.passive = false;

    }



    public boolean isPassive() {
        return passive;
    }

    @PrePersist
    public void init(){
        if(this.id == null)
            this.id = UUID.randomUUID().toString();
    }


}