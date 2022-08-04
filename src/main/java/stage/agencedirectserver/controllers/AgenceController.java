package stage.agencedirectserver.controllers;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import stage.agencedirectserver.entities.Agence;
import stage.agencedirectserver.exceptions.NotFoundException;
import stage.agencedirectserver.services.AgenceService;
import stage.agencedirectserver.utils.UriUtil;

import java.util.List;

@RestController @RequiredArgsConstructor @RequestMapping("/api/agence")
public class AgenceController {
    public final AgenceService agenceService;

    // get Methods
    @GetMapping("/all")
    public List<Agence> getAllAgences() { return agenceService.getAllAgences();  }
    @GetMapping("/ville/{ville}")
    public List<Agence> getAgencesByVille(@PathVariable("ville") String ville) { return agenceService.getAgencesByVille(ville); }

    // add Methods
    @PostMapping("/add")
    public ResponseEntity<Agence> addAgence(@RequestBody Agence agence) { return ResponseEntity.created(UriUtil.Uri("/api/agence/add")).body(agenceService.addAgence(agence)); }

    // update Methods
    @PutMapping("/update/{id}")
    public Agence updateAgence(@PathVariable("id") Long id,@RequestBody Agence agence) throws NotFoundException { return agenceService.updateAgence(id,agence); }

    // delete Methods
    @DeleteMapping("/delete/{id}")
    public void deleteAgence(@PathVariable("id") Long id) { agenceService.deleteAgence(id);  }

    // other Methods
    @PostMapping("/addAgent")
    public void addAgentToAgence(@RequestBody AddAgentForm form) throws NotFoundException { agenceService.addAgentToAgence(form.getUsername(), form.getAgenceName()); }
    @Data
    public static class AddAgentForm {
        private String username;
        private String agenceName;
    }
}
