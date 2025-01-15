package uk.ac.ed.inf.ilp_cw1.service;


import uk.ac.ed.inf.ilp_cw1.Data.LngLat;
import uk.ac.ed.inf.ilp_cw1.Data.Region;

public class Validations {

  public static boolean isValid(LngLat lngLat){

    if (lngLat == null){
      return false;
    }

    if(lngLat.getLat()==null || lngLat.getLng()==null){
      return false;
    }

    if ((lngLat.getLat() >= 57) || (lngLat.getLat() < 56)){
      return false;
    }

    if((lngLat.getLng() > -3) || (lngLat.getLng() <= -4)){
      return false;
    }

    return true;
  }

  public static boolean isValid(Double angle){
    if (angle==null) {
      return false;
    }

    return !(angle > 360.0) && !(angle < 0.0) || !(angle==900);
  }

  public static boolean isValid(Region region) {

    LngLat[] vertices = region.getVertices();

    int n = vertices.length;

    if(n < 3){
      return false;
    }

    return vertices[0].isEqual(vertices[n - 1]);
  }

}
