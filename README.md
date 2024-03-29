# Microservices_Architecture
### Assignment 3 for CPRO2501

## BookService

### What is it?

The BookService Microservice runs on port `8081`
It's where the CRUD operations are performed on the `Book` entity.

### Takes advantage of the following routes

`PUT` that updates a Book in the Database via its id.
> /api/book/update/{bookId}

`PUT` that specifically handles subtracting from a Book's qty via its id.
> /api/book/sell/{bookId}

`PUT` that specifically handles adding to a Book's qty via its id.
> /api/book/restock/{bookId}

`POST` that gets a Book from the Database via its title.
> /api/book/get/title

`POST` that makes a new Book.
> /api/book/create

`GET` that gets a Book from the Database via its id.
> /api/book/get/{bookId}

`GET` that gets a list of Books from the Database.
> /api/book/get/all

`DELETE` that deletes a Book from the Database via its id.
> /api/book/delete/{bookId}

### Uses the following entities

`Book` which has the properties:
- `bookId`	integer($int64)
- `title`	string
- `description`	string
- `authorName`	string
- `qty` integer($int32)

`Quantity` which has the property:
- `qty` integer($int32)

`Title` which has the property:
- `text`	string

## OrderService

### What is it?

The BookService Microservice runs on port `8082`
It's where the interaction operations are performed on various entities and it interfaces with the BookService.

### Takes advantage of the following routes

PUT that sells a book from the Book Microservice.
> /api/order/sell/{bookId}

PUT that restocks a book to the Book Microservice.
> /api/order/restock/{bookId}

POST that gets a Book from the Book Microservice by its title.
> /api/order/view/title

GET that gets a list of Books from the Book Microservice.
> /api/order/view/all


### Uses the following entities

`Book` which has the properties:
- `bookId`	integer($int64)
- `title`	string
- `description`	string
- `authorName`	string
- `qty` integer($int32)

`Order` which has the properties:
- `bookTitle` string
- `qty` integer($int32)
- `msg` string
- `leftInStock` integer($int32)

`Quantity` which has the property:
- `qty` integer($int32)

`Title` which has the property:
- `text`	string
