package com.tracebucket.x1.order.base.domain;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.tracebucket.x1.order.sale.domain.SaleOrder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.javamoney.moneta.Money;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.money.MonetaryAmount;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Abstraction of an order. An order contains a header and line items. Various types of orders will be realised extending the base order.
 *
 * @author ffazil
 * @since 18/01/16
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@DiscriminatorColumn(name = "orderType")
@Getter
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "orderType")
@JsonSubTypes({
        @JsonSubTypes.Type(value = SaleOrder.class, name = "SaleOrder")})
@ToString(exclude = {"orderId", "createdBy", "modifiedBy", "lastModified", "orderDates", "referenceOrders", "partnerFunctions", "lineItems"}, callSuper = true)
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseOrder extends BaseEntity{
    private final String orderId;

    @CreatedDate
    private LocalDateTime created;

    @CreatedBy
    private String createdBy;

    @LastModifiedBy
    private String modifiedBy;

    @LastModifiedDate
    protected LocalDateTime lastModified;

    @Column(updatable = false, insertable = false)
    protected final String orderType;



    @ElementCollection
    @CollectionTable(name = "order_dates")
    @MapKeyColumn(name = "order_dates_id")
    @Column(name = "order_date")
    private Map<DateType, Date> orderDates = new HashMap<>(0);

    @ElementCollection
    @CollectionTable(name="reference_orders")
    @Column(name="ref")
    private Set<String> referenceOrders = new HashSet<>(0);

    @OneToMany
    private Set<PartnerFunction> partnerFunctions = new HashSet<>(0);

    @ManyToOne
    @Setter
    private Organization organization;

    @OneToMany(cascade = CascadeType.ALL)
    @OrderBy("productReference ASC")
    private final SortedSet<BaseLineItem> lineItems = new TreeSet<BaseLineItem>();


    public BaseOrder(Collection<BaseLineItem> items){
        this.lineItems.addAll(items);
        this.orderId = UUID.randomUUID().toString();
        this.orderType = this.getClass().getSimpleName();
    }

    public BaseOrder(BaseLineItem... lineItems){
        this(Arrays.asList(lineItems));
    }

    protected BaseOrder(){
        this(new BaseLineItem[0]);
    }
    /**
     * Sum of sub totals across all line items
     * @return
     */
    public MonetaryAmount getTotalAmount(){
        return lineItems.stream()
                .filter(item -> item.getSubTotal() != null)
                .map(BaseLineItem::getSubTotal)
                .reduce(MonetaryAmount::add)
                .orElse(Money.of(0.0, "INR"));
    }


    public abstract String type();

}
