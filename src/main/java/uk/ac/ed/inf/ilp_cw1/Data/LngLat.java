package uk.ac.ed.inf.ilp_cw1.Data;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import lombok.Getter;

import lombok.Setter;

@Getter
@Setter


public class LngLat {
  @JsonProperty("lng")
  private Double lng;

  @JsonProperty("lat")
  private Double lat;

  public boolean isEqual(LngLat obj) {
    return (Math.abs(this.lat - obj.lat) <= SystemConstants.DRONE_IS_CLOSE_DISTANCE) && (Math.abs(this.lng - obj.lng) <= SystemConstants.DRONE_IS_CLOSE_DISTANCE);
  }

  @Override
  public boolean equals(Object obj){
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    LngLat lngLat = (LngLat) obj;
    return (Objects.equals(this.lat, lngLat.lat)) && (Objects.equals(this.lng, lngLat.lng)); // Tolerance for floating-point comparisons
  }
  @Override
  public int hashCode(){
    return Objects.hash(Math.round(lat * SystemConstants.DRONE_IS_CLOSE_DISTANCE), Math.round(lng * SystemConstants.DRONE_IS_CLOSE_DISTANCE));
  }
}
