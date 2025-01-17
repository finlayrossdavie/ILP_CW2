package uk.ac.ed.inf.ilp_cw1.Data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
/**
 * Represents a request to calculate the next position based on a starting location (LngLat)
 * and an angle for movement.
 * <p>
 * This class is typically used for passing a starting point and an angle to a service that
 * calculates the next position in the specified direction.
 * </p>
 */
@Getter
@Setter
public class NextPositionRequest {
  /**
   * The starting point of the request, represented by latitude and longitude (LngLat).
   * This defines the current position from which movement will begin.
   */
  @JsonProperty("start")
  private LngLat lngLat;
  /**
   * The angle (in degrees) that represents the direction in which to calculate the next position.
   * This angle is used to determine how the new position is calculated relative to the start point.
   */
  @JsonProperty("angle")
  private Double angle;

}
