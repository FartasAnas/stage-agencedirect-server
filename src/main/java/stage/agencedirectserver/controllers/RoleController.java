package stage.agencedirectserver.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import stage.agencedirectserver.entities.Role;
import stage.agencedirectserver.services.RoleService;

import java.net.URI;
import java.util.List;

@RestController @RequiredArgsConstructor @RequestMapping("/api/role")
public class RoleController {
    public final RoleService roleService;

    // get Methods
    @GetMapping("/all")
    public List<Role> getAllAppUsers() { return roleService.getAllRoles();  }

    // post Methods
    @PostMapping("/add")
    public ResponseEntity<Role> addRole(@RequestBody Role role){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/role/add").toUriString());
        return ResponseEntity.created(uri).body(roleService.addRole(role));
    }

    // update Methods
    @PutMapping("/update/{id}")
    public Role updateRole(@PathVariable("id") Long id,@RequestBody Role role){ return roleService.updateRole(id,role); }

    // delete Methods
    @DeleteMapping("/delete/{id}")
    public void deleteRole(@PathVariable("id") Long id){ roleService.deleteRole(id);  }
}
