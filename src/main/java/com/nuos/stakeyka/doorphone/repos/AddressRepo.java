package com.nuos.stakeyka.doorphone.repos;

import com.nuos.stakeyka.doorphone.domain.Address;
import com.nuos.stakeyka.doorphone.domain.Street;
import org.springframework.data.repository.CrudRepository;

public interface AddressRepo extends CrudRepository<Address, Integer> {

    Address findByStreetIdAndHouseAndEntrance(Integer id, Integer house, Integer entrance);
    Boolean existsByStreetNameAndHouseAndEntrance(String name, Integer house, Integer entrance);
    Address findByStreetNameAndHouseAndEntrance(String name, Integer house, Integer entrance);
}
