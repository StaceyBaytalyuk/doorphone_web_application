package com.nuos.stakeyka.doorphone.controller;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;
import com.nuos.stakeyka.doorphone.domain.Client;
import com.nuos.stakeyka.doorphone.domain.Connection;
import com.nuos.stakeyka.doorphone.domain.Service;
import com.nuos.stakeyka.doorphone.repos.ClientRepo;
import com.nuos.stakeyka.doorphone.repos.ConnectionRepo;
import com.nuos.stakeyka.doorphone.repos.ServiceRepo;
import com.nuos.stakeyka.doorphone.util.PdfDocumentCreator;
import com.nuos.stakeyka.doorphone.util.Util;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Map;

@Controller
public class RequestController {
    private final ClientRepo clientRepo;
    private final ServiceRepo serviceRepo;
    private final ConnectionRepo connectionRepo;

    public RequestController(ClientRepo clientRepo, ServiceRepo serviceRepo, ConnectionRepo connectionRepo) {
        this.clientRepo = clientRepo;
        this.serviceRepo = serviceRepo;
        this.connectionRepo = connectionRepo;
    }

    //////////////////////////////////////////////////////////////////////////
    // service
    //////////////////////////////////////////////////////////////////////////
    @GetMapping("services")
    public String services(Map<String, Object> model) {
        Iterable<Service> services = serviceRepo.findAll();
        model.put("services", services);
        return "services";
    }

    @PostMapping("addService")
    public String addService(@RequestParam Integer clientID, @RequestParam String commentClient, Map<String, Object> model) {
        if ( clientID!=null && clientRepo.existsById(clientID) ) {
            Client client = clientRepo.findById(clientID).orElse(new Client());
            if ( commentClient.isEmpty() ) {
                commentClient = "-";
            }
            Service service = new Service(client, commentClient);
            serviceRepo.save(service);
            model.put("message", "Заявка "+service.getId()+" добавлена успешно");
        } else {
            model.put("message", "Нет такого клиента");
        }
        return services(model);
    }

    @GetMapping("infoService")
    public String infoService(@RequestParam Integer id, Map<String, Object> model) {
        if ( id!=null && serviceRepo.existsById(id) ) {
            Service service = serviceRepo.findById(id).orElse(new Service());
            model.put("service", service);
            return "service_info";
        } return services(model);
    }

    @PostMapping("editService")
    public String editService(@RequestParam Integer id, @RequestParam String status,
                             @RequestParam String commentClient, @RequestParam String commentMaster,
                             @RequestParam String dateComplete, Map<String, Object> model) {
        if ( id!=null && serviceRepo.existsById(id) ) {
            Service service = serviceRepo.findById(id).orElse(new Service());
            LocalDate date = Util.parseDate(dateComplete);
            if ( date != null ) {
                service.setDateComplete(date);
            }
            service.setCommentClient(commentClient);
            service.setCommentMaster(commentMaster);
            service.setStatus(status);
            serviceRepo.save(service);
        }
        return services(model);
    }


    //////////////////////////////////////////////////////////////////////////
    // CONNECTION
    //////////////////////////////////////////////////////////////////////////
    @GetMapping("connections")
    public String connections(Map<String, Object> model) {
        Iterable<Connection> connections = connectionRepo.findAll();
        model.put("connections", connections);
        return "connections";
    }

    @PostMapping("addConnection")
    public String addConnection(@RequestParam Integer clientID, @RequestParam String typeString, Map<String, Object> model) {
        if ( clientID!=null && clientRepo.existsById(clientID) ) {
            boolean type = typeString.equals("подкл");
            Client client = clientRepo.findById(clientID).orElse(new Client());
            Connection connection = new Connection(client, type);
            connectionRepo.save(connection);
            model.put("message", "Заявка "+connection.getId()+" добавлена успешно");
        } else {
            model.put("message", "Нет такого клиента");
        }
        return connections(model);
    }

    @GetMapping("infoConnection")
    public String infoConnection(@RequestParam Integer id, Map<String, Object> model) {
        if ( id!=null && connectionRepo.existsById(id) ) {
            Connection connection = connectionRepo.findById(id).orElse(new Connection());
            model.put("connection", connection);
            return "connection_info";
        }
        return connections(model);
    }

    @PostMapping("editConnection")
    public String editConnection(@RequestParam Integer id, @RequestParam Boolean type,
                                 @RequestParam String status, @RequestParam String dateComplete, Map<String, Object> model) {
        if ( id!=null && connectionRepo.existsById(id) ) {
            Connection connection = connectionRepo.findById(id).orElse(new Connection());

            LocalDate date = Util.parseDate(dateComplete);
            if ( date!=null ) {
                connection.setDateComplete(date);
            }

            Client client = connection.getClient();
            if ( status.equals("выполнено") && client.getStatus()!=type ) {
                client.setStatus(type); // клиент подключен, если заявка на подкл, отлключён если на откл
                clientRepo.save(client);
            }

            connection.setStatus(status);connection.setDateComplete(date);
            connectionRepo.save(connection);
        }
        return connections(model);
    }

    @PostMapping("pdfServices")
    public void pdfServices(HttpServletResponse response){
        try {
            File pdfFile = new File("заявки"+".pdf");
            writePdf(pdfFile);
            InputStream is = new FileInputStream(pdfFile);
            org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());
            response.flushBuffer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writePdf(File pdfFile) throws Exception {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
        document.open();
        PdfDocumentCreator.buildPdfDocument(serviceRepo.findAll(), document);
        document.close();
    }

}