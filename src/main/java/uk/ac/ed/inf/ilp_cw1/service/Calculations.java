package uk.ac.ed.inf.ilp_cw1.service;
import static uk.ac.ed.inf.ilp_cw1.service.Validations.isValid;

import java.util.HashMap;
import java.util.Map;
import uk.ac.ed.inf.ilp_cw1.Data.LngLat;
import uk.ac.ed.inf.ilp_cw1.Data.SystemConstants;

/**
 * Utility class for performing various calculations relating to
 * positions
 */

public class Calculations {

  /**
   * A precomputed map of trigonometric values (sine and cosine) for specified angles.
   * This is used for efficient angle-based calculations.
   * Used to speed up calculations
   */

  private static final Map<Double, Double[]> PRECOMPUTED_TRIG = new HashMap<>();
  static {
    for (Double angle : SystemConstants.ANGLES) {
      PRECOMPUTED_TRIG.put(angle, new Double[] {
          Math.sin(Math.toRadians(angle)),
          Math.cos(Math.toRadians(angle))
      });
    }
  }

  /**
   * Calculates the Euclidean distance between two points represented as {@code LngLat} objects.
   *
   * @param p1 the first point
   * @param p2 the second point
   * @return the Euclidean distance between p1 and p2
   */

  public static double eucDistance(LngLat p1, LngLat p2) {
    Double latDiff = p1.getLat() - p2.getLat();
    Double lngDiff = p1.getLng() - p2.getLng();
    return Math.sqrt(latDiff * latDiff + lngDiff * lngDiff);
  }

  /**
   * Calculates the next position of a point after moving in a specified direction.
   * The movement is based on a precomputed trigonometric map for efficiency.
   *
   * @param p1    the starting point
   * @param angle the direction of movement in degrees; if 900, the point does not move
   * @return a new LngLat object representing the next position
   */
  public static LngLat nextPos(LngLat p1, Double angle){

    if (angle==900){
      return p1;
    }

    Double[] trigValues = PRECOMPUTED_TRIG.get(angle);
    Double newLng = p1.getLng() + 0.00015 * trigValues[0];
    Double newLat = p1.getLat() + 0.00015 * trigValues[1];

    LngLat result = new LngLat();
    result.setLat(newLat);
    result.setLng(newLng);
    return result;
  }
  /**
   * Checks if all the vertices in a polygon are collinear.
   *
   * @param vertices an array of LngLat vertices representing a polygon
   * @return true if all vertices are collinear, otherwise false
   */
  public static boolean collinear(LngLat[] vertices) {
    int n = vertices.length;


    for (int i = 1; i < n - 1; i++) {
      if (!areCollinear(vertices[i - 1], vertices[i], vertices[i + 1])) {
        return false;  // If any set of three points is not collinear, return false
      }
    }

    // Close the polygon by checking the last vertex and the first two vertices
    return areCollinear(vertices[n - 2], vertices[n - 1], vertices[0]);
  }
  /**
   * Helper function used by the collinear method which
   * determines if three points are collinear using the area of
   * the triangle they form.
   *
   * @param a the first point
   * @param b the second point
   * @param c the third point
   * @return true if the points are collinear, otherwise false
   */

  public static boolean areCollinear(LngLat a, LngLat b, LngLat c) {
    double epsilon = 1e-6;  // Tolerance for floating-point comparisons

    // Calculate the slope difference between points
    Double area = a.getLat() * (b.getLng() - c.getLng()) +
        b.getLat() * (c.getLng() - a.getLng()) +
        c.getLat() * (a.getLng() - b.getLng());

    // Check if the slopes are approximately equal
    return Math.abs(area) < epsilon;
  }
  }


