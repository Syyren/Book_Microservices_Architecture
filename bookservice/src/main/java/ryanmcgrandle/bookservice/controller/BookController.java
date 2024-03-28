package ryanmcgrandle.bookservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ryanmcgrandle.bookservice.model.Book;
import ryanmcgrandle.bookservice.model.Quantity;
import ryanmcgrandle.bookservice.model.Title;
import ryanmcgrandle.bookservice.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//allowing cross-origin connection
@CrossOrigin(origins = "*", allowedHeaders = "*")
//routing all the maps to be under this parent map
@RequestMapping("/api/book")
public class BookController
{
    //injecting bookService bean to interface with the controller and repository
    private final BookService bookService;
    @Autowired
    public BookController(BookService bookService)
    {
        this.bookService = bookService;
    }

    //inserts a new achievement into the "db" via the service
    @PostMapping("/create")
    @Operation(summary = "Makes a new Book.")
    public ResponseEntity<?> createBook(@RequestBody Book book)
    {
        try
        {
            bookService.saveBook(book);
            return ResponseEntity.ok(book);
        }
        catch (RuntimeException e)
        {
            String errorMessage = "Failed to create the book: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }
    //gets all books in the db from the service
    @GetMapping("/get/all")
    @Operation(summary = "Gets a list of Books from the Database.")
    public ResponseEntity<?> getAllBooks()
    {
        List <Book> booklist = bookService.getAllBooks();
        if (booklist.isEmpty())
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No books found!");
        }
        return ResponseEntity.ok(booklist);
    }
    //endpoint that gets a book from the db via its id from the service
    @GetMapping("/get/{bookId}")
    @Operation(summary = "Gets a Book from the Database via its id")
    public ResponseEntity<?> getBookById(@PathVariable Long bookId)
    {
        try
        {
            Book book = bookService.getById(bookId);
            return ResponseEntity.ok(book);
        }
        catch (RuntimeException e)
        {
            String errorMessage = e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }
    //endpoint that gets a book by the title in the request body
    @PostMapping("/get/title")
    @Operation(summary = "Gets a Book from the Database via its title")
    public ResponseEntity<?> getBookByTitle(@RequestBody Title title)
    {
        try
        {
            Book book = bookService.getByTitle(title.getText());
            return ResponseEntity.ok(book);
        }
        catch (RuntimeException e)
        {
            String errorMessage = e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }
    //endpoint that updates a book that already exists in the db by id through the service
    @PutMapping("/update/{bookId}")
    @Operation(summary = "Updates a Book in the Database via its id")
    public ResponseEntity<?> updateBook(@PathVariable Long bookId, @RequestBody Book updatedBook)
    {
        try
        {
            Book book = bookService.getById(bookId);
            updatedBook.setBookId(book.getBookId());
            bookService.updateBook(updatedBook);
            return ResponseEntity.ok(updatedBook);
        }
        catch (RuntimeException e)
        {
            String errorMessage = e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }
    //endpoints that update a books quantity via its id in the db
    @PutMapping("/restock/{bookId}")
    @Operation(summary = "Specifically handles adding to a Book's qty via its id.")
    public ResponseEntity<?> restockBook(@PathVariable Long bookId, @RequestBody Quantity amount)
    {
        try
        {
            int qty = amount.getQty();
            Book book = bookService.getById(bookId);
            bookService.stockBook(book, qty);
            return ResponseEntity.ok(book);
        }
        catch (RuntimeException e)
        {
            String errorMessage = e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }
    @PutMapping("/sell/{bookId}")
    @Operation(summary = "Specifically handles subtracting from a Book's qty via its id.")
    public ResponseEntity<?> sellBook(@PathVariable Long bookId, @RequestBody Quantity amount)
    {
        try
        {
            int qty = amount.getQty();
            Book book = bookService.getById(bookId);
            bookService.sellBook(book, qty);
            return ResponseEntity.ok(book);
        }
        catch (RuntimeException e)
        {
            String errorMessage = e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }
    //endpoint that deletes an achievement from the "db" by id via the service
    @DeleteMapping("/delete/{bookId}")
    @Operation(summary = "Deletes a Book from the Database via its id.")
    public ResponseEntity<?> deleteBook(@PathVariable Long bookId)
    {
        try
        {
            Book book = bookService.getById(bookId);
            bookService.deleteBook(book);
            return ResponseEntity.ok("Deleted Successfully!");
        }
        catch (RuntimeException e)
        {
            String errorMessage = e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }
}