package midori.kitchen.content.model;

import android.util.Log;

/**
 * Created by ModelUser on 8/3/2015.
 */
public class PlaceModel {


    private String placeId="";
    private String address="";
    private String addressDetail="";
    private Double latitude = 0.0;
    private Double longitude = 0.0;

    public PlaceModel(String placeId, String address, String addressDetail ) {
        this.placeId = placeId;
        this.address = address;
        this.addressDetail = addressDetail;
    }

    public PlaceModel(Double latitude, Double longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return address.toString();
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressDetail() {
        return addressDetail;
    }

    public void setAddressDetail(String addressDetail) {
        this.addressDetail = addressDetail;
    }

    public Double getLatitude() {
        Log.d("modelplace", "lat:" + latitude);
        return latitude;

    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        Log.d("modelplace", "lon:" + longitude);
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
