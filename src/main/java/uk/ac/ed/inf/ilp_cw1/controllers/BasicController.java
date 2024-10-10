package uk.ac.ed.inf.ilp_cw1.controllers;


import static uk.ac.ed.inf.ilp_cw1.service.Calculations.nextPos;
import static uk.ac.ed.inf.ilp_cw1.service.Validations.isValid;

import jakarta.servlet.http.HttpServletResponse;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.http.StreamingHttpOutputMessage.Body;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;
import uk.ac.ed.inf.ilp_cw1.Data.LngLatPairRequest;
import uk.ac.ed.inf.ilp_cw1.Data.NextPositionRequest;
import uk.ac.ed.inf.ilp_cw1.Data.isInRegionRequest;
import uk.ac.ed.inf.ilp_cw1.Data.Position;
import uk.ac.ed.inf.ilp_cw1.service.Calculations;
import uk.ac.ed.inf.ilp_cw1.service.Validations;

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

    double distance = Calculations.eucDistance(p1, p2);

    return ResponseEntity.status(HttpServletResponse.SC_OK).body(distance);

  }

  @PostMapping("/isClose")
  public ResponseEntity<?> isClose(@RequestBody LngLatPairRequest request){
    if (request == null || !isValid(request.getPosition2()) || !isValid(request.getPosition1())){              //TODO: Improve poor input handling, in particular null lat and lng
      return ResponseEntity.status(HttpServletResponse.SC_BAD_REQUEST).body("Invalid JSON structure");
    }

    double distance = Calculations.eucDistance(request.getPosition1(), request.getPosition2());

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
    double angle = nextPositionRequest.getAngle();

    Position result = nextPos(position,angle);

    return ResponseEntity.status(HttpServletResponse.SC_OK).body(result);
  }

  @PostMapping("/isInRegion")
  public boolean isInRegion(@RequestBody isInRegionRequest request, HttpServletResponse response){
    if (request == null || !isValid(request.getPosition()) || !isValid(request.getRegion())){
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      return false;
    }

    return true;



  }
}


