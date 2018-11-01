
package com.boringbalcomb.YelpApiCacher.BusinessDetails.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "alias",
    "title"
})
public class BusinessDetailsCategory {

    @JsonProperty("alias")
    private String alias;
    @JsonProperty("title")
    private String title;

    @JsonProperty("alias")
    public String getAlias() {
        return alias;
    }

    @JsonProperty("alias")
    public void setAlias(String alias) {
        this.alias = alias;
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "BusinessDetailsCategory{" +
                "alias='" + alias + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
