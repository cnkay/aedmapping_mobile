
package com.app.aedmapping.Retrofit.Geocoder;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateGeocoderResponse {

    @SerializedName("plus_code")
    @Expose
    private PlusCode plusCode;
    @SerializedName("results")
    @Expose
    private List<Result> results = null;
    @SerializedName("status")
    @Expose
    private String status;

    /**
     * No args constructor for use in serialization
     * 
     */
    public CreateGeocoderResponse() {
    }

    /**
     * 
     * @param plusCode
     * @param results
     * @param status
     */
    public CreateGeocoderResponse(PlusCode plusCode, List<Result> results, String status) {
        super();
        this.plusCode = plusCode;
        this.results = results;
        this.status = status;
    }

    public PlusCode getPlusCode() {
        return plusCode;
    }

    public void setPlusCode(PlusCode plusCode) {
        this.plusCode = plusCode;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
