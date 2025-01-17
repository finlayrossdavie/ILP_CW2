package uk.ac.ed.inf.ilp_cw1.service;


import uk.ac.ed.inf.ilp_cw1.Data.LngLat;
import uk.ac.ed.inf.ilp_cw1.Data.Region;
/*
Validations provides methods to validate the basic
data objects implemented in this application: coordinates,
angles and regions
 */


public class Validations {

  /**
   * Validates the provided LngLat object.
   *
   * @param lngLat the LngLat object containing latitude and longitude values to validate
   * @return true if the LngLat object is valid; false otherwise
   *
   * A valid LngLat object must:
   * - Be non-null
   * - Have non-null latitude and longitude values
   * - Have a latitude value in the range [55, 58)
   * - Have a longitude value in the range (-5, -2]
   */

  public static boolean isValid(LngLat lngLat){

    if (lngLat == null){
      return false;
    }

    if(lngLat.getLat()==null || lngLat.getLng()==null){
      return false;
    }

    if ((lngLat.getLat() >= 58) || (lngLat.getLat() < 55)){
      return false;
    }

    if((lngLat.getLng() > -2) || (lngLat.getLng() <= -5)){
      return false;
    }

    return true;
  }

  /**
   * Validates the provided angle.
   *
   * @param angle the angle in degrees to validate
   * @return true if the angle is valid; false otherwise
   *
   * A valid angle must:
   * - Be non-null
   * - Be in the range [0.0, 360.0], or be of value 900 which indicates the hover action
   */
  public static boolean isValid(Double angle){
    if (angle==null) {
      return false;
    }

    if((angle > 360.0) || (angle < 0.0)){
      return angle == 900;
    }

    return true;
  }

  /**
   * Validates the provided Region object.
   *
   * @param region the Region object containing vertices that define its boundary
   * @return true if the Region is valid; false otherwise
   *
   * A valid Region object must:
   * - Have at least 3 vertices
   * - Have the first and last vertices identical (to form a closed polygon)
   */

  public static boolean isValid(Region region) {

    LngLat[] vertices = region.getVertices();

    int n = vertices.length;

    if(n < 3){
      return false;
    }

    return vertices[0].isEqual(vertices[n - 1]);
  }
}
