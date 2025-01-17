package uk.ac.ed.inf.ilp_cw1.Data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents a request to check if a geographic position is within a specific region.
 * This class holds the information about the  location (position) and the region
 * where the check needs to be performed.
 */
@Getter
@Setter
public class isInRegionRequest {
  /**
   * The geographic position (latitude and longitude) to check.
   * Represented as a `LngLat` object, this field holds the coordinates of the position.
   */
  @JsonProperty("position")
  private LngLat lngLat;

  /**
   * The region to check against.
   * Represented as a `Region` object, this field defines the boundaries of the region where the check will be performed.
   */
  @JsonProperty("region")
  private Region region;
}
