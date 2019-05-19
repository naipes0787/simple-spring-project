package wolox.training;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.NestedServletException;
import wolox.training.controllers.BookController;
import wolox.training.models.Book;
import wolox.training.repositories.BookRepository;
import wolox.training.repositories.UserRepository;
import wolox.training.services.BookService;
import wolox.training.services.CustomUserDetailsService;
import wolox.training.services.UserService;

@RunWith(SpringRunner.class)
@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    @SuppressWarnings("unused")
    private MockMvc mvc;

    @MockBean
    @SuppressWarnings("unused")
    private BookRepository mockBookRepository;

    @MockBean
    @SuppressWarnings("unused")
    private UserRepository mockUserRepository;

    @MockBean
    @SuppressWarnings("unused")
    private BookService mockBookService;

    @MockBean
    @SuppressWarnings("unused")
    private UserService mockUserService;

    @MockBean
    @SuppressWarnings("unused")
    private CustomUserDetailsService mockCustomUserDetailsService;

    private Book oneTestBook;

    @Before
    public void setUp() {
        oneTestBook = new Book();
        oneTestBook.setAuthor("Carlitos");
        oneTestBook.setGenre("Terror");
        oneTestBook.setImage("unaImagen");
        oneTestBook.setIsbn("4578-8665");
        oneTestBook.setPages(259);
        oneTestBook.setPublisher("LaGuitarra");
        oneTestBook.setSubtitle("CarlitosWay");
        oneTestBook.setTitle("Las aventuras terrorificas de Carlitos");
        oneTestBook.setYear("2005");
    }

    @Test
    public void whenPostBook_thenBookIsCreated() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(oneTestBook);

        mvc.perform(post("/api/books")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson))
            .andExpect(status().isCreated());
    }

    @WithMockUser
    @Test
    public void whenFindByIdWhichNotExists_thenNotFound() throws Exception {
        String url = ("/api/books/100");
        mvc.perform(get(url)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @WithMockUser
    @Test
    public void whenDeleteNotExistingBook_thenNotFound() throws Exception {
        String url = ("/api/books/100");
        mvc.perform(delete(url)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @WithMockUser
    @Test(expected = NestedServletException.class)
    public void whenPutBookWithDifferentId_thenBookIdMismatchException() throws Exception {
        String url = ("/api/books/100");
        String newYear = "2010";
        String newTitle = "Las NUEVAS aventuras terrorificas de Carlitos";
        oneTestBook.setTitle(newTitle);
        oneTestBook.setYear(newYear);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(oneTestBook);
        mvc.perform(put(url)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson))
            .andExpect(status().isNotFound());
    }

    @Test
    public void whenGetWithoutUser_thenUnauthorized() throws Exception {
        String url = ("/api/books/100");
        mvc.perform(get(url)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized());
    }

    @Test
    public void whenDeleteWithoutUser_thenUnauthorized() throws Exception {
        String url = ("/api/books/100");
        mvc.perform(delete(url)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isUnauthorized());
    }

    @Test
    public void whenPutWithoutUser_thenUnauthorized() throws Exception {
        String url = ("/api/books/100");
        String newYear = "2010";
        String newTitle = "Las NUEVAS aventuras terrorificas de Carlitos";
        oneTestBook.setTitle(newTitle);
        oneTestBook.setYear(newYear);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        String requestJson = ow.writeValueAsString(oneTestBook);
        mvc.perform(put(url)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson))
            .andExpect(status().isUnauthorized());
    }

    @WithMockUser
    @Test
    public void whenFindByEveryFieldAndExists_thenReturnOk() throws Exception {
        Pageable customPageable = PageRequest.of(0, 5, Sort.by("title"));
        List<Book> books = new ArrayList<>();
        books.add(oneTestBook);
        when(mockBookRepository.findAllByEveryField("1", "Terror", "Carlitos",
            "unaImagen", "Las aventuras terrorificas de Carlitos",
            "CarlitosWay", "LaGuitarra", "2005", "2005",
            "259", "4578-8665", customPageable)).thenReturn(Optional.of(books));
        String url = ("/api/books?id=1&genre=Terror&author=Carlitos&image=unaImagen&"
            + "title=Las aventuras terrorificas de Carlitos&subtitle=CarlitosWay&"
            + "publisher=LaGuitarra&fromYear=2005&toYear=2005&pages=259&isbn=4578-8665&"
            + "page=0&size=5&sort=title");
        mvc.perform(get(url)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @WithMockUser
    @Test
    public void whenFindAndNotExists_thenReturnNotFound() throws Exception {
        Pageable customPageable = PageRequest.of(0, 5, Sort.by("title"));
        List<Book> books = new ArrayList<>();
        books.add(oneTestBook);
        when(mockBookRepository.findAllByEveryField("1", "Terror", "Carlitos",
            "unaImagen", "Las aventuras terrorificas de Carlitos",
            "CarlitosWay", "LaGuitarra", "2005", "2005",
            "259", "4578-8665", customPageable)).thenReturn(Optional.of(books));
        String url = ("/api/books?genre=Comedia&author=Carlitos");
        mvc.perform(get(url)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

}
