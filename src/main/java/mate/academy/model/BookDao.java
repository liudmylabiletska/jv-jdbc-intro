package mate.academy.model;

import java.util.List;
import java.util.Optional;

public interface BookDao {
    Book create(Book book) throws DataProcessingException;

    Optional<Book> findById(Long id);

    List<Book> findAll() throws DataProcessingException;

    Book update(Book book) throws DataProcessingException;

    boolean deleteById(Long id) throws DataProcessingException;
}
