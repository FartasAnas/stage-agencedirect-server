package stage.agencedirectserver.controllers;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import stage.agencedirectserver.entities.Agence;
import stage.agencedirectserver.services.AgenceService;

import java.net.URI;
import java.util.List;

@RestController @RequiredArgsConstructor @RequestMapping("/api/agence") @Slf4j
public class AgenceController {
    public final AgenceService agenceService;

    // get Methods
    @GetMapping("/all")
    public List<Agence> getAllAgences() { return agenceService.getAllAgences();  }
    @GetMapping("/ville/{ville}")
    public List<Agence> getAgencesByVille(@PathVariable("ville") String ville) { return agenceService.getAgencesByVille(ville); }

    // add Methods
    @PostMapping("/add")
    public ResponseEntity<Agence> addAgence(@RequestBody Agence agence) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/agence/add").toUriString());
        return ResponseEntity.created(uri).body(agenceService.addAgence(agence));
    }

    // update Methods
    @PutMapping("/update/{id}")
    public Agence updateAgence(@RequestBody Agence agence) { return agenceService.updateAgence(agence); }

    // delete Methods
    @DeleteMapping("/delete/{id}")
    public void deleteAgence(@PathVariable("id") Long id) { agenceService.deleteAgence(id);  }

    // other Methods
    @PostMapping("/addAgent")
    public void addAgentToAgence(@RequestBody AddAgentForm form){
        agenceService.addAgentToAgence(form.getUsername(), form.getAgenceName());
    }
    @Data
    public static class AddAgentForm {
        private String username;
        private String agenceName;
    }
}
