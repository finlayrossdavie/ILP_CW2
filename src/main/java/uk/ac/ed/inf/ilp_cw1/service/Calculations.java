package uk.ac.ed.inf.ilp_cw1.service;
import static uk.ac.ed.inf.ilp_cw1.service.Validations.isValid;

import uk.ac.ed.inf.ilp_cw1.Data.Position;

public class Calculations {

  public static double eucDistance(Position p1, Position p2){
    double distance = Math.sqrt(Math.pow(p1.getLat()- p2.getLat(),2)+Math.pow(p1.getLng()- p2.getLng(),2));

    return distance;
  }

  public static Position nextPos(Position p1, Double angle){

    if (angle==900){
      return p1;
    }

    double new_lng =  p1.getLng() + 0.00015*Math.sin(angle);
    double new_lat =  p1.getLat() + 0.00015*Math.cos(angle);


    Position result = new Position();
    result.setLat(new_lat);
    result.setLng(new_lng);

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
    double area = a.getLat() * (b.getLng() - c.getLng()) +
        b.getLat() * (c.getLng() - a.getLng()) +
        c.getLat() * (a.getLng() - b.getLng());

    // Check if the slopes are approximately equal
    return Math.abs(area) < epsilon;
  }
  }


