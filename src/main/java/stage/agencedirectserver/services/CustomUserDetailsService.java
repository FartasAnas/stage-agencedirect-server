package stage.agencedirectserver.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import stage.agencedirectserver.entities.Agent;
import stage.agencedirectserver.entities.Client;
import stage.agencedirectserver.repositories.AgentRepository;
import stage.agencedirectserver.repositories.ClientRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {
    private final ClientRepository clientRepository;
    private final AgentRepository agentRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loadUserByUsernameAgent");
        Client client = clientRepository.findByEmail(username);
        Agent agent = agentRepository.findByUsername(username);
        if (client == null && agent == null) {
            throw new UsernameNotFoundException("User not found");
        } else if(agent!=null) {
            log.info("Agent found");
            Collection<SimpleGrantedAuthority> authorities =new ArrayList<>();
            agent.getRoles().forEach(role ->
                    authorities.add(new SimpleGrantedAuthority(role.getName()))
            );
            return new User(agent.getUsername(), agent.getPassword(), authorities);
        } else {
            log.info("Client found");
            Collection<SimpleGrantedAuthority> authorities =new ArrayList<>();
            client.getRoles().forEach(role ->
                    authorities.add(new SimpleGrantedAuthority(role.getName()))
            );
            return new User(client.getEmail(), client.getCodeAccess(), authorities);
        }



    }
}
