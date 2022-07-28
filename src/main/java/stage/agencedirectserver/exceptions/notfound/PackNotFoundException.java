package stage.agencedirectserver.exceptions.notfound;

public class PackNotFoundException extends Exception {
    public PackNotFoundException() {
        super("Pack was not Found");
    }
}
