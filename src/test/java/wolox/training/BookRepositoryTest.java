package wolox.training;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;
import wolox.training.models.Book;
import wolox.training.repositories.BookRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class BookRepositoryTest {

    @Autowired
    @SuppressWarnings("unused")
    private BookRepository bookRepository;

    @Autowired
    @SuppressWarnings("unused")
    private TestEntityManager entityManager;

    private Book oneTestBook;
    private Book anotherTestBook;

    @Before
    public void setUp() {
        oneTestBook = new Book();
        oneTestBook.setAuthor("Carlitos");
        oneTestBook.setGenre("Terror");
        oneTestBook.setImage("unaImagen");
        oneTestBook.setIsbn("4578-8665");
        oneTestBook.setPages(259);
        oneTestBook.setPublisher("LaGuitarra");
        oneTestBook.setSubtitle("Carlito's Way");
        oneTestBook.setTitle("Las aventuras terrorificas de Carlitos");
        oneTestBook.setYear("2005");
        bookRepository.save(oneTestBook);
        anotherTestBook = new Book();
        anotherTestBook.setAuthor("Alberto");
        anotherTestBook.setGenre("Comedia");
        anotherTestBook.setImage("imagen");
        anotherTestBook.setIsbn("4578-8600");
        anotherTestBook.setPages(150);
        anotherTestBook.setPublisher("Aereal");
        anotherTestBook.setSubtitle("Aja");
        anotherTestBook.setTitle("Aventuras hilarantes");
        anotherTestBook.setYear("2002");
        bookRepository.save(anotherTestBook);
    }

    @Test
    public void whenFindBookByAuthorAndExists_thenBookIsReturned() {
        Book bookFromDatabase = bookRepository.findFirstByAuthorIgnoreCase("Carlitos")
            .orElseGet(null);
        assertThat(bookFromDatabase).isEqualToComparingFieldByField(oneTestBook);
    }

    @Test
    public void whenCreateBook_thenBookIsPersisted() {
        Book persistedBook = bookRepository.findFirstByAuthorIgnoreCase("Carlitos").orElse(null);
        assertThat(persistedBook.getAuthor().equals(oneTestBook.getAuthor())).isTrue();
        assertThat(persistedBook.getGenre().equals(oneTestBook.getGenre())).isTrue();
        assertThat(persistedBook.getImage().equals(oneTestBook.getImage())).isTrue();
        assertThat(persistedBook.getIsbn().equals(oneTestBook.getIsbn())).isTrue();
        assertThat(persistedBook.getPages().equals(oneTestBook.getPages())).isTrue();
        assertThat(persistedBook.getPublisher().equals(oneTestBook.getPublisher())).isTrue();
        assertThat(persistedBook.getSubtitle().equals(oneTestBook.getSubtitle())).isTrue();
        assertThat(persistedBook.getTitle().equals(oneTestBook.getTitle())).isTrue();
        assertThat(persistedBook.getYear().equals(oneTestBook.getYear())).isTrue();
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenCreateBookWithoutAuthor_thenThrowException() {
        oneTestBook.setAuthor(null);
        bookRepository.save(oneTestBook);
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenCreateBookWithoutImage_thenThrowException() {
        oneTestBook.setImage(null);
        bookRepository.save(oneTestBook);
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenCreateBookWithoutTitle_thenThrowException() {
        oneTestBook.setTitle(null);
        bookRepository.save(oneTestBook);
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenCreateBookWithoutSubtitle_thenThrowException() {
        oneTestBook.setSubtitle(null);
        bookRepository.save(oneTestBook);
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenCreateBookWithoutPublisher_thenThrowException() {
        oneTestBook.setPublisher(null);
        bookRepository.save(oneTestBook);
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenCreateBookWithoutYear_thenThrowException() {
        oneTestBook.setYear(null);
        bookRepository.save(oneTestBook);
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenCreateBookWithoutPages_thenThrowException() {
        oneTestBook.setPages(null);
        bookRepository.save(oneTestBook);
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenCreateBookWithoutIsbn_thenThrowException() {
        oneTestBook.setIsbn(null);
        bookRepository.save(oneTestBook);
    }

    @Test
    public void whenFindByEveryField_thenBookIsReturned() {
        Pageable customPageable = PageRequest.of(0, 5, Sort.by("id"));
        List<Book> books = bookRepository
            .findAllByEveryField(String.valueOf(oneTestBook.getId()), oneTestBook.getGenre(),
                oneTestBook.getAuthor(),
                oneTestBook.getImage(), oneTestBook.getTitle(), oneTestBook.getSubtitle(),
                oneTestBook.getPublisher(), oneTestBook.getYear(), oneTestBook.getYear(),
                oneTestBook.getPages().toString(), oneTestBook.getIsbn(), customPageable).get();
        Book book = books.get(0);
        assertThat(book.getIsbn().equals(oneTestBook.getIsbn())).isTrue();
    }

    @Test
    public void whenFindByOneSingleFieldAndExists_thenBookIsReturned() {
        Pageable customPageable = PageRequest.of(0, 5, Sort.by("id"));
        List<Book> books = bookRepository
            .findAllByEveryField("", oneTestBook.getGenre(), "", "", "",
                "", "", "", "", "", "",
                customPageable).get();
        Book book = books.get(0);
        assertThat(book.getIsbn().equals(oneTestBook.getIsbn())).isTrue();
    }

    @Test
    public void whenFindWithoutSpecificData_thenEveryBookIsReturned() {
        Pageable customPageable = PageRequest.of(0, 5, Sort.by("id"));
        List<Book> books = bookRepository
            .findAllByEveryField("", "", "", "", "",
                "", "", "", "", "", "",
                customPageable).get();
        assertThat(books.size() == 2).isTrue();
    }

    @Test
    public void whenFindByCorrectFromYearAndToYear_thenBookIsReturned() {
        Pageable customPageable = PageRequest.of(0, 5, Sort.by("id"));
        List<Book> books = bookRepository
            .findAllByEveryField("", "", "", "", "",
                "", "", "2004", "2006", "", "",
                customPageable).get();
        Book book = books.get(0);
        assertThat(book.getIsbn().equals(oneTestBook.getIsbn())).isTrue();
    }

    @Test
    public void whenFindByIncorrectFromYearAndToYear_thenEmptyOptionalIsReturned() {
        Pageable customPageable = PageRequest.of(0, 5, Sort.by("id"));
        Optional<List<Book>> books = bookRepository
            .findAllByEveryField("", "", "", "", "",
                "", "", "2003", "2004", "", "",
                customPageable);
        assertThat(books.isPresent()).isFalse();
    }

    @Test
    public void whenSortElements_thenCorrectOrderedListIsReturned() {
        Pageable customPageable = PageRequest.of(0, 5, Sort.by("title"));
        List<Book> books = bookRepository
            .findAllByEveryField("", "", "", "", "",
                "", "", "2000", "2008", "", "",
                customPageable).get();
        Book firstBook = books.get(0);
        Book secondBook = books.get(1);
        assertThat(firstBook.getIsbn().equals(anotherTestBook.getIsbn())).isTrue();
        assertThat(secondBook.getIsbn().equals(oneTestBook.getIsbn())).isTrue();
    }

    @Test
    public void whenChangePageElementsNumber_thenCorrectSizedListIsReturned() {
        Pageable customPageable = PageRequest.of(0, 1, Sort.by("title"));
        List<Book> books = bookRepository
            .findAllByEveryField("", "", "", "", "",
                "", "", "2000", "2008", "", "",
                customPageable).get();
        assertThat(books.size() == 1).isTrue();
    }

}
