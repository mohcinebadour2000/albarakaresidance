package com.example.albarakaResidence.repository;


import com.example.albarakaResidence.entity.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ClientRepository extends JpaRepository <ClientEntity, Long>{

}
