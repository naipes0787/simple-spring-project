package wolox.training.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import wolox.training.models.Book;

@Repository
public interface BookRepository extends CrudRepository<Book, Long> {

    Optional<Book> findFirstByAuthorIgnoreCase(String author);

    Optional<Book> findByIsbn(String isbn);

    @Query(
        "SELECT b FROM Book b WHERE (b.genre = :genre OR :genre = '') AND "
            + " (b.author = :author OR :author = '') AND (b.image = :image OR :image = '') AND "
            + " (b.title = :title OR :title = '') AND (b.subtitle = :subtitle OR :subtitle = '') AND "
            + " (b.publisher = :publisher OR :publisher = '') AND "
            + " ("
            + "     (b.year BETWEEN :fromYear AND :toYear)"
            + "     OR ( :fromYear = '' AND b.year <= :toYear )"
            + "     OR ( :toYear = '' AND b.year >= :fromYear )"
            + "     OR ( :toYear = '' AND :fromYear  = '' )"
            + " ) AND "
            + " (:pages = '' OR b.pages = CAST(:pages as int) ) AND "
            + "(b.isbn = :isbn OR :isbn = '') AND  (:id = '' OR b.id = CAST(:id as int))")
    Optional<List<Book>> findAllByEveryField(@Param("id") String id, @Param("genre") String genre,
        @Param("author") String author, @Param("image") String image, @Param("title") String title,
        @Param("subtitle") String subtitle, @Param("publisher") String publisher,
        @Param("fromYear") String fromYear, @Param("toYear") String toYear,
        @Param("pages") String pages, @Param("isbn") String isbn, Pageable pageable);

}
