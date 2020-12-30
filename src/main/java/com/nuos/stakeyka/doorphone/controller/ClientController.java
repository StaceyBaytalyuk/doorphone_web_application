package com.nuos.stakeyka.doorphone.controller;

import com.nuos.stakeyka.doorphone.domain.*;
import com.nuos.stakeyka.doorphone.repos.*;
import com.nuos.stakeyka.doorphone.util.Util;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.time.LocalDate;
import java.util.Map;

@Controller
public class ClientController {
    private final ClientRepo clientRepo;
    private final MasterRepo masterRepo;
    private final AddressRepo addressRepo;
    private final StreetRepo streetRepo;
    private final TariffRepo tariffRepo;

    public ClientController(ClientRepo clientRepo, MasterRepo masterRepo, AddressRepo addressRepo,StreetRepo streetRepo, TariffRepo tariffRepo) {
        this.clientRepo = clientRepo;
        this.masterRepo = masterRepo;
        this.addressRepo = addressRepo;
        this.streetRepo = streetRepo;
        this.tariffRepo = tariffRepo;
    }

    //////////////////////////////////////////////////////////////////////////
    // ADDRESS
    //////////////////////////////////////////////////////////////////////////
    @GetMapping("address")
    public String address(Map<String, Object> model) {
        Iterable<Address> addresses = addressRepo.findAll();
        model.put("address", addresses);
        return "address";
    }

    @PostMapping("editStreet")
    public String editStreet(@RequestParam Integer id, @RequestParam String name, Map<String, Object> model) {
        if ( id!=null && streetRepo.existsById(id) && !name.isEmpty() ) {
            Street street = streetRepo.findById(id).orElse(new Street());
            street.setName(name);
            streetRepo.save(street);
        }
        return address(model);
    }

    //////////////////////////////////////////////////////////////////////////
    // CLIENT
    //////////////////////////////////////////////////////////////////////////
    @GetMapping("clients")
    public String clients(Map<String, Object> model) {
        Iterable<Client> clients = clientRepo.findAll();
        model.put("clients", clients);
        return "clients";
    }

    @GetMapping("addClient")
    public String addClient() {
        return "client_add";
    }

    @GetMapping("searchClients")
    public String searchClients() {
        return "search_clients";
    }

    @PostMapping("searchClientById")
    public String searchClientById(@RequestParam Integer id, Map<String, Object> model) {
        if ( id!=null && clientRepo.existsById(id) ) {
            Client client = clientRepo.findById(id).orElse(new Client());
            model.put("clients", client);
        } else {
            model.put("message", "Нет клиента с таким id");
        }
        return searchClients();
    }

    @PostMapping("searchClientsByName")
    public String searchClientsByName(@RequestParam String name, Map<String, Object> model) {
        if ( !name.isEmpty() ) {
            Iterable<Client> clients = clientRepo.findAllByName(name);
            if ( clients.iterator().hasNext() ) {
                model.put("clients", clients);
            } else {
                model.put("message", "Не найдено");
            }
        } else {
            model.put("message", "Пустое имя");
        }
        return searchClients();
    }

    @PostMapping("searchClientsByPhone")
    public String searchClientsByPhone(@RequestParam String phone, Map<String, Object> model) {
        if ( !phone.isEmpty() ) {
            Iterable<Client> clients = clientRepo.findAllByPhone(phone);
            if ( clients.iterator().hasNext() ) {
                model.put("clients", clients);
            } else {
                model.put("message", "Не найдено");
            }
        } else {
            model.put("message", "Пустой номер телефона");
        }
        return searchClients();
    }

    @PostMapping("searchClientsByAddress")
    public String searchClientsByAddress(@RequestParam String streetName, @RequestParam Integer house, @RequestParam Integer entrance, @RequestParam Integer apartment, Map<String, Object> model) {
        if ( !streetName.isEmpty() && house!=null && entrance!=null ) {
            if ( addressRepo.existsByStreetNameAndHouseAndEntrance(streetName, house, entrance) ) {
                Address address = addressRepo.findByStreetNameAndHouseAndEntrance(streetName, house, entrance);
                if ( apartment==null ) { // найти всех клиентов в подъезде
                    Iterable<Client> clients = clientRepo.findAllByAddressAndStatus(address, true);
                    model.put("clients", clients);
                } else if ( clientRepo.existsByAddressAndApartment(address, apartment) ) { // конкретная кв.
                    Client client = clientRepo.findByAddressAndApartment(address, apartment);
                    model.put("clients", client);
                }
            } else {
                model.put("message", "Адрес не обслуживается");
            }
        } else {
            model.put("message", "Введите все поля (кв. не обязательно)");
        }
        return searchClients();
    }

    @PostMapping("debtors")
    public String debtors(@RequestParam Integer addressID, Map<String, Object> model) {
        if ( addressID==null ) { // все должники

            Iterable<Client> clients = clientRepo.findAllByBalanceLessThan(0);
            if ( clients.iterator().hasNext() ) {
                model.put("clients", clients);
            } else {
                model.put("message", "Нет должников");
            }

        } else if ( addressRepo.existsById(addressID) ) { // только в конкретном подъезде

            Address address = addressRepo.findById(addressID).orElse(new Address());
            Iterable<Client> clients = clientRepo.findAllByAddressAndBalanceLessThan(address, 0);
            if ( clients.iterator().hasNext() ) {
                model.put("clients", clients);
//                Integer sum = clientRepo.
//                model.put("message", "Общая сумма долга по всем клиентам: "+);
            } else {
                model.put("message", "Нет должников");
            }
        } else {
            model.put("message", "Адрес не обслуживается");
        }
        return searchClients();
    }

    @GetMapping("infoClient")
    public String infoClient(@RequestParam Integer id, Map<String, Object> model) {
        if ( id!=null && clientRepo.existsById(id) ) {
            Client client = clientRepo.findById(id).orElse(new Client());
            model.put("client", client);
            return "client_info";
        } return clients(model);
    }

    @GetMapping("registerClient")
    public String registerClient(@RequestParam String name, @RequestParam String phone, @RequestParam String deviceType,
                                 @RequestParam String streetName, @RequestParam Integer house, @RequestParam Integer entrance,
                                 @RequestParam Integer apartment, @RequestParam Integer masterID, @RequestParam Integer tariffID,
                                 @RequestParam String contractDate, Map<String, Object> model) {
        if ( tariffID!=null && !name.isEmpty() && !streetName.isEmpty() && house!=null && entrance!=null && apartment!=null ) {
            if ( tariffRepo.existsById(tariffID) ) {
                if ( checkAddress(0, streetName, house, entrance, apartment) ) {
                    Tariff tariff = tariffRepo.findById(tariffID).orElse(new Tariff());
                    Address address = addressRepo.findByStreetNameAndHouseAndEntrance(streetName, house, entrance);
                    LocalDate date = Util.parseDateOrNow(contractDate);

                    Master master;
                    if ( masterID!=null && masterRepo.existsById(masterID) ) {
                        master = masterRepo.findById(masterID).orElse(new Master());
                    } else { // не выбран или неправильный id
                        master = masterRepo.findAll().iterator().next(); // первый попавшийся
                    }

                    if ( !phone.isEmpty() && phone.length()>10 ) {
                        phone = phone.substring(0, 10);
                    }

                    Client.ClientBuilder clientBuilder = new Client.ClientBuilder().setMaster(master).setName(name)
                            .setApartment(apartment).setAddress(address).setDeviceType(deviceType).setPhone(phone)
                            .setTariff(tariff).setContractDate(date);
                    Client client = clientBuilder.build();
                    clientRepo.save(client);
                    model.put("message", "Клиент "+name+" успешно зарегистрирован");
                } else {
                    model.put("message", "Эта квартира уже занята");
                }
            } else {
                model.put("message", "Нет такого тарифа");
            }
        } else {
            model.put("message", "Невозможно зарегистрировать клиента. Введите все поля.");
        }
        return clients(model);
    }

    @PostMapping("editClient")
    public String editClient(@RequestParam Integer clientID, @RequestParam String name, @RequestParam String phone,
                             @RequestParam String deviceType, @RequestParam String streetName, @RequestParam Integer house,
                             @RequestParam Integer entrance, @RequestParam Integer apartment,
                             @RequestParam Integer masterID, @RequestParam Integer tariffID,
                             @RequestParam String contractDate, @RequestParam String statusString, Map<String, Object> model) {
        Client client = clientRepo.findById(clientID).orElse(new Client());
        boolean status = statusString.equals("true");

        LocalDate date = Util.parseDate(contractDate);
        if ( date != null ) {
            client.setContractDate(date);
        }

        if ( tariffID!=null && tariffRepo.existsById(tariffID) ) {
            Tariff tariff = tariffRepo.findById(tariffID).orElse(new Tariff());
            client.setTariff(tariff);
        }

        if ( masterID != null && masterRepo.existsById(masterID) ) {
            Master master = masterRepo.findById(masterID).orElse(new Master());
            client.setMaster(master);
        }

        if ( checkAddress(clientID, streetName, house, entrance, apartment) ) {
            Address address = addressRepo.findByStreetNameAndHouseAndEntrance(streetName, house, entrance);
            client.setAddress(address);
            client.setApartment(apartment);
        }

        if ( !phone.isEmpty() && phone.length()>10 ) {
            phone = phone.substring(0, 10);
        }

        if ( !name.isEmpty() ) {
            client.setName(name);
        }

        client.setPhone(phone);client.setStatus(status);client.setDeviceType(deviceType);
        clientRepo.save(client);
        return clients(model);
    }

    // кв. занята - false, свободна - true. созд адрес если такого нет
    private boolean checkAddress(Integer clientID, String streetName, Integer house, Integer entrance, Integer apartment) {
        Street street = streetRepo.findByName(streetName);
        Address address = addressRepo.findByStreetNameAndHouseAndEntrance(streetName, house, entrance); // вдруг упадёт от нул улица
        if (street != null && address != null) {
            Client c = clientRepo.findByAddressAndApartment(address, apartment);
            if ( c!=null ) { // есть клиент в этой кв.
                return c.getId().equals(clientID); // если это один и тот же человек - true
            }
        } else {
            if (street == null) {
                street = new Street(streetName);
                streetRepo.save(street);
            }
            if (address == null) {
                address = new Address(street, house, entrance);
                addressRepo.save(address);
            }
        }
        return true;
    }

}