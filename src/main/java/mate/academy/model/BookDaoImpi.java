package mate.academy.model;


import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookDaoImpi implements BookDao {
    private static final String INSERT_SQL = "INSERT INTO books (title, price) VALUES (?, ?)";
    private static final String SELECT_BY_ID_SQL = "SELECT id, title, price FROM books WHERE id = ?";
    private static final String SELECT_ALL_SQL = "SELECT id, title, price FROM books";
    private static final String UPDATE_SQL = "UPDATE books SET title = ?, price = ? WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM books WHERE id = ?";

    @Override
    public Book create(Book book) throws DataProcessingException {
        try (Connection connection = DbConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, book.getTitle());
            statement.setBigDecimal(2, book.getPrice());
            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new DataProcessingException("Creating book failed, no rows affected.", null);
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    book.setId(generatedKeys.getLong(1));
                } else {
                    throw new DataProcessingException("Creating book failed, no ID obtained.", null);
                }
            }
            return book;
        } catch (SQLException e) {
            throw new DataProcessingException("Error creating book", e);
        }
    }

    @Override
    public Optional<Book> findById(Long id) {
        try (Connection connection = DbConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_BY_ID_SQL)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(parseResultSet(resultSet));
                }
            }
            return Optional.empty();
        } catch (SQLException e) {
            try {
                throw new DataProcessingException("Error finding book by id", e);
            } catch (DataProcessingException ex) {
                throw new RuntimeException(ex);
            }
        } catch (DataProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Book> findAll() throws DataProcessingException {
        List<Book> books = new ArrayList<>();
        try (Connection connection = DbConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ALL_SQL);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                books.add(parseResultSet(resultSet));
            }
            return books;
        } catch (SQLException e) {
            throw new DataProcessingException("Error retrieving all books", e);
        } catch (DataProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Book update(Book book) throws DataProcessingException {
        try (Connection connection = DbConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_SQL)) {
            statement.setString(1, book.getTitle());
            statement.setBigDecimal(2, book.getPrice());
            statement.setLong(3, book.getId());
            int affectedRows = statement.executeUpdate();

            if (affectedRows == 0) {
                throw new DataProcessingException("Updating book failed, no rows affected.", null);
            }
            return book;
        } catch (SQLException e) {
            throw new DataProcessingException("Error updating book", e);
        } catch (DataProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean deleteById(Long id) throws DataProcessingException {
        try (Connection connection = DbConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_SQL)) {
            statement.setLong(1, id);
            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            throw new DataProcessingException("Error deleting book", e);
        } catch (DataProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private Book parseResultSet(ResultSet resultSet) throws SQLException {
        Long id = resultSet.getLong("id");
        String title = resultSet.getString("title");
        BigDecimal price = resultSet.getBigDecimal("price");
        return new Book(id, title, price);
    }
}
