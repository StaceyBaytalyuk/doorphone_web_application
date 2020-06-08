package com.nuos.stakeyka.doorphone.repos;

import com.nuos.stakeyka.doorphone.domain.Payment;
import org.springframework.data.repository.CrudRepository;

public interface PaymentRepo extends CrudRepository<Payment, Integer> {

}
