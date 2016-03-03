package com.tracebucket.x1.order.sale.service;

import com.tracebucket.x1.order.sale.domain.CreditCard;
import com.tracebucket.x1.order.sale.domain.CreditCardNumber;
import com.tracebucket.x1.order.sale.jpa.repository.CreditCardRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.time.Year;

/**
 * Sample data to be loaded in case the database is empty. Executed only for dev profile.
 *
 * @author ffazil
 * @since 05/02/16
 */
@Slf4j
@Service
public class CreditCardSampleDataLoader {

    @Autowired
    public CreditCardSampleDataLoader(CreditCardRepository creditCardRepository){
        if(creditCardRepository.count() > 0)
            return;

        CreditCardNumber creditCardNumber = new CreditCardNumber("1234123412341234");
        CreditCard creditCard = new CreditCard(creditCardNumber, "Algernon Moncheff", Month.DECEMBER, Year.of(2030));
        creditCard = creditCardRepository.save(creditCard);
        log.info("New sample credit card {} added", creditCard);
    }
}
