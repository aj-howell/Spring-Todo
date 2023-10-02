package com.todo.Location;

public class MyLocation {
    float longitude;
    float latitude;
    String time_zone;
    String city_name;
    Integer location_id;

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public String getTime_zone() {
        return time_zone;
    }

    public void setTime_zone(String timeZone) {
        this.time_zone = timeZone;
    }


    public Integer getLocation_id() {
        return location_id;
    }

    public void setLocation_id(Integer location_id) {
        this.location_id = location_id;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String name) {
        this.city_name = name;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Float.floatToIntBits(longitude);
        result = prime * result + Float.floatToIntBits(latitude);
        result = prime * result + ((time_zone == null) ? 0 : time_zone.hashCode());
        result = prime * result + ((city_name == null) ? 0 : city_name.hashCode());
        result = prime * result + ((location_id == null) ? 0 : location_id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MyLocation other = (MyLocation) obj;
        if (Float.floatToIntBits(longitude) != Float.floatToIntBits(other.longitude))
            return false;
        if (Float.floatToIntBits(latitude) != Float.floatToIntBits(other.latitude))
            return false;
        if (time_zone == null) {
            if (other.time_zone != null)
                return false;
        } else if (!time_zone.equals(other.time_zone))
            return false;
        if (city_name == null) {
            if (other.city_name != null)
                return false;
        } else if (!city_name.equals(other.city_name))
            return false;
        if (location_id == null) {
            if (other.location_id != null)
                return false;
        } else if (!location_id.equals(other.location_id))
            return false;
        return true;
    }

    public MyLocation(){}

    public MyLocation(float longitude, float latitude, String timeZone, Integer location_id, String city_name) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.time_zone = timeZone;
        this.location_id=location_id;
        this.city_name=city_name;
    }
}
