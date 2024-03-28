package ryanmcgrandle.bookservice.service;

import ryanmcgrandle.bookservice.model.Book;
import ryanmcgrandle.bookservice.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService
{
    private final BookRepository bookRepository;
    @Autowired
    public BookService(BookRepository bookRepository)
    {
        this.bookRepository = bookRepository;
    }

    //method that passes the book object to the repository and saves it
    public void saveBook(Book book)
    {
        bookRepository.save(book);
    }
    //passes an updated book with values to the repository
    public void updateBook(Book book)
    {
        bookRepository.save(book);
    }
    //method that adds a qty of books to a given book
    public void stockBook(Book book, int Qty)
    {
        book.setQty(book.getQty() + Qty);
        bookRepository.save(book);
    }
    public void sellBook(Book book, int Qty)
    {
        if (book.getQty() == 0) { throw new RuntimeException("Book is Sold Out!"); }
        else if (book.getQty() < Qty) { throw new RuntimeException("Not enough in Stock!"); }
        book.setQty(book.getQty() - Qty);
        bookRepository.save(book);
    }
    //method that pushes an id to the repository to delete a matching book
    public void deleteBook(Book book)
    {
        bookRepository.delete(book);
    }
    //method that passes the id to the repository to pull a matching object
    public Book getById(Long id)
    {
        Optional<Book> optionalBook = bookRepository.findById(id);
        if (optionalBook.isPresent()) { return optionalBook.get(); }
        else { throw new RuntimeException("Book not found with ID: " + id); }
    }
    public Book getByTitle(String title)
    {
        Optional<Book> optionalBook = Optional.ofNullable(bookRepository.findByTitleIgnoreCase(title));
        if (optionalBook.isPresent()) { return optionalBook.get(); }
        else { throw new RuntimeException("Book not found with title: " + title); }
    }
    //method that pulls all book objects from the repository
    public List<Book> getAllBooks()
    {
        return bookRepository.findAll();
    }
}
