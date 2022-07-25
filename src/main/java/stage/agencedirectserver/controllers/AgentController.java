package stage.agencedirectserver.controllers;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import stage.agencedirectserver.entities.Agent;
import stage.agencedirectserver.services.AgentService;

import java.net.URI;
import java.util.List;
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
    public Agent getAgent(@PathVariable("id") Long id){
        return agentService.getAgent(id);
    }
    @GetMapping("/agent/username/{username}")
    public Agent getAgentByUsername(@PathVariable("username") String username){
        return agentService.getAgentByUsername(username);
    }

    // post Methods
    @PostMapping("/register")
    public ResponseEntity<Agent> addAgent(@RequestBody Agent agent){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/agent/add").toUriString());
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
    @PostMapping("/addRole/{username}/{role}")
    public void addRoleToUser(@PathVariable("username") String username, @PathVariable("role") String roleName){
        agentService.addRoleToAgent(username, roleName);
    }



}
