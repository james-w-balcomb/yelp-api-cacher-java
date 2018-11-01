
package com.boringbalcomb.YelpApiCacher.BusinessSearch.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "longitude",
    "latitude"
})
public class BusinessSearchRegionCenter {

    @JsonProperty("longitude")
    private Float longitude;
    @JsonProperty("latitude")
    private Float latitude;

    @JsonProperty("longitude")
    public Float getLongitude() {
        return longitude;
    }

    @JsonProperty("longitude")
    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    @JsonProperty("latitude")
    public Float getLatitude() {
        return latitude;
    }

    @JsonProperty("latitude")
    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return "BusinessSearchRegionCenter{" +
                "longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }

}
