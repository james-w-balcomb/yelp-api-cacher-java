
package com.boringbalcomb.YelpApiCacher.BusinessSearch.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
// import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "latitude",
    "longitude"
})
public class BusinessSearchBusinessCoordinates {

    @JsonProperty("latitude")
    private Float latitude;
    @JsonProperty("longitude")
    private Float longitude;

    @JsonProperty("latitude")
    public Float getLatitude() {
        return latitude;
    }

    @JsonProperty("latitude")
    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    @JsonProperty("longitude")
    public Float getLongitude() {
        return longitude;
    }

    @JsonProperty("longitude")
    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

//    @Override
//    public String toString() {
//        return new ToStringBuilder(this).append("latitude", latitude).append("longitude", longitude).toString();
//    }

    @Override
    public String toString() {
        return "BusinessSearchBusinessCoordinates{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }

}
