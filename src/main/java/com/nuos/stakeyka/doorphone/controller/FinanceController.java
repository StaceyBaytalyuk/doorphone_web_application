package com.nuos.stakeyka.doorphone.controller;

import com.nuos.stakeyka.doorphone.domain.*;
import com.nuos.stakeyka.doorphone.repos.*;
import com.nuos.stakeyka.doorphone.util.Util;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.Map;

@Controller
public class FinanceController {
    private final TariffRepo tariffRepo;
    private final PaymentRepo paymentRepo;
    private final FeeRepo feeRepo;
    private final ClientRepo clientRepo;
    private final ConnectionRepo connectionRepo;

    public FinanceController(TariffRepo tariffRepo, FeeRepo feeRepo, ClientRepo clientRepo, PaymentRepo paymentRepo, ConnectionRepo connectionRepo) {
        this.tariffRepo = tariffRepo;
        this.feeRepo = feeRepo;
        this.clientRepo = clientRepo;
        this.paymentRepo = paymentRepo;
        this.connectionRepo = connectionRepo;
    }

    //////////////////////////////////////////////////////////////////////////
    // TARIFF
    //////////////////////////////////////////////////////////////////////////
    @GetMapping("tariffs")
    public String tariffs(Map<String, Object> model) {
        Iterable<Tariff> all = tariffRepo.findAll();
        model.put("tariffs", all);
        return "tariffs";
    }

    @PostMapping("addTariff")
    public String addTariff(@RequestParam Integer price, @RequestParam String name, Map<String, Object> model) {
        if ( price!=null && price>0 && !name.isEmpty() ) {
            Tariff tariff = new Tariff(name, price);
            tariffRepo.save(tariff);
        }
        return tariffs(model);
    }

    @PostMapping("editTariff")
    public String editTariff(@RequestParam Integer id, @RequestParam Integer price, Map<String, Object> model) {
        if ( id!=null && tariffRepo.existsById(id) && price!=null && price>0 ) {
            Tariff oldTariff = tariffRepo.findById(id).orElse(new Tariff());
            Tariff newTariff = new Tariff(oldTariff.getName(), price);
            tariffRepo.save(oldTariff);
            Iterable<Client> clients = clientRepo.findAllByTariff(oldTariff); // перевести всех на новый
            for (Client client : clients) {
                client.setTariff(newTariff);
                clientRepo.save(client);
            }
        }
        return tariffs(model);
    }


    //////////////////////////////////////////////////////////////////////////
    // FEE
    //////////////////////////////////////////////////////////////////////////
    @GetMapping("fees")
    public String fees(Map<String, Object> model) {
        Iterable<Fee> all = feeRepo.findAll();
        model.put("fees", all);
        return "fees";
    }

    @PostMapping("addFee")
    public String addFee(Map<String, Object> model) {
        Iterable<Client> allClients = clientRepo.findAll();
        for (Client client : allClients) {
            Fee fee = new Fee(client, LocalDate.now());
            if ( client.getBalance() >= client.getTariff().getPrice() ) {
                fee.setStatus(true);
            }
            client.updateBalance(-client.getTariff().getPrice());
            feeRepo.save(fee);
            clientRepo.save(client);
        }
        disconnectDebtors(allClients); // если задолженность 4 месяца - заявка на отключение
        return fees(model);
    }

    private void disconnectDebtors(Iterable<Client> clients) {
        for (Client client : clients) {
            if ( client.getBalance() <= (client.getTariff().getPrice() * -4) ) {
                Connection disconnect = new Connection(client, false);
                connectionRepo.save(disconnect);
            }
        }
    }

    //////////////////////////////////////////////////////////////////////////
    // PAYMENT
    //////////////////////////////////////////////////////////////////////////
    @GetMapping("payments")
    public String payments(Map<String, Object> model) {
        Iterable<Payment> all = paymentRepo.findAll();
        model.put("payments", all);
        return "payments";
    }

    @PostMapping("addPayment")
    public String addPayment(@RequestParam Integer clientID, @RequestParam Integer sum,
                             @RequestParam String methodString, @RequestParam String dateString, Map<String, Object> model) {
        if ( clientID!=null && clientRepo.existsById(clientID) && sum!=null && sum>0 ) {
            Client client = clientRepo.findById(clientID).orElse(new Client());
            boolean method = methodString.equals("касса");
            LocalDate date = Util.parseDateOrNow(dateString);
            Payment payment = new Payment(client, sum, method, date);
            paymentRepo.save(payment);
            client.updateBalance(sum); // зачислили
            clientRepo.save(client);

            int money = client.getBalance(); // пытаемся снять деньги если есть долги
            Iterable<Fee> unpaidFees = feeRepo.findAllByClientAndStatus(client, false);
            for (Fee fee : unpaidFees) {
                if ( money >= fee.getTariff().getPrice() ) {
                    fee.setStatus(true);
                    money -= fee.getTariff().getPrice();
                    feeRepo.save(fee);
                }
            }
        } else {
            model.put("message", "Данные введены неверно");
        }
        return payments(model);
    }
}
