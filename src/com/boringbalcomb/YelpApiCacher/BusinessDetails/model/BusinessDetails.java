
package com.boringbalcomb.YelpApiCacher.BusinessDetails.model;

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
    "id",
    "alias",
    "name",
    "image_url",
    "is_claimed",
    "is_closed",
    "url",
    "phone",
    "display_phone",
    "review_count",
    "categories",
    "rating",
    "location",
    "coordinates",
    "photos",
    "price",
    "hours",
    "transactions"
})
public class BusinessDetails {

    @JsonProperty("id")
    private String id;
    @JsonProperty("alias")
    private String alias;
    @JsonProperty("name")
    private String name;
    @JsonProperty("image_url")
    private String imageUrl;
    @JsonProperty("is_claimed")
    private Boolean isClaimed;
    @JsonProperty("is_closed")
    private Boolean isClosed;
    @JsonProperty("url")
    private String url;
    @JsonProperty("phone")
    private String phone;
    @JsonProperty("display_phone")
    private String displayPhone;
    @JsonProperty("review_count")
    private Integer reviewCount;
    @JsonProperty("categories")
    private List<BusinessDetailsCategory> categories = null;
    @JsonProperty("rating")
    private Float rating;
    @JsonProperty("location")
    private BusinessDetailsLocation location;
    @JsonProperty("coordinates")
    private BusinessDetailsCoordinates coordinates;
    @JsonProperty("photos")
    private List<String> photos = null;
    @JsonProperty("price")
    private String price;
    @JsonProperty("hours")
    private List<BusinessDetailsHour> hours = null;
    @JsonProperty("transactions")
    private List<Object> transactions = null;
    @JsonProperty("error")
    private BusinessDetailsError error;

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("alias")
    public String getAlias() {
        return alias;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("image_url")
    public String getImageUrl() {
        return imageUrl;
    }

    @JsonProperty("is_claimed")
    public Boolean getIsClaimed() {
        return isClaimed;
    }

    @JsonProperty("is_closed")
    public Boolean getIsClosed() {
        return isClosed;
    }

    @JsonProperty("url")
    public String getUrl() {
        return url;
    }

    @JsonProperty("phone")
    public String getPhone() {
        return phone;
    }

    @JsonProperty("display_phone")
    public String getDisplayPhone() {
        return displayPhone;
    }

    @JsonProperty("review_count")
    public Integer getReviewCount() {
        return reviewCount;
    }

    @JsonProperty("categories")
    public List<BusinessDetailsCategory> getCategories() {
        return categories;
    }

    @JsonProperty("rating")
    public Float getRating() {
        return rating;
    }

    @JsonProperty("location")
    public BusinessDetailsLocation getLocation() {
        return location;
    }

    @JsonProperty("coordinates")
    public BusinessDetailsCoordinates getCoordinates() {
        return coordinates;
    }

    @JsonProperty("photos")
    public List<String> getPhotos() {
        return photos;
    }

    @JsonProperty("price")
    public String getPrice() {
        return price;
    }

    @JsonProperty("hours")
    public List<BusinessDetailsHour> getHours() {
        return hours;
    }

    @JsonProperty("transactions")
    public List<Object> getTransactions() {
        return transactions;
    }

    @JsonProperty("error")
    public BusinessDetailsError getError() {
        return error;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setClaimed(Boolean claimed) {
        isClaimed = claimed;
    }

    public void setClosed(Boolean closed) {
        isClosed = closed;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setDisplayPhone(String displayPhone) {
        this.displayPhone = displayPhone;
    }

    public void setReviewCount(Integer reviewCount) {
        this.reviewCount = reviewCount;
    }

    public void setCategories(List<BusinessDetailsCategory> categories) {
        this.categories = categories;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public void setLocation(BusinessDetailsLocation location) {
        this.location = location;
    }

    public void setCoordinates(BusinessDetailsCoordinates coordinates) {
        this.coordinates = coordinates;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setHours(List<BusinessDetailsHour> hours) {
        this.hours = hours;
    }

    public void setTransactions(List<Object> transactions) {
        this.transactions = transactions;
    }

    public void setError(BusinessDetailsError error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "BusinessDetails{" +
                "id='" + id + '\'' +
                ", alias='" + alias + '\'' +
                ", name='" + name + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", isClaimed=" + isClaimed +
                ", isClosed=" + isClosed +
                ", url='" + url + '\'' +
                ", phone='" + phone + '\'' +
                ", displayPhone='" + displayPhone + '\'' +
                ", reviewCount=" + reviewCount +
                ", categories=" + categories +
                ", rating=" + rating +
                ", location=" + location +
                ", coordinates=" + coordinates +
                ", photos=" + photos +
                ", price='" + price + '\'' +
                ", hours=" + hours +
                ", transactions=" + transactions +
                ", error=" + error +
                '}';
    }

}
