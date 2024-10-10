package uk.ac.ed.inf.ilp_cw1.Data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class isInRegionRequest {
  @JsonProperty("position")
  private Position position;

  @JsonProperty("region")
  private Region region;
}
