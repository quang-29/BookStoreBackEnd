package org.example.bookstore.repository;

import org.example.bookstore.model.DeliveryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DeliveryRepository extends JpaRepository<DeliveryType, Integer> {


    @Query("select dt from DeliveryType dt where dt.deliveryName = ?1")
    DeliveryType findByName(String deliveryMethod);
}
