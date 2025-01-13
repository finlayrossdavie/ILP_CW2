package uk.ac.ed.inf.ilp_cw1.Data;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import lombok.Getter;

import lombok.NonNull;
import lombok.Setter;
import uk.ac.ed.inf.ilp_cw1.service.Calculations;

@Getter
@Setter


public class Position {
  @JsonProperty("lng")
  private Double lng;

  @JsonProperty("lat")
  private Double lat;

  public boolean isEqual(Position obj) {
    return (Math.abs(this.lat - obj.lat) <= SystemConstants.DRONE_IS_CLOSE_DISTANCE) && (Math.abs(this.lng - obj.lng) <= SystemConstants.DRONE_IS_CLOSE_DISTANCE);
  }

  @Override
  public boolean equals(Object obj){
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    Position position = (Position) obj;
    return (Objects.equals(this.lat, position.lat)) && (Objects.equals(this.lng, position.lng)); // Tolerance for floating-point comparisons
  }
  @Override
  public int hashCode(){
    return Objects.hash(Math.round(lat * SystemConstants.DRONE_IS_CLOSE_DISTANCE), Math.round(lng * SystemConstants.DRONE_IS_CLOSE_DISTANCE));
  }
}
