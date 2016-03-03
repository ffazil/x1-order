package com.tracebucket.x1.order.sale.jpa.repository;

import com.tracebucket.x1.order.base.jpa.repository.BaseRepository;
import com.tracebucket.x1.order.sale.domain.SaleOrder;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Definition of repository methods for {@link SaleOrder}.
 *
 * @author ffazil
 * @since 19/01/16
 */
@RepositoryRestResource(collectionResourceRel = "saleOrders", path = "saleOrders")
public interface SaleOrderRepository extends BaseRepository<SaleOrder> {

}
