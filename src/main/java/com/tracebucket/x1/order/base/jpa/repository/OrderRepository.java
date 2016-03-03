package com.tracebucket.x1.order.base.jpa.repository;

import com.tracebucket.x1.order.base.domain.BaseOrder;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Definition of repository methods for {@link BaseOrder}.
 *
 * @author ffazil
 * @since 19/01/16
 */
@RepositoryRestResource(collectionResourceRel = "orders", path = "orders")
public interface OrderRepository extends BaseRepository<BaseOrder> {

}
