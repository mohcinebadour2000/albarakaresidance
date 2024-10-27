package com.example.albarakaResidence.services;

import com.example.albarakaResidence.entity.ClientEntity;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

@Service
public class PdfService {

    public void generateFacturePdf(ClientEntity client, OutputStream outputStream) throws DocumentException {
        // Initialisation du document avec marges
        Document document = new Document(PageSize.A4, 36, 36, 54, 54);
        PdfWriter writer = PdfWriter.getInstance(document, outputStream);

        // Ajout de l'en-tête et du pied de page
        writer.setPageEvent(new HeaderFooter());

        document.open();

        // Ajouter un espace de 4 cm après l'en-tête
        document.add(new Paragraph("\n\n\n\n\n"));

        // Titre de la facture
        Paragraph title = new Paragraph("Facture", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20));
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(85f);  // Ajout de 3 cm après le titre
        document.add(title);

        // Informations sur le client et la facture
        PdfPTable infoTable = createInfoTable(client);
        infoTable.setSpacingAfter(15f);  // Espacement ajusté après le tableau des infos
        document.add(infoTable);

        // Tableau des détails de la facture
        PdfPTable detailTable = createDetailTable(client);
        detailTable.setSpacingBefore(10f); // Espacement ajusté avant le tableau
        detailTable.setSpacingAfter(15f);  // Espacement ajusté après le tableau des détails
        document.add(detailTable);

        // Totaux de la facture
        Paragraph totalTTC = new Paragraph("Total TTC: " + String.format("%.2f", client.getPrice()) + " MAD",
                FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, BaseColor.RED));
        totalTTC.setAlignment(Element.ALIGN_RIGHT);
        totalTTC.setSpacingBefore(10f); // Espacement ajusté avant le total TTC
        document.add(totalTTC);

        // Ajouter un espace flexible pour pousser le message de remerciement vers le bas
        document.add(new Paragraph("\n\n\n"));

        // Message de remerciement en bas de la page
        Paragraph thankYou = new Paragraph("Merci pour votre confiance !",
                FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 12));
        thankYou.setAlignment(Element.ALIGN_CENTER);
        document.add(thankYou);

        document.close();
    }

    // En-tête et pied de page personnalisés
    class HeaderFooter extends PdfPageEventHelper {
        public void onEndPage(PdfWriter writer, Document document) {
            // En-tête
            PdfPTable header = new PdfPTable(1);
            try {
                header.setWidths(new int[]{1});
                header.setTotalWidth(523);
                header.setLockedWidth(true);
                header.getDefaultCell().setBorder(Rectangle.BOTTOM);
                header.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                header.addCell(new Phrase(
                        "EL OTMANI IBTISSAM ET CONSORTS - ICE: 003523257000058 - HAY AL MATAR - NADOR",
                        FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.BLACK)));
                header.addCell(new Phrase(
                        "Numéro de téléphone: +212 808 664 594 / +212 708 048 726",
                        FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.BLACK)));

                header.writeSelectedRows(0, -1, 36, 820, writer.getDirectContent());
            } catch (DocumentException de) {
                throw new ExceptionConverter(de);
            }

            // Pied de page
            PdfPTable footer = new PdfPTable(1);
            try {
                footer.setWidths(new int[]{1});
                footer.setTotalWidth(523);
                footer.setLockedWidth(true);
                footer.getDefaultCell().setBorder(Rectangle.TOP);
                footer.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                footer.addCell(new Phrase(
                        "EL OTMANI IBTISSAM ET CONSORTS - ICE: 003523257000058 - HAY AL MATAR - NADOR",
                        FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.BLACK)));
                footer.addCell(new Phrase(
                        "Numéro de téléphone: +212 808 664 594 / +212 708 048 726",
                        FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.BLACK)));

                // Remonter le pied de page à 50 au lieu de 30 pour qu'il soit mieux visible lors de l'impression
                footer.writeSelectedRows(0, -1, 36, 50, writer.getDirectContent());
            } catch (DocumentException de) {
                throw new ExceptionConverter(de);
            }
        }
    }

    private PdfPTable createInfoTable(ClientEntity client) throws DocumentException {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{1, 1});
        table.getDefaultCell().setBorder(Rectangle.NO_BORDER);

        // Colonne client
        PdfPCell cellClient = new PdfPCell();
        cellClient.setBorder(Rectangle.NO_BORDER);

        // Informations client avec espace de 1 cm entre chaque élément
        Paragraph clientInfo = new Paragraph();
        clientInfo.add(new Paragraph("Numéro de facture: " + client.getId(), FontFactory.getFont(FontFactory.HELVETICA, 12)));
        clientInfo.add(new Paragraph("Nom du client: " + client.getClientName(), FontFactory.getFont(FontFactory.HELVETICA, 12)));
        clientInfo.add(new Paragraph("CIN: " + client.getIdCard(), FontFactory.getFont(FontFactory.HELVETICA, 12)));
        clientInfo.add(new Paragraph("\n")); // Espace de 1 cm

        cellClient.addElement(clientInfo);

        // Colonne entreprise avec email à droite
        PdfPCell cellCompany = new PdfPCell();
        cellCompany.setBorder(Rectangle.NO_BORDER);
        cellCompany.setHorizontalAlignment(Element.ALIGN_RIGHT);
        Paragraph companyInfo = new Paragraph(" ", FontFactory.getFont(FontFactory.HELVETICA, 12));
        cellCompany.addElement(companyInfo);

        table.addCell(cellClient);
        table.addCell(cellCompany);

        return table;
    }

    private PdfPTable createDetailTable(ClientEntity client) throws DocumentException {
        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{3, 2, 2, 2, 2, 2});

        // En-têtes du tableau
        addTableHeader(table);

        // Ajouter les données du client
        addRows(table, client);

        return table;
    }

    private void addTableHeader(PdfPTable table) {
        Stream.of("Description", "Date de début", "Date de fin", "Prix HT (MAD)", "TVA (MAD)", "Prix TTC (MAD)")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell(new Phrase(columnTitle, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
                    header.setHorizontalAlignment(Element.ALIGN_CENTER);
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setPadding(10);
                    table.addCell(header);
                });
    }

    private void addRows(PdfPTable table, ClientEntity client) {
        PdfPCell descriptionCell = new PdfPCell(new Phrase(client.getDesignation(), FontFactory.getFont(FontFactory.HELVETICA, 12)));
        descriptionCell.setPadding(10);
        table.addCell(descriptionCell);

        table.addCell(new Phrase(client.getReservationStartDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), FontFactory.getFont(FontFactory.HELVETICA, 12)));
        table.addCell(new Phrase(client.getReservationEndDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), FontFactory.getFont(FontFactory.HELVETICA, 12)));
        table.addCell(new Phrase(String.format("%.2f", client.getPriceHt()), FontFactory.getFont(FontFactory.HELVETICA, 12)));
        table.addCell(new Phrase(String.format("%.2f", client.getTva()), FontFactory.getFont(FontFactory.HELVETICA, 12)));
        table.addCell(new Phrase(String.format("%.2f", client.getPrice()), FontFactory.getFont(FontFactory.HELVETICA, 12)));
    }
}
