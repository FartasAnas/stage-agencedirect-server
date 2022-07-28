package stage.agencedirectserver.controllers;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import stage.agencedirectserver.entities.Client;
import stage.agencedirectserver.exceptions.notfound.*;
import stage.agencedirectserver.services.ClientService;

import javax.mail.MessagingException;
import java.net.URI;
import java.util.List;

@RestController @RequiredArgsConstructor @RequestMapping("/api/client") @Slf4j
public class ClientController {
    private final ClientService clientService;

    // get Methods
    @GetMapping("/all")
    public List<Client> getAllClients() { return clientService.getAllClients();  }
    @GetMapping("/email/{email}")
    public Client getClientByEmail(@PathVariable("email") String email) { return clientService.getClientByEmail(email); }
    @GetMapping("/cin/{cin}")
    public Client getClientByCIN(@PathVariable("cin") String cin) { return clientService.getClientByCIN(cin); }

    // add Methods
    @PostMapping("/add")
    public ResponseEntity<Client> addClient(@RequestBody Client client) throws MessagingException, AgenceNotFoundException, PackNotFoundException {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/client/add").toUriString());
        return ResponseEntity.created(uri).body(clientService.addClient(client));
    }

    // update Methods
    @PutMapping("/update/{id}")
    public Client updateClient(@PathVariable("id") Long id, @RequestBody Client newClient) throws ClientNotFound { return clientService.updateClient(id, newClient); }

    // delete Methods
    @DeleteMapping("/delete/{id}")
    public void deleteClient(@PathVariable("id") Long id) { clientService.deleteClient(id); }

    // other Methods
    @PostMapping("/myAgence")
    public void addClientToAgence(@RequestBody AddClientToAgenceForm form) throws ClientOrAgentNotFound {
        clientService.addClientToAgence(form.getClientEmail(), form.getAgenceName());
    }
    @PostMapping("/myPack")
    public void addClientToPack(@RequestBody AddClientToPackForm form) throws ClientOrPackNotFound {
        clientService.addClientToPack(form.getClientEmail(), form.getPackName());
    }
    @Data
    public static class AddClientToAgenceForm {
        private String clientEmail;
        private String agenceName;
    }
    @Data
    public static class AddClientToPackForm {
        private String clientEmail;
            private String packName;
    }





}
