package com.example.albarakaResidence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@Entity
public class ClientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String clientName;
    private String idCard;
    private BigDecimal price; // Prix TTC
    private BigDecimal tva;   // Montant de la TVA
    private BigDecimal priceHt; // Prix HT
    private String designation;
    private LocalDate reservationStartDate;
    private LocalDate reservationEndDate;

    private LocalDate issueDate;

    // Getters et Setters
    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    @NotBlank(message = "Le numéro de téléphone est obligatoire")
    @Pattern(regexp = "^\\+?[0-9]*$", message = "Numéro de téléphone invalide")
    private String phoneNumber;

    // Getters et setters
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }




    // Getters et setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public BigDecimal getPrice() {
        return price;
    }

    // Met à jour le prix TTC et ajuste le prix HT et la TVA
    public void setPrice(BigDecimal price) {
        this.price = price;
        if (price != null) {
            this.priceHt = price.divide(BigDecimal.valueOf(1.2), 2, RoundingMode.HALF_UP);
            this.tva = price.subtract(this.priceHt);
        }
    }

    public BigDecimal getTva() {
        return tva;
    }

    // Met à jour la TVA et ajuste le prix TTC et le prix HT
    public void setTva(BigDecimal tva) {
        this.tva = tva;
        if (tva != null && priceHt != null) {
            this.price = this.priceHt.add(tva);
        }
    }

    public BigDecimal getPriceHt() {
        return priceHt;
    }

    // Met à jour le prix HT et ajuste le prix TTC et la TVA
    public void setPriceHt(BigDecimal priceHt) {
        this.priceHt = priceHt;
        if (priceHt != null) {
            this.tva = priceHt.multiply(BigDecimal.valueOf(0.2)).setScale(2, RoundingMode.HALF_UP);
            this.price = priceHt.add(this.tva);
        }
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public LocalDate getReservationStartDate() {
        return reservationStartDate;
    }

    public void setReservationStartDate(LocalDate reservationStartDate) {
        this.reservationStartDate = reservationStartDate;
    }

    public LocalDate getReservationEndDate() {
        return reservationEndDate;
    }

    public void setReservationEndDate(LocalDate reservationEndDate) {
        this.reservationEndDate = reservationEndDate;
    }

    // Constructeurs (facultatif)
    public ClientEntity() {
    }

    public ClientEntity(Long id, String clientName, String idCard, BigDecimal price, BigDecimal tva, BigDecimal priceHt, String designation, LocalDate reservationStartDate, LocalDate reservationEndDate, String phoneNumber) {
        this.id = id;
        this.clientName = clientName;
        this.idCard = idCard;
        this.price = price;
        this.tva = tva;
        this.priceHt = priceHt;
        this.designation = designation;
        this.reservationStartDate = reservationStartDate;
        this.reservationEndDate = reservationEndDate;
        this.phoneNumber = phoneNumber;
    }

    private boolean deleted = false;

    // Getters et Setters

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    /*public ClientEntity(String clientName, String idCard, BigDecimal price, String designation,
                        LocalDate reservationStartDate, LocalDate reservationEndDate) {
        this.clientName = clientName;
        this.idCard = idCard;
        setPrice(price); // Utilise le setter pour ajuster automatiquement la TVA et le prix HT
        this.designation = designation;
        this.reservationStartDate = reservationStartDate;
        this.reservationEndDate = reservationEndDate;
    }*/
}
