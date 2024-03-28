package ryanmcgrandle.orderservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//using lombok to easily and cleanly construct my class
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order
{
    private String bookTitle;
    private int qty;
    private String msg;
    private int leftInStock;
}