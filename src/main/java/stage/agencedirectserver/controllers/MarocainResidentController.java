package stage.agencedirectserver.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import stage.agencedirectserver.entities.MarocainResident;
import stage.agencedirectserver.exceptions.EmailNotValidException;
import stage.agencedirectserver.exceptions.NotFoundException;
import stage.agencedirectserver.exceptions.NullAttributeException;
import stage.agencedirectserver.services.MarocainResidentService;
import stage.agencedirectserver.utils.UriUtil;

import javax.mail.MessagingException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/marocainresident")
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
        return ResponseEntity.created(UriUtil.Uri("/api/marocainresident/add")).body(marocainResidentService.addMarocainResident(marocainResident));
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
