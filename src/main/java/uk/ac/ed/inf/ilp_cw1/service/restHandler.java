package uk.ac.ed.inf.ilp_cw1.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import uk.ac.ed.inf.ilp_cw1.Data.Order;
import uk.ac.ed.inf.ilp_cw1.Data.Region;
import uk.ac.ed.inf.ilp_cw1.Data.Restaurant;
import uk.ac.ed.inf.ilp_cw1.Data.SystemConstants;
import org.springframework.web.client.RestTemplate;

public class restHandler {

  public static Restaurant[] fetchRestaurants(String inputURL){

    RestTemplate restTemplate = new RestTemplate();
    Restaurant[] restaurants = restTemplate.getForObject(inputURL, Restaurant[].class);

    return restaurants;
  }

  public static Region[] fetchNoFlyZones(String inputURL){
    RestTemplate restTemplate = new RestTemplate();
    Region[] noFlyZones = restTemplate.getForObject(inputURL, Region[].class);

    return noFlyZones;
  }

  public static Order[] fetchOrders(String inputURL){
    RestTemplate restTemplate = new RestTemplate();
    Order[] orders = restTemplate.getForObject(inputURL, Order[].class);

    return orders;
  }
}
