package uk.ac.ed.inf.ilp_cw1.service;
import static uk.ac.ed.inf.ilp_cw1.service.Validations.isValid;

import java.util.HashMap;
import java.util.Map;
import uk.ac.ed.inf.ilp_cw1.Data.Position;
import uk.ac.ed.inf.ilp_cw1.Data.SystemConstants;


public class Calculations {

  private static final Map<Double, Double[]> PRECOMPUTED_TRIG = new HashMap<>();
  static {
    for (Double angle : SystemConstants.ANGLES) {
      PRECOMPUTED_TRIG.put(angle, new Double[] {
          Math.sin(Math.toRadians(angle)),
          Math.cos(Math.toRadians(angle))
      });
    }
  }

  public static double eucDistance(Position p1, Position p2) {
    Double latDiff = p1.getLat() - p2.getLat();
    Double lngDiff = p1.getLng() - p2.getLng();
    return Math.sqrt(latDiff * latDiff + lngDiff * lngDiff);
  }


  public static Position nextPos(Position p1, Double angle){

    if (angle==900){
      return p1;
    }

    Double[] trigValues = PRECOMPUTED_TRIG.get(angle);
    Double newLng = p1.getLng() + 0.00015 * trigValues[0];
    Double newLat = p1.getLat() + 0.00015 * trigValues[1];

    Position result = new Position();
    result.setLat(newLat);
    result.setLng(newLng);
    return result;
  }

  public static boolean collinear(Position[] vertices) {
    int n = vertices.length;


    for (int i = 1; i < n - 1; i++) {
      if (!areCollinear(vertices[i - 1], vertices[i], vertices[i + 1])) {
        return false;  // If any set of three points is not collinear, return false
      }
    }

    // Close the polygon by checking the last vertex and the first two vertices
    return areCollinear(vertices[n - 2], vertices[n - 1], vertices[0]);
  }


  public static boolean areCollinear(Position a, Position b, Position c) {
    double epsilon = 1e-6;  // Tolerance for floating-point comparisons

    // Calculate the slope difference between points
    Double area = a.getLat() * (b.getLng() - c.getLng()) +
        b.getLat() * (c.getLng() - a.getLng()) +
        c.getLat() * (a.getLng() - b.getLng());

    // Check if the slopes are approximately equal
    return Math.abs(area) < epsilon;
  }
  }


