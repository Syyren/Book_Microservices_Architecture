package ryanmcgrandle.bookservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ryanmcgrandle.bookservice.model.Book;
import ryanmcgrandle.bookservice.service.BookService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

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

        mockMvc.perform(MockMvcRequestBuilders.get("/api/book/get/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
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

        mockMvc.perform(MockMvcRequestBuilders.get("/api/book/get/" + book.getBookId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.bookId").value(book.getBookId()))
                .andExpect(jsonPath("$.title").value(book.getTitle()))
                .andExpect(jsonPath("$.description").value(book.getDescription()))
                .andExpect(jsonPath("$.authorName").value(book.getAuthorName()))
                .andExpect(jsonPath("$.qty").value(book.getQty()));
    }
}
