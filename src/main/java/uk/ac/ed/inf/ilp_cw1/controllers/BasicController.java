package uk.ac.ed.inf.ilp_cw1.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BasicController {

  @GetMapping("/isAlive")
  public boolean isAlive(){
    return true;
  }

  @GetMapping("/studentId/{name}")
  public String studentId(@PathVariable String name){
    if (name.equalsIgnoreCase("Cameron")){
      return "s2562095";
    }
    else{
      return "You're name is not in our register, sorry!";
    }
  }

}
