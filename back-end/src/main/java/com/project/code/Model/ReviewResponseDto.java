package com.project.code.Model;

public class ReviewResponseDto {

    private String comment;

    private String customerName;

    private Integer rating;

    public ReviewResponseDto(String comment, String customerName, Integer rating) {
        this.comment = comment;
        this.customerName = customerName;
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }
}
