package com.nuos.stakeyka.doorphone.repos;

import com.nuos.stakeyka.doorphone.domain.Material;
import org.springframework.data.repository.CrudRepository;

public interface MaterialRepo extends CrudRepository<Material, Integer> {
    Boolean existsByName(String name);

}
