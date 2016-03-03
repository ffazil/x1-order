package com.tracebucket.x1.order.sale;

import com.tracebucket.x1.order.sale.domain.SaleOrder;
import com.tracebucket.x1.order.sale.domain.SaleOrderStatus;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * @author ffazil
 * @since 04/03/16
 */
public class TestUtils {

    public static SaleOrder createExistingOrder() {

        SaleOrder order = new SaleOrder();
        ReflectionTestUtils.setField(order, "id", "");
        return order;
    }

    public static SaleOrder createExistingOrderWithStatus(SaleOrderStatus status) {

        SaleOrder order = createExistingOrder();
        ReflectionTestUtils.setField(order, "status", status);
        return order;
    }
}
