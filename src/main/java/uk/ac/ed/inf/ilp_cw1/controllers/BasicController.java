package uk.ac.ed.inf.ilp_cw1.controllers;

import static uk.ac.ed.inf.ilp_cw1.service.Calculations.collinear;
import static uk.ac.ed.inf.ilp_cw1.service.Calculations.nextPos;
import static uk.ac.ed.inf.ilp_cw1.service.Validations.isValid;
import static uk.ac.ed.inf.ilp_cw1.service.restHandler.fetchCentralRegion;
import static uk.ac.ed.inf.ilp_cw1.service.restHandler.fetchRestaurants;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import uk.ac.ed.inf.ilp_cw1.Data.*;
import uk.ac.ed.inf.ilp_cw1.service.*;

import uk.ac.ed.inf.ilp_cw1.service.restHandler;

/**
 * BasicController is a Spring REST controller that exposes several endpoints for handling requests
 * related to geographic calculations, order validation, and pathfinding operations.
 *
 * The controller provides functionality to calculate distances, check proximity, validate geographic positions,
 * fetch and validate restaurant orders, and calculate delivery paths while considering no-fly zones.
 *
 */

@RestController
public class BasicController {

  /**
   * Endpoint to check if the service is alive.
   *
   * @return true indicating the service is running.
   */

  @GetMapping("/isAlive")
  public boolean isAlive(){
    return true;
  }

  /**
   * Endpoint to return the user UUID.
   *
   * @return the user UUID as a string.
   */

  @GetMapping("/uuid")
  public String uuid(){
    return "s2281183";
  }

  /**
   * Endpoint to calculate the Euclidean distance between two positions.
   *
   * @param request A JSON request containing two positions (LngLatPairRequest).
   * @return the calculated distance or a bad request response if input is invalid.
   */

  @PostMapping("/distanceTo")
  public ResponseEntity<?> distanceTo(@RequestBody LngLatPairRequest request){
    if (request == null || !isValid(request.getLngLat1()) || !isValid(request.getLngLat2())){              //TODO: Improve poor input handling, in particular null lat and lng
      return ResponseEntity.status(HttpServletResponse.SC_BAD_REQUEST).body("Invalid JSON structure");
    }

    LngLat p1 = request.getLngLat1();
    LngLat p2 = request.getLngLat2();

    Double distance = Calculations.eucDistance(p1, p2);

    return ResponseEntity.status(HttpServletResponse.SC_OK).body(distance);

  }

  /**
   * Endpoint to check if two positions are close to each other.
   * Positions are considered close if their distance is less than a certain threshold.
   *
   * @param request A JSON request containing two positions (LngLatPairRequest).
   * @return true if the positions are close, otherwise false.
   */

  @PostMapping("/isCloseTo")
  public ResponseEntity<?> isCloseTo(@RequestBody LngLatPairRequest request){
    if (request == null || !isValid(request.getLngLat2()) || !isValid(request.getLngLat1())){              //TODO: Improve poor input handling, in particular null lat and lng
      return ResponseEntity.status(HttpServletResponse.SC_BAD_REQUEST).body("Invalid JSON structure");
    }

    Double distance = Calculations.eucDistance(request.getLngLat1(), request.getLngLat2());

    if (distance < 0.00015){
      return ResponseEntity.status(HttpServletResponse.SC_OK).body(true);
    }
    else{
      return ResponseEntity.status(HttpServletResponse.SC_OK).body(false);
    }
  }

  /**
   * Endpoint to calculate the next position based on a starting position and an angle.
   *
   * @param nextPositionRequest A JSON request containing the starting position and the angle (NextPositionRequest).
   * @return the next calculated position.
   */

  @PostMapping("/nextPosition")
  public ResponseEntity<?> nextPosition(@RequestBody NextPositionRequest nextPositionRequest){
    if (nextPositionRequest == null || !isValid(nextPositionRequest.getLngLat()) || !isValid(nextPositionRequest.getAngle())){
      return ResponseEntity.status(HttpServletResponse.SC_BAD_REQUEST).body("Invalid input");
    }

    LngLat lngLat = nextPositionRequest.getLngLat();
    Double angle = nextPositionRequest.getAngle();

    LngLat result = nextPos(lngLat,angle);

    return ResponseEntity.status(HttpServletResponse.SC_OK).body(result);
  }

  /**
   * Endpoint to check if a geographic position is inside a specified region.
   *
   * @param request A JSON request containing a position and a region (isInRegionRequest).
   * @return true if the position is inside the region, false otherwise.
   */

  @PostMapping("/isInRegion")
  public ResponseEntity<?> isInRegion(@RequestBody isInRegionRequest request){
    if (request == null || !isValid(request.getLngLat()) || !isValid(request.getRegion())){
      return ResponseEntity.status(HttpServletResponse.SC_BAD_REQUEST).body("Invalid input");
    }

    if (collinear(request.getRegion().getVertices())){
      return ResponseEntity.status(HttpServletResponse.SC_BAD_REQUEST).body("Points are collinear, thus invalid region");
    }

    return ResponseEntity.status(HttpServletResponse.SC_OK).body(request.getRegion().isInside(request.getLngLat()));

  }

  /**
   * Endpoint to validate an order and return the validation result.
   *
   * @param request A JSON request containing an order (Order).
   * @return the order validation result.
   */
  @PostMapping("/validateOrder")
  public ResponseEntity<?> validateOrder(@RequestBody Order request){
    OrderValidationImpl orderValidator = new OrderValidationImpl();
    Order result;

    Restaurant[] restaurants = fetchRestaurants(SystemConstants.RESTAURANT_URL);
    SystemConstants.CENTRAL_REGION = fetchCentralRegion(SystemConstants.CENTRALAREA_URL);

    result = orderValidator.validateOrder(request, restaurants);

    OrderValidationResult orderValidationResult = new OrderValidationResult(result.getOrderStatus(), result.getOrderValidationCode());

   return ResponseEntity.status(HttpServletResponse.SC_OK).body(orderValidationResult);
  }

  /**
   * Endpoint to calculate the delivery path for an order, considering no-fly zones.
   *
   * @param request A JSON request containing an order (Order).
   * @return the calculated path for delivery, considering no-fly zones.
   */
@PostMapping("/calcDeliveryPath")
  public ResponseEntity<?> calcDeliveryPath(@RequestBody Order request){
    OrderValidationImpl orderValidator = new OrderValidationImpl();
    Order result = null;

    Restaurant[] restaurants = null;

    restaurants = fetchRestaurants(SystemConstants.RESTAURANT_URL);
    SystemConstants.CENTRAL_REGION = fetchCentralRegion(SystemConstants.CENTRALAREA_URL);


  if(restaurants == null){
    return ResponseEntity.status(HttpServletResponse.SC_BAD_REQUEST).body("No response from restaurant website");
  }
   result = orderValidator.validateOrder(request, restaurants);

   if(result.getOrderValidationCode() != OrderValidationCode.NO_ERROR){
     OrderValidationResult orderValidationResult = new OrderValidationResult(result.getOrderStatus(), result.getOrderValidationCode());
     return ResponseEntity.status(HttpServletResponse.SC_BAD_REQUEST).body(orderValidationResult);
   }

  Region[] noFlyZones = restHandler.fetchNoFlyZones(SystemConstants.NOFLY_URL);

   Restaurant targetRestaurant = orderValidator.getOrderRestaurant(request.getPizzasInOrder(), restaurants);
   List<LngLat> path = CalculatePath.hybridPathfinding(targetRestaurant.location(), noFlyZones);

   return ResponseEntity.status(HttpServletResponse.SC_OK).body(path);
  }

  /**
   * Endpoint to calculate the delivery path for an order and return the result as GeoJSON.
   *
   * @param request A JSON request containing an order (Order).
   * @return the calculated delivery path as GeoJSON format.
   */
@PostMapping("/calcDeliveryPathAsGeoJson")
  public ResponseEntity<?> calcDeliveryPathAsGeoJson(@RequestBody Order request){
  OrderValidationImpl orderValidator = new OrderValidationImpl();
  Order result;

  Restaurant[] restaurants;

  restaurants = fetchRestaurants(SystemConstants.RESTAURANT_URL);
  SystemConstants.CENTRAL_REGION = fetchCentralRegion(SystemConstants.CENTRALAREA_URL);

  if(restaurants == null){
    return ResponseEntity.status(HttpServletResponse.SC_BAD_REQUEST).body("No response from restaurant website");
  }
  result = orderValidator.validateOrder(request, restaurants);

  if(result.getOrderValidationCode() != OrderValidationCode.NO_ERROR){
    OrderValidationResult orderValidationResult = new OrderValidationResult(result.getOrderStatus(), result.getOrderValidationCode());
    return ResponseEntity.status(HttpServletResponse.SC_BAD_REQUEST).body(orderValidationResult);
  }

  Region[] noFlyZones = restHandler.fetchNoFlyZones(SystemConstants.NOFLY_URL);

  Restaurant targetRestaurant = orderValidator.getOrderRestaurant(request.getPizzasInOrder(), restaurants);
  List<LngLat> path = CalculatePath.hybridPathfinding(targetRestaurant.location(), noFlyZones);
  String pathAsGeoJson = GeoJsonHandler.mapToGeoJson(path);

  return ResponseEntity.status(HttpServletResponse.SC_OK).body(pathAsGeoJson);

  }
}


