package uk.ac.ed.inf.ilp_cw1.service;
import static uk.ac.ed.inf.ilp_cw1.service.Validations.isValid;

import java.util.Objects;
import uk.ac.ed.inf.ilp_cw1.Data.Position;
import uk.ac.ed.inf.ilp_cw1.Data.Region;

public class Calculations {

  public static double eucDistance(Position p1, Position p2){
    double distance = Math.sqrt(Math.pow(p1.getLat()- p2.getLat(),2)+Math.pow(p1.getLng()- p2.getLng(),2));

    return distance;
  }

  public static Position nextPos(Position p1, Double angle){
    double new_lng =  p1.getLng() + 0.00015*Math.sin(angle);
    double new_lat =  p1.getLat() + 0.00015*Math.cos(angle);


    Position result = new Position();
    result.setLat(new_lat);
    result.setLng(new_lng);

    return result;
  }

  public static boolean colinear(Position[] vertices) {

    int n = vertices.length;

    if (!(vertices[0].isEqual(vertices[n - 1])))
      return false;

    boolean x_colinear = true;
    boolean y_colinear = true;

    int i = 0;

    while (x_colinear || y_colinear && i < n - 2) {

      if (!Objects.equals(vertices[i].getLng(), vertices[i + 1].getLng())) {
        x_colinear = false;
      }

      if (!Objects.equals(vertices[i].getLat(), vertices[i + 1].getLat())) {
        y_colinear = false;
      }
      i++;
    }
    return x_colinear || y_colinear;
  }

  public static boolean isIn(Position position, Region region){
    return isValid(position) && isValid(region);
  }
  }


