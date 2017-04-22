package com.lions.caring_vendor.structs;

/**
 * Created by Panwar on 19/04/17.
 */
public class Pending_Booking_Bean {


    private String book_id ;
    private String customer_name;
    private String advance ;
    private String distance;
    private String date;
    private String time;
    private String customer_mob;
    private String vendor_mobile;
    private String lat;
    private String longitude;
    private String status;
    private String otp;


    public String getOtp() {
        return this.otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getLat() {
        return lat;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }


    public String getCustomer_mob() {
        return customer_mob;
    }

    public String getVendor_mobile() {
        return vendor_mobile;
    }

    public void setCustomer_mob(String customer_mob) {
        this.customer_mob = customer_mob;
    }

    public void setVendor_mobile(String vendor_mobile) {
        this.vendor_mobile = vendor_mobile;
    }

    public String getBook_id() {
        return book_id;
    }

    public void setBook_id(String book_id) {
        this.book_id = book_id;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public void setAdvance(String advance) {
        this.advance = advance;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAdvance() {
        return advance;
    }

    public String getDate() {
        return date;
    }

    public String getDistance() {
        return distance;
    }

    public String getTime() {
        return time;
    }

}
