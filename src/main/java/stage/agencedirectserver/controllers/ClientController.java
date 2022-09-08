package stage.agencedirectserver.controllers;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import stage.agencedirectserver.entities.Client;
import stage.agencedirectserver.exceptions.EmailNotValidException;
import stage.agencedirectserver.exceptions.NotFoundException;
import stage.agencedirectserver.services.ClientService;
import stage.agencedirectserver.utils.UriUtil;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController @RequiredArgsConstructor @RequestMapping("/api/client") @Slf4j
public class ClientController {
    private final ClientService clientService;

    // get Methods
    @GetMapping("/all")
    public List<Client> getAllClients() { return clientService.getAllClients();  }
    @GetMapping("inactive/all")
    public List<Client> getAllInactiveClients() { return clientService.getAllInactiveClients();  }
    @GetMapping("/email/{email}")
    public Client getClientByEmail(@PathVariable("email") String email) { return clientService.getClientByEmail(email); }
    @GetMapping("/cin/{cin}")
    public Client getClientByCIN(@PathVariable("cin") String cin) { return clientService.getClientByCIN(cin); }
    @GetMapping("/count")
    public Long getClientCount(){ return  clientService.getClientCount(); }
    @GetMapping("/demande")
    public Long getDemandeCount(){ return  clientService.getDemandeCount(); }

    // add Methods
    @PostMapping("/add")
    public ResponseEntity<Client> addClient(@RequestBody Client client) throws MessagingException, NotFoundException, EmailNotValidException {
        return ResponseEntity.created(UriUtil.Uri("/api/client/add")).body(clientService.addClient(client));
    }

    // update Methods
    @PutMapping("/update/{id}")
    public Client updateClient(@PathVariable("id") Long id, @RequestBody Client newClient) throws NotFoundException { return clientService.updateClient(id, newClient); }

    // delete Methods
    @DeleteMapping("/delete/{id}")
    public void deleteClient(@PathVariable("id") Long id) { clientService.deleteClient(id); }

    // other Methods
    @PostMapping("/newAccessCode")
    public void newAccessCode(@RequestBody NewAccessCodeForm form){ clientService.newAccessCode(form.getEmail(),form.getSendMail()); }
    @PostMapping("/myAgence")
    public void addClientToAgence(@RequestBody AddClientToAgenceForm form) throws NotFoundException { clientService.addClientToAgence(form.getClientEmail(), form.getAgenceName()); }
    @PostMapping("/myPack")
    public void addClientToPack(@RequestBody AddClientToPackForm form) throws NotFoundException { clientService.addClientToPack(form.getClientEmail(), form.getPackName()); }

    @PostMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException { clientService.refreshToken(request, response); }


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
    @Data
    public  static  class NewAccessCodeForm{
        private String email;
        private Boolean sendMail;
    }
}
