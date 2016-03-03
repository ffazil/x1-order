package com.tracebucket.x1.order.sale.domain;

import com.tracebucket.x1.order.base.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.Entity;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;

/**
 * Realisation of a credit card.
 * 
 * @author ffazil
 */
@Entity
@ToString(callSuper = true)
@AllArgsConstructor
public class CreditCard extends BaseEntity {

	private final @Getter CreditCardNumber number;
	private final @Getter String cardHolderName;

	private Month expiryMonth;
	private Year expiryYear;

	//For Jpa
	protected CreditCard() {
		this(null, null, null, null);
	}

	/**
	 * Returns whether the {@link CreditCard} is currently valid.
	 * 
	 * @return
	 */
	public boolean isValid() {
		return isValid(LocalDate.now());
	}

	/**
	 * Returns whether the {@link CreditCard} is valid for the given date.
	 * 
	 * @param date
	 * @return
	 */
	public boolean isValid(LocalDate date) {
		return date == null ? false : getExpirationDate().isAfter(date);
	}

	/**
	 * Returns the {@link LocalDate} the {@link CreditCard} expires.
	 * 
	 * @return will never be {@literal null}.
	 */
	public LocalDate getExpirationDate() {
		return LocalDate.of(expiryYear.getValue(), expiryMonth, 1);
	}

	/**
	 * Protected setter to allow binding the expiration date.
	 * 
	 * @param date
	 */
	protected void setExpirationDate(LocalDate date) {

		this.expiryYear = Year.of(date.getYear());
		this.expiryMonth = date.getMonth();
	}
}
