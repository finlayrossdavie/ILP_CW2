package uk.ac.ed.inf.ilp_cw1.Data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class LngLatPairRequest {
  @JsonProperty("position1")
  private LngLat lngLat1;

  @JsonProperty("position2")
  private LngLat lngLat2;

}