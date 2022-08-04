package stage.agencedirectserver.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import stage.agencedirectserver.entities.Role;
import stage.agencedirectserver.exceptions.NotFoundException;
import stage.agencedirectserver.services.RoleService;
import stage.agencedirectserver.utils.UriUtil;

import java.util.List;

@RestController @RequiredArgsConstructor @RequestMapping("/api/role")
public class RoleController {
    public final RoleService roleService;

    // get Methods
    @GetMapping("/all")
    public List<Role> getAllAppUsers() { return roleService.getAllRoles();  }

    // post Methods
    @PostMapping("/add")
    public ResponseEntity<Role> addRole(@RequestBody Role role){ return ResponseEntity.created(UriUtil.Uri("/api/role/add")).body(roleService.addRole(role)); }

    // update Methods
    @PutMapping("/update/{id}")
    public Role updateRole(@PathVariable("id") Long id,@RequestBody Role role) throws NotFoundException { return roleService.updateRole(id,role); }

    // delete Methods
    @DeleteMapping("/delete/{id}")
    public void deleteRole(@PathVariable("id") Long id){ roleService.deleteRole(id);  }
}
