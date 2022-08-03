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
import stage.agencedirectserver.exceptions.NotFoundException;
import stage.agencedirectserver.services.AgenceService;
import stage.agencedirectserver.services.AgentService;
import stage.agencedirectserver.utils.GenerateTokenUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.*;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@RestController @RequiredArgsConstructor @RequestMapping("/api/agent")
public class AgentController {
    private final AgentService agentService;

    // get Methods
    @GetMapping("/all")
    public ResponseEntity<List<Agent>> getAllAgents(){
        return ResponseEntity.ok().body(agentService.getAllAgents());
    }
    @GetMapping("/{id}")
    public Agent getAgent(@PathVariable("id") Long id) throws NotFoundException {
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
        return ResponseEntity.created(uri).body(agentService.addAgent(agent));
    }

    // put Methods
    @PutMapping("/update/{id}")
    public Agent updateAgent(@PathVariable("id") Long id, @RequestBody Agent agent) throws NotFoundException {
        return agentService.updateAgent(id, agent);
    }

    // delete Methods
    @DeleteMapping("/delete/{id}")
    public void deleteAgent(@PathVariable("id") Long id){
        agentService.deleteAgent(id);
    }

    // other Methods
    @PostMapping("/addRole")
    public void addRoleToUser(@RequestBody AddRoleForm form) throws NotFoundException { agentService.addRoleToAgent(form.getUsername(), form.getRoleName()); }

    @Data
    public static class AddRoleForm {
        private String username;
        private String roleName;
    }
    @PostMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String refreshToken = authorizationHeader.substring("Bearer ".length());
            Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(refreshToken);
            String username = decodedJWT.getSubject();
            Agent agent = agentService.getAgentByUsername(username);
            GenerateTokenUtil.generateToken(request, response, agent, refreshToken,algorithm);
        }else {
            throw new RuntimeException("Refresh token is missing");
        }
    }

}
