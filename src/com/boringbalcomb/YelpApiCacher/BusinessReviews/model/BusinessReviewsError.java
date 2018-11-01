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
    "code",
    "description",
    "field",
    "instance"
})
public class BusinessReviewsError {

    @JsonProperty("code")
    private String code;
    @JsonProperty("description")
    private String description;
    @JsonProperty("field")
    private String field;
    @JsonProperty("instance")
    private String instance;

    @JsonProperty("code")
    public String getCode() {
        return code;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("field")
    public String getField() {
        return field;
    }

    @JsonProperty("instance")
    public String getInstance() {
        return instance;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setField(String field) {
        this.field = field;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

    @Override
    public String toString() {
        return "BusinessSearchError{" +
                "code='" + code + '\'' +
                ", description='" + description + '\'' +
                ", field='" + field + '\'' +
                ", instance='" + instance + '\'' +
                '}';
    }
}
