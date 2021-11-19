package com.nuos.stakeyka.doorphone.util;

import com.nuos.stakeyka.doorphone.domain.Service;

import java.util.ArrayList;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

public class PdfDocumentCreator {
    public static void buildPdfDocument(Iterable<Service> services, Document document) throws Exception {
        //utf-8 not supported
        String[] names = {"ID", "Address", "Client comment", "Master ID", "Date"};
        PdfPTable table = new PdfPTable(names.length);
        table.setWidthPercentage(100);

        ArrayList<PdfPCell> headers = new ArrayList<>();

        for (String name : names) {
            headers.add(new PdfPCell(new Phrase(name)));
        }

        for (PdfPCell header : headers) {
            header.setHorizontalAlignment(Element.ALIGN_LEFT);
        }

        for (PdfPCell header : headers) {
            table.addCell(header);
        }

        for (Service service : services) {
            table.addCell(String.valueOf(service.getId()));
            table.addCell(service.getClient().getAddress().toString());
            table.addCell(service.getCommentClient());
            table.addCell(String.valueOf(service.getMaster().getId()));
            table.addCell(service.getDateCreate().toString());
        }

        document.add(table);
    }
}
