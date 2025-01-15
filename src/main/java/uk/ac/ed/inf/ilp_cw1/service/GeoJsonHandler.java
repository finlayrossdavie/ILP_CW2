package uk.ac.ed.inf.ilp_cw1.service;
import com.mapbox.geojson.*;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import uk.ac.ed.inf.ilp_cw1.Data.LngLat;



public class GeoJsonHandler {

  public static List<Point> convertToGeoJsonPoints(List<LngLat> lngLats) {
      if (lngLats == null || lngLats.isEmpty()) {
        throw new IllegalArgumentException("Position list cannot be null or empty.");
      }

      return lngLats.stream()
          .map(position -> {
            if (position.getLng() == null || position.getLat() == null) {
              throw new IllegalArgumentException("Position must have non-null longitude and latitude values.");
            }
            return Point.fromLngLat(position.getLng(), position.getLat());
          })
          .collect(Collectors.toList());
    }

    public static String mapToGeoJson(List<LngLat> lngLats) {
      if (lngLats == null || lngLats.isEmpty()) {
        // Return an empty FeatureCollection for better compatibility
        return FeatureCollection.fromFeatures(Collections.emptyList()).toJson();
      }

      List<Point> pointList = convertToGeoJsonPoints(lngLats);

      // Create a LineString from the points
      LineString lineString = LineString.fromLngLats(pointList);

      // Wrap the LineString in a Feature
      Feature feature = Feature.fromGeometry(lineString);

      // Wrap the Feature in a FeatureCollection
      FeatureCollection featureCollection = FeatureCollection.fromFeature(feature);

      return featureCollection.toJson();
    }
  }