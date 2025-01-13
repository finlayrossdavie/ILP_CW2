package uk.ac.ed.inf.ilp_cw1.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import uk.ac.ed.inf.ilp_cw1.Data.Restaurant;
import uk.ac.ed.inf.ilp_cw1.Data.SystemConstants;

public class restaurantHandler {

  public static Restaurant[] fetchRestaurants(){

    URL url;
    try {
      url = new URL(SystemConstants.RESTAURANT_URL);
    } catch (MalformedURLException e) {
      return null;
    }

    HttpURLConnection connection;
    try {
      connection = (HttpURLConnection) url.openConnection();
    } catch (IOException e) {
      return null;
    }
    try {
      connection.setRequestMethod("GET");
    } catch (ProtocolException e) {
      return null;
    }
    connection.setRequestProperty("Accept", "application/json");

    try {
      if(connection.getResponseCode() != 200){
        return null;
      }
    } catch (IOException e) {
      return null;
    }

    ObjectMapper mapper = new ObjectMapper();
    try {
      return mapper.readValue(connection.getInputStream(), Restaurant[].class);
    } catch (IOException e) {
      return null;
    }
  }

}
