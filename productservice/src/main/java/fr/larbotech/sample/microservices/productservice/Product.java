package fr.larbotech.sample.microservices.productservice;

public class Product {

    private String id;
    private String details = "no details";


    private Product(String id) {
        this.id = id;
    }

    static Product getMockProduct(String id) {
        return new Product(id);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
