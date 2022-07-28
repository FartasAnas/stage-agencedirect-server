package stage.agencedirectserver.exceptions.notfound;

public class ClientOrAgentNotFound extends Exception {
    public ClientOrAgentNotFound() {
        super("Client or Agence not found");
    }
}
