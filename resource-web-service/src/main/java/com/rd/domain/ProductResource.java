package com.rd.domain;

/**
 * Created by mtoure on 29/01/2018.
 */
public class ProductResource {
    private String id;
    private String details;

    public ProductResource(String id, String details) {
        this.id = id;
        this.details = details;
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

    public ProductResource() {
    }
}
