package com.tracebucket.x1.order.sale.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Value object to represent a {@link CreditCardNumber}.
 * 
 * @author ffazil
 */
@Data
@Embeddable
public class CreditCardNumber {

	private static final String regex = "[0-9]{16}";

	private final @Column(unique = true) String number;

	protected CreditCardNumber() {
		this.number = null;
	}

	/**
	 * Creates a new {@link CreditCardNumber}.
	 * 
	 * @param number must not be {@literal null} and be a 16 digit numerical only String.
	 */
	@JsonCreator
	public CreditCardNumber(@JsonProperty("number") String number) {

		if (!isValid(number)) {
			throw new IllegalArgumentException(String.format("Invalid credit card NUMBER %s!", number));
		}

		this.number = number;
	}

	/**
	 * Returns whether the given {@link String} is a valid {@link CreditCardNumber}.
	 * 
	 * @param number
	 * @return
	 */
	public static boolean isValid(String number) {
		return number == null ? false : number.matches(regex);
	}
}
