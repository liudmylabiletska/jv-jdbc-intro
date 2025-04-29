package mate.academy;



import mate.academy.lib.Injector;

import java.awt.print.Book;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static java.lang.System.*;

public class Main {
    private static Injector Injector;
    private static final Injector injector = Injector.getInstance("your.package.name");

    public static void main(String[] args) {
        BookDao bookDao = (BookDao) injector.getInstance(BookDao.class);

        // Create a new book
        mate.academy.model.Book newBook = new mate.academy.model.Book();
        mate.academy.model.Book createdBook = bookDao.create(newBook);
        out.println("Created Book: " + createdBook);

        // Find by ID
        Optional<Book> foundBookOpt = (Optional<Book>) bookDao.findAll();
        foundBookOpt.ifPresent(book -> out.println("Found Book: " + book));

        // Find all books
        Collection<Book> books = (Collection<Book>) bookDao.findAll(); // assuming findAll() returns a Collection<Book>
        out.println("All Books:");
        for (Book book : books) {
            out.println(book);
        }



        // Update book
        createdBook.setTitle("Effective Java, 3rd Edition");
        createdBook.setPrice(new BigDecimal("50.00"));
        Book updatedBook = bookDao.update(createdBook);
        out.println("Updated Book: " + updatedBook);

        // Delete by ID
        boolean isDeleted = false;
        try {
            bookDao.deleteById(updatedBook.getClass());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        out.println("Deleted: " + isDeleted);
    }
}
