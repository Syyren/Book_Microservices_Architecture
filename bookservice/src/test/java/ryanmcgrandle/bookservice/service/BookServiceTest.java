package ryanmcgrandle.bookservice.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Optional;
import ryanmcgrandle.bookservice.model.Book;
import ryanmcgrandle.bookservice.repository.BookRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest
public class BookServiceTest
{
    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    //testing the save book function in the service
    @Test
    public void testSaveBook()
    {
        Book book = new Book(
                1L,
                "Test1",
                "Desc1",
                "Auth1",
                10);

        when(bookRepository.save(book)).thenReturn(null);
        bookService.saveBook(book);
        verify(bookRepository, times(1)).save(book);
        assertEquals("Test1", book.getTitle());
        assertEquals("Desc1", book.getDescription());
        assertEquals("Auth1", book.getAuthorName());
        assertEquals(10, book.getQty());
    }
    @Test
    public void testUpdateBook()
    {
        Book book = new Book(
                1L,
                "Test1",
                "Desc1",
                "Auth1",
                10);

        when(bookRepository.save(book)).thenReturn(null);
        bookService.updateBook(book);
        verify(bookRepository, times(1)).save(book);
        assertEquals("Test1", book.getTitle());
        assertEquals("Desc1", book.getDescription());
        assertEquals("Auth1", book.getAuthorName());
        assertEquals(10, book.getQty());
    }
    @Test
    public void testStockBook()
    {
        Book book = new Book(
                1L,
                "Test1",
                "Desc1",
                "Auth1",
                10);

        when(bookRepository.save(book)).thenReturn(null);

        bookService.stockBook(book, 5);
        assertEquals(15, book.getQty());
        verify(bookRepository, times(1)).save(book);
    }
    @Test
    public void testSellBook()
    {
        Book book = new Book(
                1L,
                "Test1",
                "Desc1",
                "Auth1",
                10);
        when(bookRepository.save(book)).thenReturn(null);

        bookService.sellBook(book, 7);
        assertEquals(3, book.getQty());
        verify(bookRepository, times(1)).save(book);
    }
    @Test
    public void testDeleteBook()
    {
        Book book = new Book(
                1L,
                "Test1",
                "Desc1",
                "Auth1",
                10);
        doNothing().when(bookRepository).delete(book);
        bookService.deleteBook(book);
        verify(bookRepository, times(1)).delete(book);
    }

    @Test
    public void testGetById()
    {
        Long bookId = 1L;

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(new Book()));
        Book book = bookService.getById(bookId);
        verify(bookRepository, times(1)).findById(bookId);
        assertNotNull(book);
    }
}
