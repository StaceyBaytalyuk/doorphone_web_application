package com.nuos.stakeyka.doorphone.repos;

import com.nuos.stakeyka.doorphone.domain.Address;
import com.nuos.stakeyka.doorphone.domain.Client;
import com.nuos.stakeyka.doorphone.domain.Master;
import com.nuos.stakeyka.doorphone.domain.Tariff;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface ClientRepo extends CrudRepository<Client, Integer> {
    Iterable<Client> findAllByName(String name);
    Iterable<Client> findAllByPhone(String phone);
    Iterable<Client> findAllByTariff(Tariff tariff);
    Iterable<Client> findAllByAddressAndStatus(Address address, Boolean status);

//    @Query(value = "SELECT id FROM Client where balance<0")
//    Iterable<Client> findAllByAddress(Address address);
    Iterable<Client> findAllByAddressAndBalanceLessThan(Address address, Integer balance);
    Iterable<Client> findAllByBalanceLessThan(Integer balance);

    Client findByAddressAndApartment(Address address, Integer apartment);
    Boolean existsByAddressAndApartment(Address address, Integer apartment);
    Boolean existsByStatusAndMaster(Boolean status, Master master);

    @Query(value = "SELECT sum(balance) FROM Client where balance<0")
    Iterable<Client> findAllByAddress(Address address);

}
