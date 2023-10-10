package com.todo.PrayerDetails;
import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class PrayerDetailsFactory{
    
    public static List<String> GetPrayerDetails(String city_name, Integer userID)
    {
        //instantiates the client 
        HttpClient prayer_client = HttpClients.createDefault();

        List<String> five_prayers = new ArrayList<>();

        HttpGet get_Prayer = new HttpGet("http://api.aladhan.com/v1/timingsByCity?city=%s&country=UnitedStates".formatted(city_name));
        // could set parameters with the object itself but since this is a factory and we are doing one action this will be left like this

        //create response & execute the get request
        try {
            HttpResponse prayer_response = prayer_client.execute(get_Prayer);
            
        //extract the prayer name & time from response
         String prayer = EntityUtils.toString(prayer_response.getEntity());

         // important for releasing the resources 
         EntityUtils.consume(prayer_response.getEntity());

         //parse the string using JSONObject class
         JSONObject prayerObject = new JSONObject(prayer);

        //Grab the 5 prayers
        String Fajr = prayerObject.getJSONObject("data").getJSONObject("timings").getString("Fajr");
        String Dhuhr = prayerObject.getJSONObject("data").getJSONObject("timings").getString("Dhuhr");
        String Asr = prayerObject.getJSONObject("data").getJSONObject("timings").getString("Asr");
        String Maghrib = prayerObject.getJSONObject("data").getJSONObject("timings").getString("Maghrib");
        String Isha = prayerObject.getJSONObject("data").getJSONObject("timings").getString("Isha");

        five_prayers.add(Fajr);
        five_prayers.add(Dhuhr);
        five_prayers.add(Asr);
        five_prayers.add(Maghrib);
        five_prayers.add(Isha);

        ((Closeable) prayer_client).close();


        } catch (ClientProtocolException e) {   
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return five_prayers;


    }

}
