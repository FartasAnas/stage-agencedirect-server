package stage.agencedirectserver.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import stage.agencedirectserver.entities.Agent;
import stage.agencedirectserver.entities.Role;
import stage.agencedirectserver.exceptions.NotFoundException;
import stage.agencedirectserver.repositories.AgenceRepository;
import stage.agencedirectserver.repositories.AgentRepository;
import stage.agencedirectserver.repositories.RoleRepository;
import stage.agencedirectserver.utils.GenerateTokenUtil;
import stage.agencedirectserver.utils.affectation.AgentToAgence;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Service @Transactional @RequiredArgsConstructor @Slf4j
//@Primary @Component("AgentService")
public class AgentService{
    private final AgentRepository agentRepository;
    private final RoleRepository roleRepository;
    private final AgenceRepository agenceRepository;

    private final PasswordEncoder passwordEncoder;


    // get Methods
    public List<Agent> getAllAgents() {
        log.info("Fetching all agents");
        return agentRepository.findAll();
    }
    public Agent getAgent(Long id) throws NotFoundException {
        log.info("Fetching agent with id: {}", id);
        return agentRepository.findById(id).orElseThrow(() -> new NotFoundException("Agent was not found"));
    }
    public Agent getAgentByUsername(String username) {
        log.info("Fetching agent {}",username);
        return agentRepository.findByUsername(username.toLowerCase());
    }
    public Long getAgentCount(){
        return agentRepository.count();
    }

    // post Methods
    public Agent addAgent(Agent agent) throws NotFoundException {
        log.info("Saving new user :{} password:{}",agent.getUsername(),agent.getPassword());
        agent.setPassword(passwordEncoder.encode(agent.getPassword()));
        agent.setAgence((agenceRepository.findByNom("agence direct")));
        return agentRepository.save(agent);
    }

    // put Methods
    public Agent updateAgent(Long id, Agent agent) throws NotFoundException {
        Agent agentToUpdate = agentRepository.findById(id).orElseThrow(() -> new NotFoundException("Agent was not found"));
        log.info("updating agent: {}",agentToUpdate.getUsername());
        agentToUpdate.setPrenom( agent.getPrenom()!=null ? agent.getPrenom() : agentToUpdate.getPrenom() );
        agentToUpdate.setNom( agent.getNom()!=null ? agent.getNom() : agentToUpdate.getNom());
        agentToUpdate.setUsername( agent.getUsername()!=null ? agent.getUsername() : agentToUpdate.getUsername());
        agentToUpdate.setPassword(agent.getPassword()!=null ? agent.getPassword() : agentToUpdate.getPassword());
        return agentToUpdate;
    }

    // delete Methods
    public void deleteAgent(Long id){
        agentRepository.deleteById(id);
    }

    // other Methods
    public void addRoleToAgent(String username, String roleName) throws NotFoundException {
        Agent agent = agentRepository.findByUsername(username.toLowerCase());
        Role role = roleRepository.findByName(roleName);

        if(agent == null || role == null) {
            throw new NotFoundException("Agent or role not found");
        }
        else if(agent.getRoles().contains(role)) {
            throw new DataIntegrityViolationException("Agent already has this role");
        }
        log.info("adding role {}", role.getName() ," to user {}",agent.getUsername());
        agent.getRoles().add(role);
    }
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String refreshToken = authorizationHeader.substring("Bearer ".length());
            Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(refreshToken);
            String username = decodedJWT.getSubject();
            Agent agent = getAgentByUsername(username);
            GenerateTokenUtil.generateToken(request, response, agent, refreshToken,algorithm);
        }else {
            throw new RuntimeException("Refresh token is missing");
        }
    }


}
