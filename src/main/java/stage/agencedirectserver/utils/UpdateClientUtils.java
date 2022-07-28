package stage.agencedirectserver.utils;

import stage.agencedirectserver.entities.Client;

public class UpdateClientUtils {
    public static void update(Client clientToUpdate, Client client){
        clientToUpdate.setEmail(client.getEmail()!=null ? client.getEmail() : clientToUpdate.getEmail());
        clientToUpdate.setCodeAccess(client.getCodeAccess()!=null ? client.getCodeAccess() : clientToUpdate.getCodeAccess());
        clientToUpdate.setNom(client.getNom()!=null ? client.getNom() : clientToUpdate.getNom());
        clientToUpdate.setPrenom(client.getPrenom()!=null ? client.getPrenom() : clientToUpdate.getPrenom());
        clientToUpdate.setAdresse(client.getAdresse()!=null ? client.getAdresse() : clientToUpdate.getAdresse());
        clientToUpdate.setTelephone(client.getTelephone()!=null ? client.getTelephone() : clientToUpdate.getTelephone());
        clientToUpdate.setDateNaissance(client.getDateNaissance()!=null ? client.getDateNaissance() : clientToUpdate.getDateNaissance());
        clientToUpdate.setDateInscription(client.getDateInscription()!=null ? client.getDateInscription() : clientToUpdate.getDateInscription());
        clientToUpdate.setDateExpiration(client.getDateExpiration()!=null ? client.getDateExpiration() : clientToUpdate.getDateExpiration());
        clientToUpdate.setCinRectoURL(client.getCinRectoURL()!=null ? client.getCinRectoURL() : clientToUpdate.getCinRectoURL());
        clientToUpdate.setCinVersoURL(client.getCinVersoURL()!=null ? client.getCinVersoURL() : clientToUpdate.getCinVersoURL());
        clientToUpdate.setSelfieURL(client.getSelfieURL()!=null ? client.getSelfieURL() : clientToUpdate.getSelfieURL());
        clientToUpdate.setAdresse(client.getAdresse()!=null ? client.getAdresse() : clientToUpdate.getAdresse());
        clientToUpdate.setProfession(client.getProfession()!=null ? client.getProfession() : clientToUpdate.getProfession());
    }
}
