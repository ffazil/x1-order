package com.tracebucket.x1.order.base.jpa.repository;

import com.tracebucket.x1.order.base.domain.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Definition of find and delete by id for {@link BaseEntity}.
 *
 * @author ffazil
 * @since 27/01/16
 */
@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity> extends JpaRepository<T, String> {
    T findById(String id);



    @Override
    @Modifying(clearAutomatically = true)
    @Query("UPDATE #{#entityName} x set x.passive = true where x.id = :id")
    void delete(String id);
}
