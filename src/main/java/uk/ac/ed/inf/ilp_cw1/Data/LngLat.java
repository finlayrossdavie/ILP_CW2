package uk.ac.ed.inf.ilp_cw1.Data;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import lombok.Getter;

import lombok.Setter;

@Getter
@Setter

/**
 * Represents a geographic location defined by latitude and longitude (LngLat).
 * This class is used to store geographic coordinates (latitude and longitude) and provides
 * methods for comparing the equality of two `LngLat` objects, as well as calculating hash codes
 * for storage and comparison in collections.
 *
 */

public class LngLat {

  /**
   * The longitude of the geographic location.
   * Represented as a `Double`, it defines the horizontal position of the location.
   */
  @JsonProperty("lng")
  private Double lng;

  /**
   * The latitude of the geographic location.
   * Represented as a `Double`, it defines the vertical position of the location.
   */

  @JsonProperty("lat")
  private Double lat;

  /**
   * Compares the current `LngLat` object to another `LngLat` object to check if their coordinates
   * are equal within a predefined tolerance (defined by `SystemConstants.DRONE_IS_CLOSE_DISTANCE`).
   *
   * This method is used for determining if two geographic locations are "close enough" to each other
   * based on the provided tolerance distance.
   *
   *
   * @param obj The `LngLat` object to compare to the current object.
   * @return `true` if the locations are close enough, `false` otherwise.
   */

  public boolean isEqual(LngLat obj) {
    return (Math.abs(this.lat - obj.lat) <= SystemConstants.DRONE_IS_CLOSE_DISTANCE) && (Math.abs(this.lng - obj.lng) <= SystemConstants.DRONE_IS_CLOSE_DISTANCE);
  }
  /**
   * Compares the current `LngLat` object to another object for equality.
   * This method compares both the latitude and longitude values of the two `LngLat` objects.
   * The comparison uses `Objects.equals` to account for potential `null` values and tolerance for floating-point precision.
   *
   * @param obj The object to compare to.
   * @return `true` if the object is an instance of `LngLat` and has the same latitude and longitude values, `false` otherwise.
   */
  @Override
  public boolean equals(Object obj){
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    LngLat lngLat = (LngLat) obj;
    return (Objects.equals(this.lat, lngLat.lat)) && (Objects.equals(this.lng, lngLat.lng)); // Tolerance for floating-point comparisons
  }

  /**
   * Generates a hash code for the `LngLat` object based on its latitude and longitude values.
   * The hash code is computed by rounding the latitude and longitude to a specific tolerance defined
   * by `SystemConstants.DRONE_IS_CLOSE_DISTANCE` to ensure correct handling of floating-point precision.
   * @return The computed hash code for the current `LngLat` object.
   */
  @Override
  public int hashCode(){
    return Objects.hash(Math.round(lat * SystemConstants.DRONE_IS_CLOSE_DISTANCE), Math.round(lng * SystemConstants.DRONE_IS_CLOSE_DISTANCE));
  }
}
