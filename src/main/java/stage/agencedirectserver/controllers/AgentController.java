package stage.agencedirectserver.controllers;


import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import stage.agencedirectserver.entities.Agent;
import stage.agencedirectserver.exceptions.NotFoundException;
import stage.agencedirectserver.services.AgentService;
import stage.agencedirectserver.utils.UriUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


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
        return ResponseEntity.created(UriUtil.Uri("/api/agent/add")).body(agentService.addAgent(agent));
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

    @PostMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {agentService.refreshToken(request, response);}

    @Data
    public static class AddRoleForm {
        private String username;
        private String roleName;
    }
}
