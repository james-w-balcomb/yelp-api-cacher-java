package com.boringbalcomb.YelpApiCacher.BusinessReviews.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

// @JsonIgnoreProperties
@JsonIgnoreProperties(ignoreUnknown = true)
// @JsonIgnoreProperties({ "extra", "uselessValue" })
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "url",
    "text",
    "rating",
    "time_created",
    "user"
})

public class BusinessReviewsReview {

    @JsonProperty("id")
    private String id;
    @JsonProperty("url")
    private String url;
    @JsonProperty("text")
    private String text;
    @JsonProperty("rating")
    private Integer rating;
    @JsonProperty("time_created")
    private String timeCreated;
    @JsonProperty("user")
    private BusinessReviewsReviewUser user;

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("url")
    public String getUrl() {
        return url;
    }

    @JsonProperty("url")
    public void setUrl(String url) {
        this.url = url;
    }

    @JsonProperty("text")
    public String getText() {
        return text;
    }

    @JsonProperty("text")
    public void setText(String text) {
        this.text = text;
    }

    @JsonProperty("rating")
    public Integer getRating() {
        return rating;
    }

    @JsonProperty("rating")
    public void setRating(Integer rating) {
        this.rating = rating;
    }

    @JsonProperty("time_created")
    public String getTimeCreated() {
        return timeCreated;
    }

    @JsonProperty("time_created")
    public void setTimeCreated(String timeCreated) {
        this.timeCreated = timeCreated;
    }

    @JsonProperty("user")
    public BusinessReviewsReviewUser getUser() {
        return user;
    }

    @JsonProperty("user")
    public void setUser(BusinessReviewsReviewUser user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "BusinessReviewsReview{" +
                "id='" + id + '\'' +
                ", url='" + url + '\'' +
                ", text='" + text + '\'' +
                ", rating=" + rating +
                ", timeCreated='" + timeCreated + '\'' +
                ", user=" + user +
                '}';
    }

}
