package uk.ac.ed.inf.ilp_cw1.controllers;

import static uk.ac.ed.inf.ilp_cw1.service.Calculations.collinear;
import static uk.ac.ed.inf.ilp_cw1.service.Calculations.nextPos;
import static uk.ac.ed.inf.ilp_cw1.service.Validations.isValid;
import static uk.ac.ed.inf.ilp_cw1.service.restaurantHandler.fetchRestaurants;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import uk.ac.ed.inf.ilp_cw1.Data.LngLatPairRequest;
import uk.ac.ed.inf.ilp_cw1.Data.NextPositionRequest;
import uk.ac.ed.inf.ilp_cw1.Data.Order;
import uk.ac.ed.inf.ilp_cw1.Data.OrderValidationCode;
import uk.ac.ed.inf.ilp_cw1.Data.Restaurant;
import uk.ac.ed.inf.ilp_cw1.Data.SystemConstants;
import uk.ac.ed.inf.ilp_cw1.Data.isInRegionRequest;
import uk.ac.ed.inf.ilp_cw1.Data.Position;
import uk.ac.ed.inf.ilp_cw1.service.CalculatePath;
import uk.ac.ed.inf.ilp_cw1.service.Calculations;
import uk.ac.ed.inf.ilp_cw1.service.GeoJsonHandler;
import uk.ac.ed.inf.ilp_cw1.service.OrderValidation;
import uk.ac.ed.inf.ilp_cw1.service.OrderValidationImpl;




@RestController
public class BasicController {

  @GetMapping("/isAlive")
  public boolean isAlive(){
    return true;
  }

  @GetMapping("/uuid")
  public String uuid(){
    return "s2281183";
  }

  @PostMapping("/distanceTo")
  public ResponseEntity<?> distanceTo(@RequestBody LngLatPairRequest request){
    if (request == null || !isValid(request.getPosition1()) || !isValid(request.getPosition2())){              //TODO: Improve poor input handling, in particular null lat and lng
      return ResponseEntity.status(HttpServletResponse.SC_BAD_REQUEST).body("Invalid JSON structure");
    }

    Position p1 = request.getPosition1();
    Position p2 = request.getPosition2();

    Double distance = Calculations.eucDistance(p1, p2);

    return ResponseEntity.status(HttpServletResponse.SC_OK).body(distance);

  }

  @PostMapping("/isCloseTo")
  public ResponseEntity<?> isCloseTo(@RequestBody LngLatPairRequest request){
    if (request == null || !isValid(request.getPosition2()) || !isValid(request.getPosition1())){              //TODO: Improve poor input handling, in particular null lat and lng
      return ResponseEntity.status(HttpServletResponse.SC_BAD_REQUEST).body("Invalid JSON structure");
    }

    Double distance = Calculations.eucDistance(request.getPosition1(), request.getPosition2());

    if (distance < 0.00015){
      return ResponseEntity.status(HttpServletResponse.SC_OK).body(true);
    }
    else{
      return ResponseEntity.status(HttpServletResponse.SC_OK).body(false);
    }
  }

  @PostMapping("/nextPosition")
  public ResponseEntity<?> nextPosition(@RequestBody NextPositionRequest nextPositionRequest){
    if (nextPositionRequest == null || !isValid(nextPositionRequest.getPosition()) || !isValid(nextPositionRequest.getAngle())){
      return ResponseEntity.status(HttpServletResponse.SC_BAD_REQUEST).body("Invalid input");
    }

    Position position = nextPositionRequest.getPosition();
    Double angle = nextPositionRequest.getAngle();

    Position result = nextPos(position,angle);

    return ResponseEntity.status(HttpServletResponse.SC_OK).body(result);
  }

  @PostMapping("/isInRegion")
  public ResponseEntity<?> isInRegion(@RequestBody isInRegionRequest request){
    if (request == null || !isValid(request.getPosition()) || !isValid(request.getRegion())){
      return ResponseEntity.status(HttpServletResponse.SC_BAD_REQUEST).body("Invalid input");
    }

    if (collinear(request.getRegion().getVertices())){
      return ResponseEntity.status(HttpServletResponse.SC_BAD_REQUEST).body("Points are collinear, thus invalid region");
    }

    return ResponseEntity.status(HttpServletResponse.SC_OK).body(request.getRegion().isInside(request.getPosition()));

  }
  @PostMapping("/validateOrder")
  public ResponseEntity<?> validateOrder(@RequestBody Order request){
    OrderValidationImpl orderValidator = new OrderValidationImpl();
    Order result = null;

    Restaurant[] restaurants = null;

    restaurants = fetchRestaurants();

    if(restaurants == null){
      return ResponseEntity.status(HttpServletResponse.SC_BAD_REQUEST).body("No response from restaurant website");
    }
    result = orderValidator.validateOrder(request, restaurants);

    if(result == null){
     return ResponseEntity.status(HttpServletResponse.SC_BAD_REQUEST).body("No pizza's found");
   }

   return ResponseEntity.status(HttpServletResponse.SC_OK).body(result.getOrderValidationCode());
  }
@PostMapping("/calcDeliveryPath")
  public ResponseEntity<?> calcDeliveryPath(@RequestBody Order request){
    OrderValidationImpl orderValidator = new OrderValidationImpl();
    Order result = null;

    Restaurant[] restaurants = null;

    restaurants = fetchRestaurants();

   if(restaurants == null){
    return ResponseEntity.status(HttpServletResponse.SC_BAD_REQUEST).body("No response from restaurant website");
  }
   result = orderValidator.validateOrder(request, restaurants);

   if(result.getOrderValidationCode() != OrderValidationCode.NO_ERROR){
     return ResponseEntity.status(HttpServletResponse.SC_OK).body("Order Invalid");
   }

   Restaurant targetRestaurant = orderValidator.getOrderRestaurant(request.getPizzasInOrder(), restaurants);
   List<Position> path = CalculatePath.astarSearch(targetRestaurant.location());

   return ResponseEntity.status(HttpServletResponse.SC_OK).body(path);
  }
@PostMapping("/calcDeliveryPathAsGeoJson")
  public ResponseEntity<?> calcDeliveryPathAsGeoJson(@RequestBody Order request){
  OrderValidationImpl orderValidator = new OrderValidationImpl();
  Order result = null;

  Restaurant[] restaurants = null;

  restaurants = fetchRestaurants();

  if(restaurants == null){
    return ResponseEntity.status(HttpServletResponse.SC_BAD_REQUEST).body("No response from restaurant website");
  }
  result = orderValidator.validateOrder(request, restaurants);

  if(result.getOrderValidationCode() != OrderValidationCode.NO_ERROR){
    return ResponseEntity.status(HttpServletResponse.SC_OK).body("Order Invalid");
  }

  Restaurant targetRestaurant = orderValidator.getOrderRestaurant(request.getPizzasInOrder(), restaurants);
  List<Position> path = CalculatePath.astarSearch(targetRestaurant.location());

  return ResponseEntity.status(HttpServletResponse.SC_OK).body(GeoJsonHandler.convertToGeoJsonPoints(path));

  }
}


