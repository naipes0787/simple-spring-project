package wolox.training.controllers;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import wolox.training.exceptions.BookIdMismatchException;
import wolox.training.exceptions.RequiredFieldNotExists;
import wolox.training.models.Book;
import wolox.training.repositories.BookRepository;
import wolox.training.services.BookService;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    @SuppressWarnings("unused")
    private BookRepository bookRepository;

    @Autowired
    @SuppressWarnings("unused")
    private BookService bookService;

    @GetMapping("/{id}")
    @SuppressWarnings("unused")
    public Book findOne(@PathVariable Long id) {
        return bookRepository.findById(id).
            orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Book with id " + id + " not found"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Book create(@RequestBody Book book) {
        return bookRepository.save(book);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        bookRepository.findById(id).
            orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Book with id " + id + " not found"));
        bookRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    public Book updateBook(@RequestBody Book book, @PathVariable Long id) {
        if (book.getId() != id) {
            throw new BookIdMismatchException();
        }
        bookRepository.findById(id).
            orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Book with id " + id + " not found"));
        return bookRepository.save(book);
    }

    @GetMapping("/search-by-isbn")
    public ResponseEntity<Book> search(@RequestParam("isbn") String isbn) {
        try {
            Optional<Book> optionalBook = bookRepository.findByIsbn(isbn);
            if (optionalBook.isPresent()) {
                return (ResponseEntity.status(HttpStatus.OK).body(optionalBook.get()));
            } else {
                return ResponseEntity.status(HttpStatus.CREATED)
                    .body(bookService.findByIsbn(isbn).orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Book with ISBN " + isbn + " not found")));
            }
        } catch (RequiredFieldNotExists ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                ex.getMessage());
        }
    }

    @GetMapping
    @ResponseBody
    public List<Book> getBooks(@RequestParam(required = false, defaultValue = "") String id,
        @RequestParam(required = false, defaultValue = "") String genre,
        @RequestParam(required = false, defaultValue = "") String author,
        @RequestParam(required = false, defaultValue = "") String image,
        @RequestParam(required = false, defaultValue = "") String title,
        @RequestParam(required = false, defaultValue = "") String subtitle,
        @RequestParam(required = false, defaultValue = "") String publisher,
        @RequestParam(required = false, defaultValue = "") String fromYear,
        @RequestParam(required = false, defaultValue = "") String toYear,
        @RequestParam(required = false, defaultValue = "") String pages,
        @RequestParam(required = false, defaultValue = "") String isbn,
        Pageable pageable) {
        return bookRepository
            .findAllByEveryField(id, genre, author, image, title, subtitle, publisher,
                fromYear, toYear, pages, isbn, pageable)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Not found")
            );
    }


}
