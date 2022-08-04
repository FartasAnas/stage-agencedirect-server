package stage.agencedirectserver.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import stage.agencedirectserver.entities.Pack;
import stage.agencedirectserver.exceptions.NotFoundException;
import stage.agencedirectserver.services.PackService;
import stage.agencedirectserver.utils.UriUtil;

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
    public ResponseEntity<Pack> addPack(@RequestBody Pack pack){ return ResponseEntity.created(UriUtil.Uri("/api/pack/add")).body(packService.addPack(pack)); }

    // update Methods
    @PutMapping("/update/{id}")
    public Pack updatePack(@PathVariable("id") Long id,@RequestBody Pack pack) throws NotFoundException { return packService.updatePack(id,pack); }

    // delete Methods
    @DeleteMapping("/delete/{id}")
    public void deletePack(@PathVariable("id") Long id){ packService.deletePack(id);  }

}
