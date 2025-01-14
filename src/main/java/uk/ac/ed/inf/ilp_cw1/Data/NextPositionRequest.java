package uk.ac.ed.inf.ilp_cw1.Data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NextPositionRequest {
  @JsonProperty("start")
  private LngLat lngLat;

  @JsonProperty("angle")
  private Double angle;

}
