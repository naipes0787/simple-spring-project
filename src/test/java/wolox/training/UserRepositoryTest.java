package wolox.training;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
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
import wolox.training.models.User;
import wolox.training.repositories.BookRepository;
import wolox.training.repositories.UserRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    @SuppressWarnings("unused")
    private UserRepository userRepository;

    @Autowired
    @SuppressWarnings("unused")
    private BookRepository bookRepository;

    @Autowired
    @SuppressWarnings("unused")
    private TestEntityManager entityManager;

    private User oneTestUser;
    private User anotherTestUser;

    @Before
    public void setUp() {
        Book oneTestBook = new Book();
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
        oneTestUser = new User();
        oneTestUser.setUsername("carlitosbala");
        oneTestUser.setPassword("pass");
        oneTestUser.setName("Carlos Rovira");
        oneTestUser.setBirthdate(LocalDate.parse("2000-03-27"));
        oneTestUser.addBook(oneTestBook);
        userRepository.save(oneTestUser);
        anotherTestUser = new User();
        anotherTestUser.setUsername("aleblanco");
        anotherTestUser.setPassword("pass");
        anotherTestUser.setName("Alejandro Blanco");
        anotherTestUser.setBirthdate(LocalDate.parse("1990-03-27"));
        anotherTestUser.addBook(oneTestBook);
        userRepository.save(anotherTestUser);
    }

    @Test
    public void whenCreateUser_thenUserIsPersisted() {
        User persistedUser = userRepository.findFirstByUsername("carlitosbala").orElse(null);
        assertThat(persistedUser.getUsername().equals(oneTestUser.getUsername())).isTrue();
        assertThat(persistedUser.getName().equals(oneTestUser.getName())).isTrue();
        assertThat(persistedUser.getBirthdate().equals(oneTestUser.getBirthdate())).isTrue();
        assertThat(persistedUser.getBooks().size() == oneTestUser.getBooks().size()).isTrue();
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenCreateUserWithoutUsername_thenThrowException() {
        oneTestUser.setUsername(null);
        userRepository.save(oneTestUser);
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenCreateUserWithoutName_thenThrowException() {
        oneTestUser.setName(null);
        userRepository.save(oneTestUser);
    }

    @Test(expected = NullPointerException.class)
    public void whenCreateUserWithoutBirthdate_thenThrowException() {
        oneTestUser.setBirthdate(null);
        userRepository.save(oneTestUser);
    }

    @Test
    public void whenFindByBirthdateAndName_thenUserIsReturned() {
        Pageable customPageable = PageRequest.of(0, 5, Sort.by("id"));
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        LocalDate from = LocalDate.parse("1980-12-01", formatter);
        LocalDate to = LocalDate.parse("2010-12-31", formatter);
        List<User> users = userRepository
            .findAllByBirthdateBetweenAndNameContainingIgnoreCase(from, to, "Rovi",
                customPageable)
            .get();
        User user = users.get(0);
        assertThat(user.getUsername().equals(oneTestUser.getUsername())).isTrue();
        assertThat(user.getName().equals(oneTestUser.getName())).isTrue();
    }

    @Test
    public void whenFindByBirthdateAndNull_thenUserIsReturned() {
        Pageable customPageable = PageRequest.of(0, 5, Sort.by("id"));
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        LocalDate from = LocalDate.parse("1980-12-01", formatter);
        LocalDate to = LocalDate.parse("2010-12-31", formatter);
        List<User> users = userRepository
            .findAllByBirthdateBetweenAndNameContainingIgnoreCase(from, to, "",
                customPageable)
            .get();
        User user = users.get(0);
        assertThat(user.getUsername().equals(oneTestUser.getUsername())).isTrue();
        assertThat(user.getName().equals(oneTestUser.getName())).isTrue();
    }

    @Test
    public void whenFindByNameAndNull_thenUserIsReturned() {
        Pageable customPageable = PageRequest.of(0, 5, Sort.by("id"));
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        LocalDate from = LocalDate.parse("0000-01-01", formatter);
        LocalDate to = LocalDate.parse("9999-12-31", formatter);
        List<User> users = userRepository
            .findAllByBirthdateBetweenAndNameContainingIgnoreCase(
                from, to, "Rovi", customPageable).get();
        User user = users.get(0);
        assertThat(user.getUsername().equals(oneTestUser.getUsername())).isTrue();
        assertThat(user.getName().equals(oneTestUser.getName())).isTrue();
    }

    @Test
    public void whenFindByFromAndDefaultValues_thenUserIsReturned() {
        Pageable customPageable = PageRequest.of(0, 5, Sort.by("id"));
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        LocalDate from = LocalDate.parse("1980-12-01", formatter);
        LocalDate to = LocalDate.parse("9999-12-31", formatter);
        List<User> users = userRepository
            .findAllByBirthdateBetweenAndNameContainingIgnoreCase(
                from, to, "", customPageable).get();
        User user = users.get(0);
        assertThat(user.getUsername().equals(oneTestUser.getUsername())).isTrue();
        assertThat(user.getName().equals(oneTestUser.getName())).isTrue();
    }

    @Test
    public void whenFindByToAndDefaultValues_thenUserIsReturned() {
        Pageable customPageable = PageRequest.of(0, 5, Sort.by("id"));
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        LocalDate to = LocalDate.parse("2005-12-01", formatter);
        LocalDate from = LocalDate.parse("0000-01-01", formatter);
        List<User> users = userRepository
            .findAllByBirthdateBetweenAndNameContainingIgnoreCase(
                from, to, "", customPageable).get();
        User user = users.get(0);
        assertThat(user.getUsername().equals(oneTestUser.getUsername())).isTrue();
        assertThat(user.getName().equals(oneTestUser.getName())).isTrue();
    }

    @Test
    public void whenSortElements_thenCorrectOrderedListIsReturned() {
        Pageable customPageable = PageRequest.of(0, 5, Sort.by("name"));
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        LocalDate from = LocalDate.parse("1980-12-01", formatter);
        LocalDate to = LocalDate.parse("2010-12-31", formatter);
        List<User> users = userRepository
            .findAllByBirthdateBetweenAndNameContainingIgnoreCase(from, to, "",
                customPageable)
            .get();
        User firstUser = users.get(0);
        User secondUser = users.get(1);
        assertThat(firstUser.getUsername().equals(anotherTestUser.getUsername())).isTrue();
        assertThat(secondUser.getUsername().equals(oneTestUser.getUsername())).isTrue();
    }

    @Test
    public void whenChangePageElementsNumber_thenCorrectSizedListIsReturned() {
        Pageable customPageable = PageRequest.of(0, 1, Sort.by("name"));
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        LocalDate from = LocalDate.parse("1980-12-01", formatter);
        LocalDate to = LocalDate.parse("2010-12-31", formatter);
        List<User> users = userRepository
            .findAllByBirthdateBetweenAndNameContainingIgnoreCase(from, to, "",
                customPageable)
            .get();
        assertThat(users.size() == 1).isTrue();
    }

}
