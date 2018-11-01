
package com.boringbalcomb.YelpApiCacher.BusinessSearch.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "center"
})
public class BusinessSearchRegion {

    @JsonProperty("center")
    private BusinessSearchRegionCenter center;

    @JsonProperty("center")
    public BusinessSearchRegionCenter getCenter() {
        return center;
    }

    @JsonProperty("center")
    public void setCenter(BusinessSearchRegionCenter center) {
        this.center = center;
    }

    @Override
    public String toString() {
        return "BusinessSearchRegion{" +
                "center=" + center +
                '}';
    }

}
