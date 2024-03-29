package ryanmcgrandle.orderservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ryanmcgrandle.orderservice.client.BookClient;
import ryanmcgrandle.orderservice.model.Book;
import ryanmcgrandle.orderservice.model.Quantity;
import ryanmcgrandle.orderservice.model.Title;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//declaring the test for use on the OrderController
@WebMvcTest(OrderController.class)
public class OrderControllerTest
{
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookClient bookClient;

    @Test
    public void testGetAllBooks() throws Exception
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

        when(bookClient.getAllBooks()).thenReturn(booksList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/order/view/all"))
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
    public void testGetBookByTitle() throws Exception
    {
        Long bookId = 1L;
        Title title = new Title("Book1");
        Book book = new Book(
                bookId,
                title.getText(),
                "Desc1",
                "Auth1",
                10);

        when(bookClient.getBookByTitle(title)).thenReturn(book);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/order/view/title")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(title)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookId").value(book.getBookId()));
    }

    @Test
    public void testSellBook() throws Exception
    {
        String successMsg = "Purchased!";
        Long bookId = 1L;
        Quantity amount = new Quantity(2);

        Mockito.doNothing().when(bookClient).sellBook(bookId, amount);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/order/sell/{bookId}", bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(amount)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.qty").value(amount.getQty()))
                .andExpect(jsonPath("$.msg").value(successMsg));
    }

    @Test
    public void testRestockBook() throws Exception
    {
        String successMsg = "Restocked!";
        Long bookId = 1L;
        Quantity amount = new Quantity(2);

        Mockito.doNothing().when(bookClient).restockBook(bookId, amount);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/order/restock/{bookId}", bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(amount)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.qty").value(amount.getQty()))
                .andExpect(jsonPath("$.msg").value(successMsg));
    }
}
