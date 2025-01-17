package uk.ac.ed.inf.ilp_cw1.Data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

/**
 * Represents a request that contains two positions
 * This class is typically used for passing two locations (latitudes and longitudes)
 * to a service for comparison or further calculations (such as distance calculation).
 */

public class LngLatPairRequest {

  /**
   * The first position in the pair, represented by latitude and longitude (LngLat).
   * This defines the first location.
   */
  @JsonProperty("position1")
  private LngLat lngLat1;

  /**
   * The second position in the pair, represented by latitude and longitude (LngLat).
   * This defines the second location.
   */

  @JsonProperty("position2")
  private LngLat lngLat2;

}