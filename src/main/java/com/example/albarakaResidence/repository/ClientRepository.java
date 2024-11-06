package com.example.albarakaResidence.repository;


import com.example.albarakaResidence.entity.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ClientRepository extends JpaRepository <ClientEntity, Long>{
    @Query("SELECT c FROM ClientEntity c WHERE c.deleted = false")
    List<ClientEntity> findAllActiveClients();

    @Query("SELECT c FROM ClientEntity c WHERE c.deleted = false ORDER BY c.reservationStartDate ASC")
    List<ClientEntity> findAllByOrderByReservationStartDateAsc();



}
