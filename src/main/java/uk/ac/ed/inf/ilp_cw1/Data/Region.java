package uk.ac.ed.inf.ilp_cw1.Data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Region {
  @JsonProperty("name")
  private String name;

  @JsonProperty("vertices")
  private Position[] vertices;

  public static boolean isPointOnLine(Position a, Position b, Position p) {
    Double crossProduct = (p.getLat() - a.getLat()) * (b.getLng() - a.getLng()) -
        (p.getLng() - a.getLng()) * (b.getLat() - a.getLat());

    // Check if the point is on the line by verifying crossProduct is near zero (collinear)
    if (Math.abs(crossProduct) > 1e-9) {
      return false;
    }

    // Check if the point's longitude is between the endpoints' longitudes
    Double dotProduct = (p.getLng() - a.getLng()) * (p.getLng() - b.getLng()) +
        (p.getLat() - a.getLat()) * (p.getLat() - b.getLat());
    if (dotProduct > 0) {
      return false;  // If dot product is positive, the point is outside the line segment
    }

    return true;
  }

  public boolean isInside(Position point) {
    int n = this.vertices.length;
    boolean isInside = false;

    for (int i = 0, j = n - 1; i < n; j = i++) {
      // Check if the point lies exactly on an edge of the polygon
      if (isPointOnLine(this.vertices[i], this.vertices[j], point)) {
        return true;  // Point is on the border, treat it as inside
      }

      // Continue with the ray-casting logic
      if ((this.vertices[i].getLat() > point.getLat()) != (this.vertices[j].getLat() > point.getLat())) {
        Double intersectLng = this.vertices[i].getLng() +
            (this.vertices[j].getLng() - this.vertices[i].getLng()) *
                (point.getLat() - this.vertices[i].getLat()) /
                (this.vertices[j].getLat() - this.vertices[i].getLat());

        if (point.getLng() < intersectLng) {
          isInside = !isInside;
        }
      }
    }

    return isInside;
  }


  }
