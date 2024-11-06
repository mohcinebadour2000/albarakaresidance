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

    // Méthode pour lister tous les clients non supprimés, triés par date de début de réservation
    public List<ClientEntity> listAllSortedByReservationStartDate() {
        return repo.findAllByOrderByReservationStartDateAsc();
    }

    // Sauvegarde ou mise à jour d'un client
    public void save(ClientEntity client) {
        repo.save(client);
    }

    // Récupère un client par ID
    public ClientEntity get(long id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Client introuvable avec l'ID : " + id));
    }

    // Suppression douce d'un client
    public void delete(long id) {
        ClientEntity client = get(id);
        client.setDeleted(true);
        repo.save(client); // Enregistre l'état mis à jour de l'entité
    }

    // Restaure un client supprimé
    public void restore(long id) {
        ClientEntity client = get(id);
        client.setDeleted(false);
        repo.save(client); // Enregistre l'état restauré de l'entité
    }
}
