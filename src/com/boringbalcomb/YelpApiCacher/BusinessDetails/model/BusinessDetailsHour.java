package com.boringbalcomb.YelpApiCacher.BusinessDetails.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "open",
    "hours_type",
    "is_open_now"
})
public class BusinessDetailsHour {

    @JsonProperty("open")
    private List<BusinessDetailsHourOpen> open = null;
    @JsonProperty("hours_type")
    private String hoursType;
    @JsonProperty("is_open_now")
    private Boolean isOpenNow;

    @JsonProperty("open")
    public List<BusinessDetailsHourOpen> getOpen() {
        return open;
    }

    @JsonProperty("hours_type")
    public String getHoursType() {
        return hoursType;
    }

    @JsonProperty("is_open_now")
    public Boolean getIsOpenNow() {
        return isOpenNow;
    }

    public void setOpen(List<BusinessDetailsHourOpen> open) {
        this.open = open;
    }

    public void setHoursType(String hoursType) {
        this.hoursType = hoursType;
    }

    public void setOpenNow(Boolean openNow) {
        isOpenNow = openNow;
    }

    @Override
    public String toString() {
        return "BusinessDetailsHour{" +
                "open=" + open +
                ", hoursType='" + hoursType + '\'' +
                ", isOpenNow=" + isOpenNow +
                '}';
    }
}
