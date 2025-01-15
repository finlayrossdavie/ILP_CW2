package uk.ac.ed.inf.ilp_cw1.service;


import uk.ac.ed.inf.ilp_cw1.Data.Order;
import uk.ac.ed.inf.ilp_cw1.Data.Region;
import uk.ac.ed.inf.ilp_cw1.Data.Restaurant;
import org.springframework.web.client.RestTemplate;

public class restHandler {

  public static Restaurant[] fetchRestaurants(String inputURL){

    RestTemplate restTemplate = new RestTemplate();
    Restaurant[] restaurants = restTemplate.getForObject(inputURL, Restaurant[].class);

    return restaurants;
  }

  public static Region[] fetchNoFlyZones(String inputURL){
    RestTemplate restTemplate = new RestTemplate();

    return restTemplate.getForObject(inputURL, Region[].class);
  }

  public static Order[] fetchOrders(String inputURL){
    RestTemplate restTemplate = new RestTemplate();
    Order[] orders = restTemplate.getForObject(inputURL, Order[].class);

    return orders;
  }
}
