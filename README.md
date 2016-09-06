# order-processor
An order processing service in Java.

# What it does
The order-processor allows a client to use a REST-ful API to place orders against a simple product catalog of fruit and to query those orders. The order-processor validates the items ordered and the quantities available. It also maintains inventory quantities as orders are placed. It stores orders that fail due to one or more items in the order being unknown or unavailable.

# How it does it
The order-processor is a [Spring Boot](http://projects.spring.io/spring-boot/) application backed by an in-memory database (H2). The product catalog has been preloaded (see src/main/resources/schema.sql and data.sql). Two accounts have also been preloaded. Note that restarting the application clears the database of any orders placed and reinitializes the inventory and accounts tables.

Order placement and querying is secured by API key. Each client has his own API key, and can retrieve his own orders.

## The data model
An `Order` belongs to an `Account` and can have zero or more `OrderItem`s. An order can be in one of two statuses:
* open: all SKUs on the order are in stock and the order has been placed.
* failed: one or more SKUs are not available in sufficient quantities to fulfill the order. An `OrderItem` without a price or an extended price indicates that the requested SKU is not available. Any `Order` without `OrderItems` is also set to "failed" status.

`Inventory` maintains a record of the current product catalog: SKU, price, and available quantity.

`Order`s are mapped using Hibernate and accessed using JPA. `Inventory` and `Account`s are POJOs accessed using direct JDBC.

# How to run it
The order-processor is built using maven and runs as a standalone JAR. It requires [maven](https://maven.apache.org/download.cgi) and [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html). To build and run locally:

Clone or download the source:

`git clone git@github.com:kinman/order-processor.git`

cd into the project root and build the project:

`mvn clean package`

Launch the jar:

`java -jar target/order-processor-0.1.jar`

The order-processor runs on http://localhost:8080.

# How to use the API
The API is secured by API key. Two keys are provided:

* greengrocer123: API key for account The Green Grocer.
* producestand456: API key for account The Produce Stand.

The key must be provided in a custom request header named `kinman-api-key`.

## Available Endpoints

Note: A prototype of API documentation is available on [Apiary.io](http://docs.ordersapi7.apiary.io/#).

### /api/v1/orders (GET)
Returns a list of orders this account has placed.

Example:
`curl -H "kinman-api-key: greengrocer123" http://localhost:8080/api/v1/orders`

The list can also be filtered to return orders in a given status (`open` or `failed`); e.g.,

`curl -H "kinman-api-key: greengrocer123" http://localhost:8080/api/v1/orders?status=open`

### /api/v1/orders (POST)
Creates a new order.

Example:
````
curl -H "Content-Type: application/json" \
-H "kinman-api-key: greengrocer123" http://localhost:8080/api/v1/orders \
-d '{"orderItems": [{"sku": "Orange", "qty":2}, {"sku": "Bananas", "qty":4}]}'
````

### /api/v1/orders/{id} (GET)
Returns the order with the given id. If the order doesn't exist (or doesn't belong to the existing account), the response is a 404.

Example:

`curl -H "kinman-api-key: greengrocer123" http://localhost:8080/api/v1/orders/1`

To view the complete order, request expansion of the `orderItems` collection:

`curl -H "kinman-api-key: greengrocer123" http://localhost:8080/api/v1/orders/1?expand=orderItems`

# Assumptions and TODOs

## All orders are persisted regardless of status.
Often, an order that cannot be fulfilled is not persisted. I assume that was a requirement here in order to demonstrate filtering ability.

## SKUs are case-sensitive and exact match.
The product catalog is keyed on the name of the product (e.g, "Orange", "Kiwi", "Bananas"). An order for 1 "kiwi" will be put in a failed status.

## The application is not thread-safe.
The inventory checking, order placement, and inventory depletion are separate transactions, thus making it likely that in a high-volume situation, an order could be placed successfully, yet without sufficient inventory to fulfill it. I've allowed the inventory count to go negative: Additional functionality could include an alert when quantities become low.

## The authorization is rudimentary.
In a production-ready application, I'd use OAuth2 to secure an order processing system, rather than simple, stored-in-plaintext API keys.

## Order expansion is only a proof of concept (POC).
Ideally, I'd alter the Order mapping not to retrieve and render OrderItems by default. To demonstrate resource expansion in the Fetch Order request, I've removed the OrderItems from the retrieved Order before rendering the Order when the "expand" query parameter is not present.

## Documentation is sparse.
In a production-ready application, I'd have more complete API documentation than what is provided in this README.
