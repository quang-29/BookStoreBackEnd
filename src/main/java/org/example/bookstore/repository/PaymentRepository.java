package org.example.bookstore.repository;

import org.example.bookstore.model.PaymentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentType,Integer> {

    @Query("select pt from PaymentType pt where pt.paymentMethod = :paymentMethod")
    PaymentType findByPaymentMethod(String paymentMethod);
}
