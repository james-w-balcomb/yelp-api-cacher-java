package com.boringbalcomb.AtsUtilities;

public class AtsDateTime {

    public static java.sql.Timestamp getCurrentTimeStamp() {
        java.util.Date today = new java.util.Date();
        return new java.sql.Timestamp(today.getTime());
    }

}
