package com.tracebucket.x1.order.sale.service;

import com.tracebucket.x1.order.sale.domain.SaleOrder;
import com.tracebucket.x1.order.sale.domain.SaleOrderPaidEvent;
import com.tracebucket.x1.order.sale.jpa.repository.SaleOrderRepository;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * Simulating order processing, since order work is not implemented at the moment.
 *
 * @author ffazil
 * @since 05/02/16
 */
@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired) )
public class SaleOrderWorkSimulation {

    @NonNull
    private SaleOrderRepository saleOrderRepository;

    private final Set<SaleOrder> saleOrdersInProgress = Collections.newSetFromMap(new ConcurrentHashMap<SaleOrder, Boolean>());

    @Async
    @TransactionalEventListener
    public void prepareSalesOrder(SaleOrderPaidEvent saleOrderPaidEvent){
        SaleOrder saleOrder = (SaleOrder) saleOrderRepository.findById(saleOrderPaidEvent.getSaleOrderId());
        saleOrder.markInPreparation();
        saleOrder = saleOrderRepository.save(saleOrder);

        saleOrdersInProgress.add(saleOrder);
        log.info("Preparing order {}", saleOrder);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        saleOrder.markProcessed();
        saleOrderRepository.save(saleOrder);

        saleOrdersInProgress.remove(saleOrder);
        log.info("Finished preparing order {}", saleOrder);
    }
}
