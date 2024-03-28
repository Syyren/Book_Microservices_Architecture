package ryanmcgrandle.bookservice.controller;

import org.springframework.http.ResponseEntity;
import ryanmcgrandle.bookservice.model.Book;
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
    public ResponseEntity<Book> createBook(@RequestBody Book book)
    {
        bookService.saveBook(book);
        return ResponseEntity.ok(book);
    }
    //gets all books in the db from the service
    @GetMapping("/get/all")
    public List<Book> getAllBooks()
    {
        return bookService.getAllBooks();
    }
    //endpoint that gets an achievement from the "db" via its id from the service
    @GetMapping("/get/{bookId}")
    public ResponseEntity<Book> getBookById(@PathVariable Long bookId)
    {
        Book book = bookService.getById(bookId);
        return ResponseEntity.ok(book);
    }
    //endpoint that updates a book that already exists in the db by id through the service
    @PutMapping("/update/{bookId}")
    public ResponseEntity<Book> updateBook(@PathVariable Long bookId, @RequestBody Book updatedBook)
    {
        Book book = bookService.getById(bookId);
        updatedBook.setBookId(book.getBookId());
        bookService.updateBook(updatedBook);
        return ResponseEntity.ok(updatedBook);
    }
    //endpoints that update a books quantity via its id in the db
    @PutMapping("/restock/{bookId}")
    public ResponseEntity<Book> restockBook(@PathVariable Long bookId, @RequestBody int qty)
    {
        Book book = bookService.getById(bookId);
        bookService.stockBook(book, qty);
        return ResponseEntity.ok(book);
    }
    @PutMapping("/sell/{bookId}")
    public ResponseEntity<Book> sellBook(@PathVariable Long bookId, @RequestBody int qty)
    {
        Book book = bookService.getById(bookId);
        bookService.sellBook(book, qty);
        return ResponseEntity.ok(book);
    }
    //endpoint that deletes an achievement from the "db" by id via the service
    @DeleteMapping("/delete/{bookId}")
    public ResponseEntity<String> deleteBook(@PathVariable Long bookId)
    {
        Book book = bookService.getById(bookId);
        bookService.deleteBook(book);
        return ResponseEntity.ok("Deleted Successfully!");
    }
}