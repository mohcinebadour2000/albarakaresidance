package com.example.albarakaResidence.services;

import com.example.albarakaResidence.entity.ClientEntity;
import com.example.albarakaResidence.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {
    @Autowired
    private ClientRepository repo;
    public List<ClientEntity> listAll(){
        return repo.findAll();
    }

    public void save (ClientEntity client){
        repo.save(client);
    }

    public ClientEntity get(long id){
        return repo.findById(id).get();
    }

    public void delete(long id){
        repo.deleteById(id);
    }
}

