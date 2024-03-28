package ryanmcgrandle.orderservice.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ryanmcgrandle.orderservice.model.Book;
import ryanmcgrandle.orderservice.model.Quantity;

import java.util.Arrays;
import java.util.List;

@Component
public class BookClient
{
    private final RestTemplate restTemplate;
    private final String URL = "http://localhost:8081/api/book";

    @Autowired
    public BookClient(RestTemplate restTemplate)
    {
        this.restTemplate = restTemplate;
    }

    public List<Book> getAllBooks()
    {
        String url = URL + "/get/all";
        ResponseEntity<Book[]> responseEntity = restTemplate.getForEntity(url, Book[].class);
        Book[] booksArray = responseEntity.getBody();
        return Arrays.asList(booksArray);
    }
    public String getBookTitle(Long bookId)
    {
        String url = URL + "/get/" + bookId;
        Book book = restTemplate.getForObject(url, Book.class);
        return book.getTitle();
    }

    public int getBookStock(Long bookId)
    {
        String url = URL + "/get/" + bookId;
        Book book = restTemplate.getForObject(url, Book.class);
        return book.getQty();
    }
    public void sellBook(Long bookId, Quantity amount)
    {
        String url = URL + "/sell/" + bookId;
        restTemplate.put(url, amount);
    }

    public void restockBook(Long bookId, Quantity amount)
    {
        String url = URL + "/restock/" + bookId;
        restTemplate.put(url, amount);
    }
}
