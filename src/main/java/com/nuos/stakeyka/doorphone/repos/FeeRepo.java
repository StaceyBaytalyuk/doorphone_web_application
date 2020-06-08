package com.nuos.stakeyka.doorphone.repos;

import com.nuos.stakeyka.doorphone.domain.Client;
import com.nuos.stakeyka.doorphone.domain.Fee;
import org.springframework.data.repository.CrudRepository;

public interface FeeRepo extends CrudRepository<Fee, Integer> {
    Iterable<Fee> findAllByClientAndStatus(Client client, Boolean status);
}
