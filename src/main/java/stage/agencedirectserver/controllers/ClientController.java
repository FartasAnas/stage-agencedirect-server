package stage.agencedirectserver.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import stage.agencedirectserver.entities.Agent;
import stage.agencedirectserver.entities.Client;
import stage.agencedirectserver.exceptions.NotFoundException;
import stage.agencedirectserver.services.ClientService;
import stage.agencedirectserver.utils.ClientGenerateTokenUtil;
import stage.agencedirectserver.utils.GenerateTokenUtil;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

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
    public ResponseEntity<Client> addClient(@RequestBody Client client) throws MessagingException, NotFoundException {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/client/add").toUriString());
        return ResponseEntity.created(uri).body(clientService.addClient(client));
    }

    // update Methods
    @PutMapping("/update/{id}")
    public Client updateClient(@PathVariable("id") Long id, @RequestBody Client newClient) throws NotFoundException { return clientService.updateClient(id, newClient); }

    // delete Methods
    @DeleteMapping("/delete/{id}")
    public void deleteClient(@PathVariable("id") Long id) { clientService.deleteClient(id); }

    // other Methods
    @PostMapping("/myAgence")
    public void addClientToAgence(@RequestBody AddClientToAgenceForm form) throws NotFoundException {
        clientService.addClientToAgence(form.getClientEmail(), form.getAgenceName());
    }
    @PostMapping("/myPack")
    public void addClientToPack(@RequestBody AddClientToPackForm form) throws NotFoundException {
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

    @PostMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String refreshToken = authorizationHeader.substring("Bearer ".length());
            Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(refreshToken);
            String email = decodedJWT.getSubject();
            Client client = clientService.getClientByEmail(email);
            ClientGenerateTokenUtil.generateClientToken(request, response, client, refreshToken,algorithm);
        }else {
            throw new RuntimeException("Refresh token is missing");
        }
    }





}
