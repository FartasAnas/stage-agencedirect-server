package stage.agencedirectserver.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import stage.agencedirectserver.entities.MarocainResident;
import stage.agencedirectserver.exceptions.EmailNotValidException;
import stage.agencedirectserver.exceptions.NotFoundException;
import stage.agencedirectserver.exceptions.NullAttributeException;
import stage.agencedirectserver.services.MarocainResidentService;

import javax.mail.MessagingException;
import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/marocainresident") @Slf4j
public class MarocainResidentController {
    private final MarocainResidentService marocainResidentService;

    // get Methods
    @GetMapping("/all")
    public List<MarocainResident> getAllMarocainResidents() { return marocainResidentService.getAllMarocainResidents();  }
    @GetMapping("/email/{email}")
    public MarocainResident getMarocainResidentByEmail(@PathVariable("email") String email) { return marocainResidentService.getMarocainResidentByEmail(email); }
    @GetMapping("/cin/{cin}")
    public MarocainResident getMarocainResidentByCIN(@PathVariable("cin") String cin) { return marocainResidentService.getMarocainResidentByCIN(cin); }

    // add Methods
    @PostMapping("/add")
    public ResponseEntity<MarocainResident> addMarocainResident(@RequestBody MarocainResident marocainResident) throws MessagingException, NotFoundException, NullAttributeException, EmailNotValidException {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/marocainresident/add").toUriString());
        return ResponseEntity.created(uri).body(marocainResidentService.addMarocainResident(marocainResident));
    }

    // update Methods
    @PutMapping("/update/{id}")
    public MarocainResident updateMarocainResident(@PathVariable("id") Long id, @RequestBody MarocainResident newMarocainResident) throws NotFoundException {
        return marocainResidentService.updateMarocainResident(id, newMarocainResident);
    }

    // delete Methods
    @DeleteMapping("/delete/{id}")
    public void deleteMarocainResident(@PathVariable("id") Long id){ marocainResidentService.deleteMarocainResident(id);  }
}
