package stage.agencedirectserver.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import stage.agencedirectserver.entities.Agence;
import stage.agencedirectserver.entities.Agent;
import stage.agencedirectserver.entities.Role;
import stage.agencedirectserver.services.AgenceService;
import stage.agencedirectserver.services.AgentService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;


import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController @RequiredArgsConstructor @RequestMapping("/api/agent")
public class AgentController {
    private final AgentService agentService;
    private final AgenceService agenceService;

    // get Methods
    @GetMapping("/all")
    public ResponseEntity<List<Agent>> getAllAgents(){
        return ResponseEntity.ok().body(agentService.getAllAgents());
    }
    @GetMapping("/{id}")
    public Agent getAgent(@PathVariable("id") Long id){
        return agentService.getAgent(id);
    }
    @GetMapping("/agent/username/{username}")
    public Agent getAgentByUsername(@PathVariable("username") String username){
        return agentService.getAgentByUsername(username);
    }

    // post Methods
    @PostMapping("/register")
    public ResponseEntity<Agent> addAgent(@RequestBody Agent agent) throws Exception {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/agent/add").toUriString());
        log.info("addAgent: {}", agent);
        Agence agence=null;
        try {
            if (agent.getAgence().getId() != null) {
                log.info("addAgent: agenceId: {}", agent.getAgence().getId());
                agence = agenceService.getAgenceById(agent.getAgence().getId());
            } else if (agent.getAgence().getNom() != null) {
                agence = agenceService.getAgenceByNom(agent.getAgence().getNom());
            } else {
                throw new Exception("Agence not found");
            }
        }catch (NullPointerException e){
            log.info("Agence is null");
        }
        agent.setAgence(agence);
        return ResponseEntity.created(uri).body(agentService.addAgent(agent));
    }

    // put Methods
    @PutMapping("/update/{id}")
    public Agent updateAgent(@PathVariable("id") Long id, @RequestBody Agent agent){
        return agentService.updateAgent(id, agent);
    }

    // delete Methods
    @DeleteMapping("/delete/{id}")
    public void deleteAgent(@PathVariable("id") Long id){
        agentService.deleteAgent(id);
    }

    // other Methods
    @PostMapping("/addRole")
    public void addRoleToUser(@RequestBody AddRoleForm form){
        log.info("addRoleToUser: {}", form);
        agentService.addRoleToAgent(form.getUsername(), form.getRoleName());
    }

    @Data
    public static class AddRoleForm {
        private String username;
        private String roleName;
    }

    @PostMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            try {
                String refreshToken =authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier= JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refreshToken);
                String username = decodedJWT.getSubject();
                Agent agent = agentService.getAgentByUsername(username);

                String accessToken = JWT.create()
                        .withSubject(agent.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 3 * 24 * 60 * 60 * 1000))//expire in 3 days
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles", agent.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                        .sign(algorithm);

                Map<String,String> tokens =new HashMap<>();
                tokens.put("Access-Token",accessToken);
                tokens.put("Refresh-Token",refreshToken);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(),tokens);
            }catch (Exception e){
                log.error("error wrong token");
                response.setHeader("error",e.getMessage());
                response.setStatus(FORBIDDEN.value());
                Map<String,String> error =new HashMap<>();
                error.put("error-Message",e.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(),error);
            }

        }
        else {
            throw new RuntimeException("Refresh token is missing");
        }
    }

}
