package stage.agencedirectserver.exceptions.notfound;

public class AgenceNotFoundException extends Exception {
    public AgenceNotFoundException() {
        super("Agence was not Found");
    }
}
