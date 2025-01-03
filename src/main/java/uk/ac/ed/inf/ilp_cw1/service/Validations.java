package uk.ac.ed.inf.ilp_cw1.service;


import uk.ac.ed.inf.ilp_cw1.Data.Position;
import uk.ac.ed.inf.ilp_cw1.Data.Region;

public class Validations {

  public static boolean isValid(Position position){

    if (position == null){
      return false;
    }
    //TODO check out of bounds defintion

    if ((position.getLat() >= 100) || (position.getLat() <= -100)){
      return false;
    }

    if((position.getLng() > 100) || (position.getLng() <= -100)){
      return false;
    }

    return position.getLng() != null && position.getLat() != null;
  }

  public static boolean isValid(Double angle){
    if (angle==null) {
      return false;
    }

    return !(angle > 360.0) && !(angle < 0.0) || !(angle==900);
  }

  public static boolean isValid(Region region) {

    Position[] vertices = region.getVertices();

    int n = vertices.length;
/*
    if (colinear(vertices)){
      return false;
    }
*/
    if(n < 3){
      return false;
    }

    return vertices[0].isEqual(vertices[n - 1]);
  }

}
