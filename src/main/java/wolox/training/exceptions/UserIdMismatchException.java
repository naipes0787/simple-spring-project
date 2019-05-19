package wolox.training.exceptions;

public class UserIdMismatchException extends RuntimeException {

    public UserIdMismatchException() {
        super("User.id is not the same as the id parameter");
    }
}
