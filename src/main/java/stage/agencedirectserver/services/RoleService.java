package stage.agencedirectserver.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import stage.agencedirectserver.entities.Role;
import stage.agencedirectserver.exceptions.NotFoundException;
import stage.agencedirectserver.repositories.RoleRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service @Transactional @RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    // get Methods
    public List<Role> getAllRoles(){
        return roleRepository.findAll();
    }

    // post Methods
    public Role addRole(Role role){
        return roleRepository.save(role);
    }

    // update Methods
    public Role updateRole(Long id , Role role) throws NotFoundException {
        Role roleToUpdate=roleRepository.findById(id).orElseThrow(() -> new NotFoundException("Role was not found"));
        roleToUpdate.setName(role.getName()!=null ? role.getName() : roleToUpdate.getName());
        return roleToUpdate;
    }

    // delete Methods
    public void deleteRole(Long id){
        roleRepository.deleteById(id);
    }

}
