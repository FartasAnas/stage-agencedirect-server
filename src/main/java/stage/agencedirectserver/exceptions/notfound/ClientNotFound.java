package stage.agencedirectserver.exceptions.notfound;

public class ClientNotFound extends Exception {
    public ClientNotFound() {
        super("Client was not Found");
    }
}
