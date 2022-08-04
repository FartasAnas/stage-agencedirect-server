package stage.agencedirectserver.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import stage.agencedirectserver.entities.Client;
import stage.agencedirectserver.entities.Etudiant;
import stage.agencedirectserver.exceptions.EmailNotValidException;
import stage.agencedirectserver.exceptions.NotFoundException;
import stage.agencedirectserver.exceptions.NullAttributeException;
import stage.agencedirectserver.services.EtudiantService;
import stage.agencedirectserver.utils.UriUtil;

import javax.mail.MessagingException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/etudiant")
public class EtudiantController {
    private final EtudiantService etudiantService;

    // get Methods
    @GetMapping("/all")
    public List<Etudiant> getAllEtudiants() { return etudiantService.getAllEtudiants();  }
    @GetMapping("/email/{email}")
    public Etudiant getEtudiantByEmail(@PathVariable("email") String email) { return etudiantService.getEtudiantByEmail(email); }
    @GetMapping("/cin/{cin}")
    public Etudiant getEtudiantByCIN(@PathVariable("cin") String cin) { return etudiantService.getEtudiantByCIN(cin); }

    // add Methods
    @PostMapping("/add")
    public ResponseEntity<Etudiant> addEtudiant(@RequestBody Etudiant etudiant) throws MessagingException, NotFoundException, NullAttributeException, EmailNotValidException {
        return ResponseEntity.created(UriUtil.Uri("/api/etudiant/add")).body(etudiantService.addEtudiant(etudiant));
    }

    // update Methods
    @PutMapping("/update/{id}")
    public Client updateClient(@PathVariable("id") Long id, @RequestBody Etudiant newEtudiant) throws  NotFoundException { return etudiantService.updateEtudiant(id, newEtudiant); }

    // delete Methods
    @DeleteMapping("/delete/{id}")
    public void deleteRole(@PathVariable("id") Long id){ etudiantService.deleteEtudiant(id);  }
}
