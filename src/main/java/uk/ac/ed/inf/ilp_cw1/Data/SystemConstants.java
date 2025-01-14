package uk.ac.ed.inf.ilp_cw1.Data;

public final class SystemConstants {

  /**
   * the charge for any order
   */
  public static final int ORDER_CHARGE_IN_PENCE = 100;

  /**
   * the maximum number of pizzas in one order
   */
  public static final int MAX_PIZZAS_PER_ORDER = 4;

  /**
   * the distance a drone can move in 1 iteration
   */
  public static final Double DRONE_MOVE_DISTANCE = 0.00015;

  /**
   * the distance which is considered "close"
   */
  public static final Double DRONE_IS_CLOSE_DISTANCE = 0.00015;

  /**
   * the maximum moves a drone can make before running out of battery
   */
  public static final int DRONE_MAX_MOVES = 2000;

  /**
   * the central region name
   */
  public static final String CENTRAL_REGION_NAME = "central";

  /**
   * Longitude AT
   */
  public static final Double APPLETON_LNG = -3.186874;

  /**
   * Latitude AT
   */
  public static final Double APPLETON_LAT = 55.944494;

  public static final LngLat APPLETON_POS;

  static {
    APPLETON_POS = new LngLat();
    APPLETON_POS.setLat(APPLETON_LAT);
    APPLETON_POS.setLng(APPLETON_LNG);
  }
  public static final Region CENTRAL_REGION;

  static {
    LngLat[] vertices = new LngLat[4];

    vertices[0] = new LngLat();
    vertices[0].setLng(-3.192473);
    vertices[0].setLat(55.946233);

    vertices[1] = new LngLat();
    vertices[1].setLng(-3.184319);
    vertices[1].setLat(55.946233);

    vertices[2] = new LngLat();
    vertices[2].setLng(-3.192473);
    vertices[2].setLat(55.942617);

    vertices[3] = new LngLat();
    vertices[3].setLng(-3.184319);
    vertices[3].setLat(55.942617);

    CENTRAL_REGION = new Region();
    CENTRAL_REGION.setName(CENTRAL_REGION_NAME);
    CENTRAL_REGION.setVertices(vertices);
  }

  public static final Double[] ANGLES = {
      0.0, 22.5, 45.0, 67.5, 90.0, 112.5, 135.0, 157.5,
      180.0, 202.5, 225.0, 247.5, 270.0, 292.5, 315.0, 337.5
  };
  public static final String RESTAURANT_URL = "https://ilp-rest-2024.azurewebsites.net/restaurants";
public static final String NOFLY_URL = "https://ilp-rest-2024.azurewebsites.net/noFlyZones";

public static final String CENTRALAREA_URL = "https://ilp-rest-2024.azurewebsites.net/centralArea";

public static final String ORDERS_URL = "https://ilp-rest-2024.azurewebsites.net/orders";

}
