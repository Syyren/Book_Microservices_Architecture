package ryanmcgrandle.orderservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//using lombok to easily and cleanly construct my class
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book
{
    private Long bookId;
    private String title;
    private String description;
    private String authorName;
    private int qty;
}