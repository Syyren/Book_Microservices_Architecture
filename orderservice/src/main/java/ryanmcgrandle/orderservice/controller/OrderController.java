package ryanmcgrandle.orderservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ryanmcgrandle.orderservice.client.BookClient;
import ryanmcgrandle.orderservice.model.Book;
import ryanmcgrandle.orderservice.model.Order;
import ryanmcgrandle.orderservice.model.Quantity;
import ryanmcgrandle.orderservice.model.Title;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController
{
    @Autowired
    private BookClient bookClient;

    @GetMapping("/view/all")
    @Operation(summary = "Gets a list of Books from the Microservice.")
    public ResponseEntity<?> getAllBooks()
    {
        try
        {
            List<Book> booklist = bookClient.getAllBooks();
            return ResponseEntity.ok(booklist);
        }
        catch (RuntimeException e)
        {
            String errorMessage = e.getMessage();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
        }
    }

    @PostMapping("/view/title")
    @Operation(summary = "Gets a Book from the Microservice by its title.")
    public ResponseEntity<?> getBookByTitle(@RequestBody Title title)
    {
        try
        {
            Book book = bookClient.getBookByTitle(title);
            return ResponseEntity.ok(book);
        }
        catch (RuntimeException e)
        {
            String errorMessage = e.getMessage();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
        }
    }

    @PutMapping("/sell/{bookId}")
    @Operation(summary = "Sells a book from the microservice.")
    public ResponseEntity<?> sellBook(@PathVariable Long bookId, @RequestBody Quantity amount)
    {
        try {
            bookClient.sellBook(bookId, amount);
            Order sellOrder = new Order(
                    bookClient.getBookTitle(bookId),
                    amount.getQty(),
                    "Purchased!",
                    bookClient.getBookStock(bookId));
            return ResponseEntity.ok(sellOrder);
        }
        catch (RuntimeException e)
        {
            String errorMessage = e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }

    @PutMapping("/restock/{bookId}")
    @Operation(summary = "Restocks a book to the microservice.")
    public ResponseEntity<?> restockBook(@PathVariable Long bookId, @RequestBody Quantity amount)
    {
        try
        {
            bookClient.restockBook(bookId, amount);
            Order sellOrder = new Order(
                    bookClient.getBookTitle(bookId),
                    amount.getQty(),
                    "Restocked!",
                    bookClient.getBookStock(bookId));
            return ResponseEntity.ok(sellOrder);
        }
        catch (Exception e)
        {
            String errorMessage = e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }
}
