package stage.agencedirectserver.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import stage.agencedirectserver.entities.Agence;
import stage.agencedirectserver.entities.Agent;
import stage.agencedirectserver.entities.Role;
import stage.agencedirectserver.repositories.AgenceRepository;
import stage.agencedirectserver.repositories.AgentRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service @Transactional @RequiredArgsConstructor @Slf4j
public class AgenceService {
    private final AgenceRepository agenceRepository;
    private final AgentRepository agentRepository;

    // get Methods
    public List<Agence> getAllAgences() {
        log.info("Fetching all agences");
        return agenceRepository.findAll();
    }
    public List<Agence> getAgencesByVille(String ville) {
        log.info("Fetching agences with ville {}", ville);
        return agenceRepository.findAllByVille(ville);
    }
    public Agence getAgenceById(Long id){
        log.info("Fetching agence with id {}", id);
        return agenceRepository.findById(id).orElseThrow(null);
    }
    public Agence getAgenceByNom(String nom){
        log.info("Fetching agence with nom {}", nom);
        return agenceRepository.findByNom(nom);
    }

    // add Methods
    public Agence addAgence(Agence agence) {
        log.info("Adding agence {}", agence);
        return agenceRepository.save(agence);
    }

    // update Methods
    public Agence updateAgence(Agence agence) {
        log.info("Updating agence {}", agence);
        Agence agenceToUpdate = agenceRepository.findById(agence.getId()).orElseThrow(null);
        agenceToUpdate.setNom(agence.getNom()!=null ? agence.getNom() : agenceToUpdate.getNom());
        agenceToUpdate.setVille(agence.getVille()!=null ? agence.getVille() : agenceToUpdate.getVille());
        agenceToUpdate.setAdresse(agence.getAdresse()!=null ? agence.getAdresse() : agenceToUpdate.getAdresse());
        agenceToUpdate.setX(agence.getX()!=null ? agence.getX() : agenceToUpdate.getX());
        agenceToUpdate.setY(agence.getY()!=null ? agence.getY() : agenceToUpdate.getY());
        return agenceToUpdate;
    }

    // delete Methods
    public void deleteAgence(Long id) {
        log.info("Deleting agence with id {}", id);
        agenceRepository.deleteById(id);
    }

    // other Methods
    public void addAgentToAgence(String username,String agenceName){
        Agent agent = agentRepository.findByUsername(username);
        Agence agence = agenceRepository.findByNom(agenceName);
        if(agent == null || agence == null) {
            log.info("Agent or Agence not found");
            throw new RuntimeException("Agent or Agence not found");
        }
        else if(agence.getAgents().contains(agent)) {
            log.info("Agence already has this Agent");
            throw new RuntimeException("Agence already has this Agent");
        }

        log.info("adding Agent {}", agent.getUsername() ," to Agence {}",agence.getNom());
        agent.setAgence(agence);
    }
}
