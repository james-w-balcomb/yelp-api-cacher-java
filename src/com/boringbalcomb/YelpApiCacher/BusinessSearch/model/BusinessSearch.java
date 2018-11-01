
package com.boringbalcomb.YelpApiCacher.BusinessSearch.model;

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
        "businesses",
        "total",
        "region",
        "error"
})
public class BusinessSearch {

    @JsonProperty("businesses")
    private List<BusinessSearchBusiness> businesses = null;
    @JsonProperty("total")
    private Integer total;
    @JsonProperty("region")
    private BusinessSearchRegion region;
    @JsonProperty("error")
    private BusinessSearchError error;

    @JsonProperty("businesses")
    public List<BusinessSearchBusiness> getBusinesses() {
        return businesses;
    }

    @JsonProperty("total")
    public Integer getTotal() {
        return total;
    }

    @JsonProperty("region")
    public BusinessSearchRegion getRegion() {
        return region;
    }

    @JsonProperty("error")
    public BusinessSearchError getError() {
        return error;
    }

    public void setBusinesses(List<BusinessSearchBusiness> businesses) {
        this.businesses = businesses;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public void setRegion(BusinessSearchRegion region) {
        this.region = region;
    }

    public void setError(BusinessSearchError error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "BusinessSearch{" +
                "businesses=" + businesses +
                ", total=" + total +
                ", region=" + region +
                ", error=" + error +
                '}';
    }

}
