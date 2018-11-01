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
    "profile_url",
    "image_url",
    "name"
})
public class BusinessReviewsReviewUser {

    @JsonProperty("id")
    private String id;
    @JsonProperty("profile_url")
    private String profileUrl;
    @JsonProperty("image_url")
    private String imageUrl;
    @JsonProperty("name")
    private String name;

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("profile_url")
    public String getProfileUrl() {
        return profileUrl;
    }

    @JsonProperty("profile_url")
    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    @JsonProperty("image_url")
    public String getImageUrl() {
        return imageUrl;
    }

    @JsonProperty("image_url")
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "BusinessReviewsReviewUser{" +
                "id='" + id + '\'' +
                ", profileUrl='" + profileUrl + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

}
