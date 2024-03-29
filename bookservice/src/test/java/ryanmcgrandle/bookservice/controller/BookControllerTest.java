package ryanmcgrandle.bookservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ryanmcgrandle.bookservice.model.Book;
import ryanmcgrandle.bookservice.model.Quantity;
import ryanmcgrandle.bookservice.service.BookService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
public class BookControllerTest
{
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @InjectMocks
    private BookController bookController;
    //testing the createBook method
    @Test
    public void testCreateBook() throws Exception {
        Book book = new Book(
                1L,
                "Test1",
                "Desc1",
                "Auth1",
                10);

        Mockito.doNothing().when(bookService).saveBook(book);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/book/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookId").value(book.getBookId()))
                .andExpect(jsonPath("$.title").value(book.getTitle()))
                .andExpect(jsonPath("$.description").value(book.getDescription()))
                .andExpect(jsonPath("$.authorName").value(book.getAuthorName()))
                .andExpect(jsonPath("$.qty").value(book.getQty()));
    }
    //testing for the case of an empty db when using the getAllBooks method
    @Test
    public void testGetAllBooks_EmptyList() throws Exception
    {
        Mockito.when(bookService.getAllBooks()).thenReturn(new ArrayList<>());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/book/get/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("text/plain;charset=UTF-8"))
                .andExpect(content().string(Matchers.containsString("No books found!")));
    }
    //testing for when there are books in the list
    @Test
    public void testGetAllBooks_Populated() throws Exception
    {
        Book book1 = new Book(
                1L,
                "Book1",
                "Description1",
                "Author1",
                5);
        Book book2 = new Book(
                2L,
                "Book2",
                "Description2",
                "Author2",
                8);
        List<Book> booksList = Arrays.asList(book1, book2);

        when(bookService.getAllBooks()).thenReturn(booksList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/book/get/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].bookId").value(book1.getBookId()))
                .andExpect(jsonPath("$[0].title").value(book1.getTitle()))
                .andExpect(jsonPath("$[0].description").value(book1.getDescription()))
                .andExpect(jsonPath("$[0].authorName").value(book1.getAuthorName()))
                .andExpect(jsonPath("$[0].qty").value(book1.getQty()))
                .andExpect(jsonPath("$[1].bookId").value(book2.getBookId()))
                .andExpect(jsonPath("$[1].title").value(book2.getTitle()))
                .andExpect(jsonPath("$[1].description").value(book2.getDescription()))
                .andExpect(jsonPath("$[1].authorName").value(book2.getAuthorName()))
                .andExpect(jsonPath("$[1].qty").value(book2.getQty()));
    }
    //testing getting a book with a valid Id
    @Test
    public void testGetBookById_ValidId() throws Exception
    {
        Book book = new Book(
                1L,
                "Test1",
                "Desc1",
                "Auth1",
                10);

        Mockito.when(bookService.getById(1L)).thenReturn(book);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/book/get/" + book.getBookId())
                    .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookId").value(book.getBookId()))
                .andExpect(jsonPath("$.title").value(book.getTitle()))
                .andExpect(jsonPath("$.description").value(book.getDescription()))
                .andExpect(jsonPath("$.authorName").value(book.getAuthorName()))
                .andExpect(jsonPath("$.qty").value(book.getQty()));
    }
    //testing the error raising when the get has an invalid id
    @Test
    public void testGetBookById_InvalidId() throws Exception
    {
        Long id = 420L;

        Mockito.when(bookService.getById(id)).thenThrow(new RuntimeException("Book not found"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/book/get/" + id))
                .andExpect(status().isInternalServerError());
    }
    //testing the update book method
    @Test
    public void testUpdateBook() throws Exception
    {
        Book book = new Book(
                1L,
                "Test1",
                "Desc1",
                "Auth1",
                10);
        Book updatedBook = new Book(
                1L,
                "UpdatedTest1",
                "UpdatedDesc1",
                "UpdatedAuth1",
                15);

        Mockito.when(bookService.getById(1L)).thenReturn(book);
        Mockito.doNothing().when(bookService).updateBook(Mockito.any(Book.class));

        mockMvc.perform(MockMvcRequestBuilders.put("/api/book/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedBook)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(updatedBook.getTitle()))
                .andExpect(jsonPath("$.description").value(updatedBook.getDescription()))
                .andExpect(jsonPath("$.authorName").value(updatedBook.getAuthorName()))
                .andExpect(jsonPath("$.qty").value(updatedBook.getQty()));
    }
    //testing the restock book method
    @Test
    public void testRestockBook() throws Exception
    {
        Book book = new Book(
                1L,
                "Test1",
                "Desc1",
                "Auth1",
                10);

        Long bookId = book.getBookId();
        Quantity quantity = new Quantity(2);

        Mockito.when(bookService.getById(bookId)).thenReturn(book);

        Mockito.doAnswer(invocation ->
        {
            Book updatedBook = invocation.getArgument(0);
            int quantityToAdd = invocation.getArgument(1);
            updatedBook.setQty(updatedBook.getQty() + quantityToAdd);
            return null;
        }).when(bookService).stockBook(Mockito.any(Book.class), Mockito.anyInt());

        mockMvc.perform(MockMvcRequestBuilders.put("/api/book/restock/" + bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(quantity)))
                .andExpect(status().isOk())
                .andExpect((jsonPath("$.qty").value(12)));
    }
    //testing the sell book method
    @Test
    public void testSellBook() throws Exception
    {
        Book book = new Book(
                1L,
                "Test1",
                "Desc1",
                "Auth1",
                10);

        Long bookId = book.getBookId();
        Quantity quantity = new Quantity(3);

        Mockito.when(bookService.getById(bookId)).thenReturn(book);

        Mockito.doAnswer(invocation ->
        {
            Book updatedBook = invocation.getArgument(0);
            int quantityToSubtract = invocation.getArgument(1);
            updatedBook.setQty(updatedBook.getQty() - quantityToSubtract);
            return null;
        }).when(bookService).sellBook(Mockito.any(Book.class), Mockito.anyInt());

        mockMvc.perform(MockMvcRequestBuilders.put("/api/book/sell/" + bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(quantity)))
                .andExpect(status().isOk())
                .andExpect((jsonPath("$.qty").value(7)));
    }
    //testing that the delete book method properly deletes a book
    @Test
    public void testDeleteBook() throws Exception
    {
        Long bookId = 1L;

        Mockito.when(bookService.getById(bookId)).thenReturn(new Book());
        Mockito.doNothing().when(bookService).deleteBook(Mockito.any(Book.class));

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/book/delete/" + bookId)
                        .contentType("text/plain;charset=UTF-8"))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("Deleted Successfully!")));
    }
}
