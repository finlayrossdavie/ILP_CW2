package uk.ac.ed.inf.ilp_cw1.service;
import com.mapbox.geojson.*;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import uk.ac.ed.inf.ilp_cw1.Data.Position;

public class GeoJsonHandler {

  public static List<Point> convertToGeoJsonPoints(List<Position> positions) {
      if (positions == null || positions.isEmpty()) {
        throw new IllegalArgumentException("Position list cannot be null or empty.");
      }

      return positions.stream()
          .map(position -> {
            if (position.getLng() == null || position.getLat() == null) {
              throw new IllegalArgumentException("Position must have non-null longitude and latitude values.");
            }
            return Point.fromLngLat(position.getLng(), position.getLat());
          })
          .collect(Collectors.toList());
    }

    public static String mapToGeoJson(List<Position> positions) {
      if (positions == null || positions.isEmpty()) {
        // Return an empty FeatureCollection for better compatibility
        return FeatureCollection.fromFeatures(Collections.emptyList()).toJson();
      }

      List<Point> pointList = convertToGeoJsonPoints(positions);

      // Create a LineString from the points
      LineString lineString = LineString.fromLngLats(pointList);

      // Wrap the LineString in a Feature
      Feature feature = Feature.fromGeometry(lineString);

      // Wrap the Feature in a FeatureCollection
      FeatureCollection featureCollection = FeatureCollection.fromFeature(feature);

      return featureCollection.toJson();
    }
  }