package stage.agencedirectserver.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import stage.agencedirectserver.entities.Pack;
import stage.agencedirectserver.exceptions.notfound.PackNotFoundException;
import stage.agencedirectserver.services.PackService;

import java.net.URI;
import java.util.List;

@RestController @RequiredArgsConstructor @RequestMapping("/api/pack")
public class PackController {
    public final PackService packService;

    // get Methods
    @GetMapping("/all")
    public List<Pack> getAllPacks() { return packService.getAllPacks();  }
    @GetMapping("/titre/{titre}")
    public Pack getPackByTitre(@PathVariable("titre") String titre) { return packService.getPackByTitre(titre);  }

    // post Methods
    @PostMapping("/add")
    public ResponseEntity<Pack> addPack(@RequestBody Pack pack){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/pack/add").toUriString());
        return ResponseEntity.created(uri).body(packService.addPack(pack));
    }

    // update Methods
    @PutMapping("/update/{id}")
    public Pack updatePack(@PathVariable("id") Long id,@RequestBody Pack pack) throws PackNotFoundException { return packService.updatePack(id,pack); }

    // delete Methods
    @DeleteMapping("/delete/{id}")
    public void deletePack(@PathVariable("id") Long id){ packService.deletePack(id);  }

}
