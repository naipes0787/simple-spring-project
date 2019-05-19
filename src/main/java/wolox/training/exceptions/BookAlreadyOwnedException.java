package wolox.training.exceptions;

public class BookAlreadyOwnedException extends RuntimeException {

    public BookAlreadyOwnedException() {
        super("The user already has this book");
    }

}
