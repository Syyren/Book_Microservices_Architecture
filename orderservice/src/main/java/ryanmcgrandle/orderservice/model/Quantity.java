package ryanmcgrandle.orderservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//Making a Java class to handle buying and selling through the request body.
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Quantity
{
    private int qty;
}