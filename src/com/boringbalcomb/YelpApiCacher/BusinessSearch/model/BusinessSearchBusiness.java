
package com.boringbalcomb.YelpApiCacher.BusinessSearch.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "alias",
    "name",
    "image_url",
    "is_closed",
    "url",
    "review_count",
    "categories",
    "rating",
    "coordinates",
    "transactions",
    "price",
    "location",
    "phone",
    "display_phone",
    "getDistanceBetweenCoordinates"
})
public class BusinessSearchBusiness {

    @JsonProperty("id")
    private String id;
    @JsonProperty("alias")
    private String alias;
    @JsonProperty("name")
    private String name;
    @JsonProperty("image_url")
    private String imageUrl;
    @JsonProperty("is_closed")
    private Boolean isClosed;
    @JsonProperty("url")
    private String url;
    @JsonProperty("review_count")
    private Integer reviewCount;
    @JsonProperty("categories")
    private List<BusinessSearchBusinessCategory> categories = null;
    @JsonProperty("rating")
    private Float rating;
    @JsonProperty("coordinates")
    private BusinessSearchBusinessCoordinates coordinates;
    @JsonProperty("transactions")
    private List<String> transactions = null;
    @JsonProperty("price")
    private String price;
    @JsonProperty("location")
    private BusinessSearchBusinessLocation location;
    @JsonProperty("phone")
    private String phone;
    @JsonProperty("display_phone")
    private String displayPhone;
    @JsonProperty("getDistanceBetweenCoordinates")
    private Float distance;

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("alias")
    public String getAlias() {
        return alias;
    }

    @JsonProperty("alias")
    public void setAlias(String alias) {
        this.alias = alias;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("image_url")
    public String getImageUrl() {
        return imageUrl;
    }

    @JsonProperty("image_url")
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @JsonProperty("is_closed")
    public Boolean getIsClosed() {
        return isClosed;
    }

    @JsonProperty("is_closed")
    public void setIsClosed(Boolean isClosed) {
        this.isClosed = isClosed;
    }

    @JsonProperty("url")
    public String getUrl() {
        return url;
    }

    @JsonProperty("url")
    public void setUrl(String url) {
        this.url = url;
    }

    @JsonProperty("review_count")
    public Integer getReviewCount() {
        return reviewCount;
    }

    @JsonProperty("review_count")
    public void setReviewCount(Integer reviewCount) {
        this.reviewCount = reviewCount;
    }

    @JsonProperty("categories")
    public List<BusinessSearchBusinessCategory> getCategories() {
        return categories;
    }

    @JsonProperty("categories")
    public void setCategories(List<BusinessSearchBusinessCategory> categories) {
        this.categories = categories;
    }

    @JsonProperty("rating")
    public Float getRating() {
        return rating;
    }

    @JsonProperty("rating")
    public void setRating(Float rating) {
        this.rating = rating;
    }

    @JsonProperty("coordinates")
    public BusinessSearchBusinessCoordinates getCoordinates() {
        return coordinates;
    }

    @JsonProperty("coordinates")
    public void setCoordinates(BusinessSearchBusinessCoordinates coordinates) {
        this.coordinates = coordinates;
    }

    @JsonProperty("transactions")
    public List<String> getTransactions() {
        return transactions;
    }

    @JsonProperty("transactions")
    public void setTransactions(List<String> transactions) {
        this.transactions = transactions;
    }

    @JsonProperty("price")
    public String getPrice() {
        return price;
    }

    @JsonProperty("price")
    public void setPrice(String price) {
        this.price = price;
    }

    @JsonProperty("location")
    public BusinessSearchBusinessLocation getLocation() {
        return location;
    }

    @JsonProperty("location")
    public void setLocation(BusinessSearchBusinessLocation location) {
        this.location = location;
    }

    @JsonProperty("phone")
    public String getPhone() {
        return phone;
    }

    @JsonProperty("phone")
    public void setPhone(String phone) {
        this.phone = phone;
    }

    @JsonProperty("display_phone")
    public String getDisplayPhone() {
        return displayPhone;
    }

    @JsonProperty("display_phone")
    public void setDisplayPhone(String displayPhone) {
        this.displayPhone = displayPhone;
    }

    @JsonProperty("getDistanceBetweenCoordinates")
    public Float getDistance() {
        return distance;
    }

    @JsonProperty("getDistanceBetweenCoordinates")
    public void setDistance(Float distance) {
        this.distance = distance;
    }

//    @Override
//    public String toString() {
//        return new ToStringBuilder(this).append("id", id).append("alias", alias).append("name", name).append("imageUrl", imageUrl).append("isClosed", isClosed).append("url", url).append("reviewCount", reviewCount).append("categories", categories).append("rating", rating).append("coordinates", coordinates).append("transactions", transactions).append("price", price).append("location", location).append("phone", phone).append("displayPhone", displayPhone).append("getDistanceBetweenCoordinates", getDistanceBetweenCoordinates).toString();
//    }

    @Override
    public String toString() {
        return "BusinessSearchBusiness{" +
                "id='" + id + '\'' +
                ", alias='" + alias + '\'' +
                ", name='" + name + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", isClosed=" + isClosed +
                ", url='" + url + '\'' +
                ", reviewCount=" + reviewCount +
                ", categories=" + categories +
                ", rating=" + rating +
                ", coordinates=" + coordinates +
                ", transactions=" + transactions +
                ", price='" + price + '\'' +
                ", location=" + location +
                ", phone='" + phone + '\'' +
                ", displayPhone='" + displayPhone + '\'' +
                ", getDistanceBetweenCoordinates=" + distance +
                '}';
    }

}
