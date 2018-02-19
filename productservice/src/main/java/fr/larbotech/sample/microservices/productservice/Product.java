package fr.larbotech.sample.microservices.productservice;

import java.net.InetAddress;

public class Product {

    private String id;
    private String details = "no details";


    private Product(String id) {
        this.id = id;
    }

    static Product getMockProduct(String id) throws Exception{
        Product product = new Product(id);
        product.setDetails(InetAddress.getLocalHost().getHostAddress());
        return product;
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
