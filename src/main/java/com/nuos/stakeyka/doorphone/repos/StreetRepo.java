package com.nuos.stakeyka.doorphone.repos;

import com.nuos.stakeyka.doorphone.domain.Street;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface StreetRepo extends CrudRepository<Street, Integer> {
    Street findByName(@Param("Name") String name);
    Boolean existsByName(@Param("Name") String name);

}
