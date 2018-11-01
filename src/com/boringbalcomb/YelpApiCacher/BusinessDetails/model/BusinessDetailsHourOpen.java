
package com.boringbalcomb.YelpApiCacher.BusinessDetails.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "is_overnight",
    "start",
    "end",
    "day"
})
public class BusinessDetailsHourOpen {

    @JsonProperty("is_overnight")
    private Boolean isOvernight;
    @JsonProperty("start")
    private String start;
    @JsonProperty("end")
    private String end;
    @JsonProperty("day")
    private Integer day;

    @JsonProperty("is_overnight")
    public Boolean getIsOvernight() {
        return isOvernight;
    }

    @JsonProperty("is_overnight")
    public void setIsOvernight(Boolean isOvernight) {
        this.isOvernight = isOvernight;
    }

    @JsonProperty("start")
    public String getStart() {
        return start;
    }

    @JsonProperty("start")
    public void setStart(String start) {
        this.start = start;
    }

    @JsonProperty("end")
    public String getEnd() {
        return end;
    }

    @JsonProperty("end")
    public void setEnd(String end) {
        this.end = end;
    }

    @JsonProperty("day")
    public Integer getDay() {
        return day;
    }

    @JsonProperty("day")
    public void setDay(Integer day) {
        this.day = day;
    }

    @Override
    public String toString() {
        return "BusinessDetailsHourOpen{" +
                "isOvernight=" + isOvernight +
                ", start='" + start + '\'' +
                ", end='" + end + '\'' +
                ", day=" + day +
                '}';
    }
}
