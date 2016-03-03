package com.tracebucket.x1.order.sale.domain;

import com.tracebucket.x1.order.base.domain.BaseEntity;
import lombok.Getter;
import lombok.ToString;
import lombok.Value;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Abstraction of a payment for {@link SaleOrder}.
 * 
 * @author ffazil
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Getter
@ToString(callSuper = true)
public abstract class Payment extends BaseEntity {

	@JoinColumn(name = "saleorder")//
	@OneToOne(cascade = CascadeType.MERGE)//
	private final SaleOrder saleOrder;
	private final LocalDateTime paymentDate;

	protected Payment() {

		this.saleOrder = null;
		this.paymentDate = null;
	}

	/**
	 * Creates a new {@link Payment} referring to the given {@link SaleOrder}.
	 * 
	 * @param saleOrder must not be {@literal null}.
	 */
	public Payment(SaleOrder saleOrder) {

		Assert.notNull(saleOrder);
		this.saleOrder = saleOrder;
		this.paymentDate = LocalDateTime.now();
	}

	/**
	 * Returns a receipt for the {@link Payment}.
	 * 
	 * @return
	 */
	public Receipt getReceipt() {
		return new Receipt(paymentDate, saleOrder);
	}

	/**
	 * A receipt for an {@link SaleOrder} and a payment date.
	 * 
	 * @author ffazil
	 */
	@Value
	public static class Receipt {

		private final LocalDateTime date;
		private final SaleOrder saleOrder;
	}
}
