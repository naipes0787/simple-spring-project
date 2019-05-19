package wolox.training.exceptions;

public class BookIdMismatchException extends RuntimeException {

    public BookIdMismatchException() {
        super("Book.id is not the same as the id parameter");
    }
}
