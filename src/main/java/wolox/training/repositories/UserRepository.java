package wolox.training.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import wolox.training.models.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findFirstByUsername(String userName);

    @Query("SELECT u FROM User u WHERE u.birthdate BETWEEN :dateStart AND :dateEnd"
        + " AND ( :name = '' OR lower(u.name) LIKE lower(concat('%', :name,'%')) )")
    Optional<List<User>> findAllByBirthdateBetweenAndNameContainingIgnoreCase(
        @Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd,
        @Param("name") String name, Pageable pageable);

}
