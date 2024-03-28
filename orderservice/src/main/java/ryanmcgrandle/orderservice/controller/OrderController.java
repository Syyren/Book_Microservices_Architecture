package ryanmcgrandle.orderservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ryanmcgrandle.orderservice.client.BookClient;
import ryanmcgrandle.orderservice.model.Book;
import ryanmcgrandle.orderservice.model.Order;
import ryanmcgrandle.orderservice.model.Quantity;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController
{
    @Autowired
    private BookClient bookClient;

    @GetMapping("/view/all")
    public List<Book> getAllBooks()
    {
        return bookClient.getAllBooks();
    }
    @PutMapping("/sell/{bookId}")
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
