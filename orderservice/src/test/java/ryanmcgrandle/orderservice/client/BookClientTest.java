package ryanmcgrandle.orderservice.client;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ryanmcgrandle.orderservice.model.Book;
import ryanmcgrandle.orderservice.model.Title;
import ryanmcgrandle.orderservice.model.Quantity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

//testing all of my functions in the BookClient class
@SpringBootTest
public class BookClientTest
{

    @Test
    public void testGetBookByTitle()
    {
        RestTemplate mockRestTemplate = Mockito.mock(RestTemplate.class);
        Title title = new Title("Title");
        Book expectedBook = new Book(
                1L,
                "Title",
                "Desc",
                "Author",
                10);

        Mockito.when(
                mockRestTemplate.postForObject(
                        Mockito.anyString(),
                        Mockito.any(),
                        Mockito.eq(Book.class)))
                .thenReturn(expectedBook);

        BookClient bookClient = new BookClient(mockRestTemplate);
        Book resultBook = bookClient.getBookByTitle(title);

        assertEquals(expectedBook, resultBook);
    }
    @Test
    public void testGetAllBooks() {
        RestTemplate mockRestTemplate = Mockito.mock(RestTemplate.class);
        Book[] expectedBooksArray =
                {
                    new Book(
                            1L,
                            "Title",
                            "Desc",
                            "Author",
                            10),
                    new Book(2L,
                            "Title2",
                            "Desc2",
                            "Author2",
                            12)
                };

        Mockito.when(
                mockRestTemplate.getForEntity(
                        Mockito.anyString(),
                        Mockito.eq(Book[].class)))
                .thenReturn(new ResponseEntity<>(
                        expectedBooksArray,
                        HttpStatus.OK));

        BookClient bookClient = new BookClient(mockRestTemplate);
        List<Book> resultBooksList = bookClient.getAllBooks();

        assertEquals(Arrays.asList(expectedBooksArray), resultBooksList);
    }

    @Test
    public void testGetBookTitle()
    {
        RestTemplate mockRestTemplate = Mockito.mock(RestTemplate.class);
        Long bookId = 1L;
        Book expectedBook = new Book(
                1L,
                "Title",
                "Desc",
                "Author",
                10);

        Mockito.when(
                mockRestTemplate.getForObject(
                                Mockito.anyString(),
                                Mockito.eq(Book.class)))
                .thenReturn(expectedBook);

        BookClient bookClient = new BookClient(mockRestTemplate);
        String resultTitle = bookClient.getBookTitle(bookId);

        assertEquals(expectedBook.getTitle(), resultTitle);
    }

    @Test
    public void testGetBookStock() {
        RestTemplate mockRestTemplate = Mockito.mock(RestTemplate.class);
        Long bookId = 1L;
        Book expectedBook = new Book(
                1L,
                "Title",
                "Desc",
                "Author",
                10);

        Mockito.when(
                mockRestTemplate.getForObject(
                        Mockito.anyString(),
                        Mockito.eq(Book.class)))
                .thenReturn(expectedBook);

        BookClient bookClient = new BookClient(mockRestTemplate);
        int resultStock = bookClient.getBookStock(bookId);

        assertEquals(expectedBook.getQty(), resultStock);
    }

    @Test
    public void testSellBook() {
        RestTemplate mockRestTemplate = Mockito.mock(RestTemplate.class);
        Long bookId = 1L;
        Quantity amount = new Quantity(2);

        BookClient bookClient = new BookClient(mockRestTemplate);
        bookClient.sellBook(bookId, amount);

        Mockito.verify(mockRestTemplate).put(
                Mockito.anyString(),
                Mockito.eq(amount));
    }

    @Test
    public void testRestockBook() {
        RestTemplate mockRestTemplate = Mockito.mock(RestTemplate.class);
        Long bookId = 1L;
        Quantity amount = new Quantity(5);

        BookClient bookClient = new BookClient(mockRestTemplate);
        bookClient.restockBook(bookId, amount);

        Mockito.verify(mockRestTemplate).put(
                Mockito.anyString(),
                Mockito.eq(amount));
    }
}