package com.boringbalcomb.YelpApiCacher.BusinessReviews.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

// @JsonIgnoreProperties
@JsonIgnoreProperties(ignoreUnknown = true)
// @JsonIgnoreProperties({ "extra", "uselessValue" })
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "reviews",
    "total",
    "possible_languages",
    "error"
})
public class BusinessReviews {

    @JsonProperty("reviews")
    private List<BusinessReviewsReview> reviews = null;
    @JsonProperty("total")
    private Integer total;
    @JsonProperty("possible_languages")
    private List<String> possibleLanguages = null;
    @JsonProperty("error")
    private BusinessReviewsError error;

    @JsonProperty("reviews")
    public List<BusinessReviewsReview> getReviews() {
        return reviews;
    }

    @JsonProperty("reviews")
    public void setReviews(List<BusinessReviewsReview> reviews) {
        this.reviews = reviews;
    }

    @JsonProperty("total")
    public Integer getTotal() {
        return total;
    }

    @JsonProperty("total")
    public void setTotal(Integer total) {
        this.total = total;
    }

    @JsonProperty("possible_languages")
    public List<String> getPossibleLanguages() {
        return possibleLanguages;
    }

    @JsonProperty("possible_languages")
    public void setPossibleLanguages(List<String> possibleLanguages) {
        this.possibleLanguages = possibleLanguages;
    }

    @JsonProperty("error")
    public BusinessReviewsError getError() {
        return error;
    }

    @JsonProperty("error")
    public void setError(BusinessReviewsError error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "BusinessReviews{" +
                "reviews=" + reviews +
                ", total=" + total +
                ", possibleLanguages=" + possibleLanguages +
                ", error=" + error +
                '}';
    }
}
