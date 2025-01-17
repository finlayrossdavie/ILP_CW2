package uk.ac.ed.inf.ilp_cw1.service;


import uk.ac.ed.inf.ilp_cw1.Data.Order;
import uk.ac.ed.inf.ilp_cw1.Data.Region;
import uk.ac.ed.inf.ilp_cw1.Data.Restaurant;
import org.springframework.web.client.RestTemplate;

/*
The restHandler class provides methods to fetch data from the ILP
data service. This class retrieves Restaurants, No-Fly Zones, Central area
and test orders.
 */

public class restHandler {

  /**
   * Fetches an array of Restaurant objects from the specified API URL.
   *
   * @param inputURL the URL of the API endpoint returning Restaurant data
   * @return an array of Restaurant objects fetched from the API
   *
   * This method uses a `RestTemplate` to make an HTTP GET request to the given URL.
   * The API response is a JSON array that maps to the Restaurant class.
   */

  public static Restaurant[] fetchRestaurants(String inputURL){

    RestTemplate restTemplate = new RestTemplate();
    Restaurant[] restaurants = restTemplate.getForObject(inputURL, Restaurant[].class);

    return restaurants;
  }

  /**
   * Fetches an array of Region objects representing no-fly zones from the specified API URL.
   *
   * @param inputURL the URL of the API endpoint returning no-fly zone data
   * @return an array of Region objects fetched from the API
   *
   * This method uses a `RestTemplate` to make an HTTP GET request to the given URL.
   * The API response a JSON array that maps to the Region class.
   */

  public static Region[] fetchNoFlyZones(String inputURL){
    RestTemplate restTemplate = new RestTemplate();

    return restTemplate.getForObject(inputURL, Region[].class);
  }

  /**
   * Fetches an array of Order objects from the specified API URL.
   *
   * @param inputURL the URL of the API endpoint returning Order data
   * @return an array of Order objects fetched from the API
   *
   * This method uses a `RestTemplate` to make an HTTP GET request to the given URL.
   * The API response is a JSON array that maps to the Order class.
   */

  public static Order[] fetchOrders(String inputURL){
    RestTemplate restTemplate = new RestTemplate();
    Order[] orders = restTemplate.getForObject(inputURL, Order[].class);

    return orders;
  }

  /**
   * The coordinates of the central region
   *
   * @param inputURL the URL of the API endpoint returning Order data
   * @return A Region object of the central region
   *
   * This method uses a `RestTemplate` to make an HTTP GET request to the given URL.
   * The API response is a JSON array that maps to the Order class.
   */


  public static Region fetchCentralRegion(String inputURL){
    RestTemplate restTemplate = new RestTemplate();
    Region centralRegion = restTemplate.getForObject(inputURL, Region.class);

    return centralRegion;
  }
}
