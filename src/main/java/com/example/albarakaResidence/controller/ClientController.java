package com.example.albarakaResidence.controller;

import com.example.albarakaResidence.entity.ClientEntity;
import com.example.albarakaResidence.services.ClientService;
import com.example.albarakaResidence.services.PdfService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import com.itextpdf.text.DocumentException;

@Controller
public class ClientController {

    @Autowired
    private ClientService service;

    @Autowired
    private PdfService pdfService;

    // Page d'accueil avec calcul des totaux
    @GetMapping("/")
    public String viewHomePage(Model model) {
        List<ClientEntity> listClients = service.listAll();

        // Calcul des totaux
        BigDecimal totalPriceHt = listClients.stream()
                .map(ClientEntity::getPriceHt)
                .filter(priceHt -> priceHt != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalTva = listClients.stream()
                .map(ClientEntity::getTva)
                .filter(tva -> tva != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalPriceTtc = listClients.stream()
                .map(ClientEntity::getPrice)
                .filter(price -> price != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Ajouter les données au modèle
        model.addAttribute("listClients", listClients);
        model.addAttribute("totalPriceHt", totalPriceHt);
        model.addAttribute("totalTva", totalTva);
        model.addAttribute("totalPriceTtc", totalPriceTtc);

        return "index";
    }

    @GetMapping("/new")
    public String addClientForm(Model model) {
        model.addAttribute("client", new ClientEntity());
        return "new";
    }

    @PostMapping("/save")
    public String saveClient(@ModelAttribute("client") @Valid ClientEntity clientEntity,
                             BindingResult result,
                             @RequestParam("reservationStartDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                             @RequestParam("reservationEndDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                             @RequestParam(value = "apartments", required = false) List<String> apartments,
                             @RequestParam("phoneNumber") String phoneNumber,
                             @RequestParam("issueDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate issueDate) {
        if (result.hasErrors()) {
            return "new";
        }

        // Mise à jour des attributs de l'entité
        clientEntity.setReservationStartDate(startDate);
        clientEntity.setReservationEndDate(endDate);
        clientEntity.setIssueDate(issueDate);

        String designation = (apartments != null && !apartments.isEmpty())
                ? "Appartements: " + String.join(", ", apartments)
                : "Aucun appartement sélectionné";
        clientEntity.setDesignation(designation);
        clientEntity.setPhoneNumber(phoneNumber);

        service.save(clientEntity);
        return "redirect:/";
    }

    @GetMapping("/edit/{id}")
    public ModelAndView showEditClientPage(@PathVariable(name = "id") Long id) {
        ModelAndView mav = new ModelAndView("new");
        ClientEntity clientEntity = service.get(id);
        if (clientEntity != null) {
            mav.addObject("client", clientEntity);
        } else {
            mav.setViewName("redirect:/error");
        }
        return mav;
    }

    @GetMapping("/delete/{id}")
    public String deleteClient(@PathVariable(name = "id") Long id) {
        ClientEntity client = service.get(id);
        if (client != null) {
            service.delete(id);
        } else {
            return "redirect:/error";
        }
        return "redirect:/";
    }

    @GetMapping("/facture/{id}")
    public String showFacture(@PathVariable(name = "id") Long id, Model model) {
        ClientEntity client = service.get(id);
        if (client == null) {
            model.addAttribute("errorMessage", "Client introuvable.");
            return "error";
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String startDateFormatted = client.getReservationStartDate() != null
                ? client.getReservationStartDate().format(formatter) : "N/A";
        String endDateFormatted = client.getReservationEndDate() != null
                ? client.getReservationEndDate().format(formatter) : "N/A";
        String issueDateFormatted = client.getIssueDate() != null
                ? client.getIssueDate().format(formatter) : "N/A";

        String priceHtFormatted = formatBigDecimal(client.getPriceHt());
        String tvaFormatted = formatBigDecimal(client.getTva());
        String priceFormatted = formatBigDecimal(client.getPrice());

        model.addAttribute("client", client);
        model.addAttribute("startDateFormatted", startDateFormatted);
        model.addAttribute("endDateFormatted", endDateFormatted);
        model.addAttribute("issueDateFormatted", issueDateFormatted);
        model.addAttribute("priceHtFormatted", priceHtFormatted);
        model.addAttribute("tvaFormatted", tvaFormatted);
        model.addAttribute("priceFormatted", priceFormatted);

        return "facture";
    }

    @GetMapping("/facture/pdf/{id}")
    public void downloadFacturePdf(@PathVariable Long id, HttpServletResponse response) {
        try {
            ClientEntity client = service.get(id);
            if (client == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Client introuvable.");
                return;
            }

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=facture_" + client.getId() + ".pdf");

            pdfService.generateFacturePdf(client, response.getOutputStream());

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
            try {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erreur lors de la génération du PDF.");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    private String formatBigDecimal(BigDecimal value) {
        return value != null ? String.format("%.2f", value) : "0.00";
    }

    @GetMapping("/error")
    public String showErrorPage(Model model) {
        model.addAttribute("errorMessage", "Une erreur est survenue.");
        return "error";
    }
}
