package uk.ac.ed.inf.ilp_cw1.service;

import static uk.ac.ed.inf.ilp_cw1.service.Calculations.colinear;

import uk.ac.ed.inf.ilp_cw1.Data.LngLatPairRequest;
import uk.ac.ed.inf.ilp_cw1.Data.Position;
import uk.ac.ed.inf.ilp_cw1.Data.Region;

public class Validations {

  public static boolean isValid(Position position){

    if (position == null){
      return false;
    }
    return position.getLng() != null && position.getLat() != null;
  }

  public static boolean isValid(Double angle){
    if (angle==null) {
      return false;
    }

    return !(angle > 360.0) && !(angle < 0.0);
  }

  public static boolean isValid(Region region) {

    Position[] vertices = region.getVertices();

    int n = vertices.length;

    if (n < 3) {
      return false;
    }

    if (!(vertices[0].isEqual(vertices[n - 1])))
      return false;

    return !colinear(vertices);
  }
}
