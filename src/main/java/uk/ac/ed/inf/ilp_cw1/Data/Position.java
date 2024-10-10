package uk.ac.ed.inf.ilp_cw1.Data;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import lombok.Getter;

import lombok.NonNull;
import lombok.Setter;
@Getter
@Setter


public class Position {
  @JsonProperty("lng")
  private Double lng;

  @JsonProperty("lat")
  private Double lat;

  public boolean isEqual(Position obj) {
    return Objects.equals(this.lat, obj.getLat()) && Objects.equals(this.lng, obj.getLng());
  }
}
