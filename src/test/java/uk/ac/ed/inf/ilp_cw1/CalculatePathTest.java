package uk.ac.ed.inf.ilp_cw1;

import java.util.List;
import org.junit.jupiter.api.Test;
import uk.ac.ed.inf.ilp_cw1.Data.LngLat;
import uk.ac.ed.inf.ilp_cw1.Data.Region;
import uk.ac.ed.inf.ilp_cw1.Data.SystemConstants;
import uk.ac.ed.inf.ilp_cw1.service.CalculatePath;

import uk.ac.ed.inf.ilp_cw1.service.Calculations;
import uk.ac.ed.inf.ilp_cw1.service.GeoJsonHandler;
import uk.ac.ed.inf.ilp_cw1.service.restHandler;

import static org.junit.jupiter.api.Assertions.*;


public class CalculatePathTest {
/*
  @Test
  public void testGetValidNeighbours() {
    Position testPosition = new Position();
    testPosition.setLat(3.1);
    testPosition.setLng(55.1);

    Node targetNode = new Node(null, testPosition);

    boolean enteredCentral = false;

    // Implement a mock CENTRAL_REGION

    // Call the method
    Set<Node> neighbours = CalculatePath.getValidNeighbours(targetNode, enteredCentral);

    // Assert that the neighbors are valid
    assertFalse(neighbours.isEmpty());
    for (Node neighbour : neighbours) {
      assertNotNull(neighbour.getPosition());
      assertFalse(SystemConstants.CENTRAL_REGION.isInside(neighbour.getPosition()));
    }
  }


  @Test
  public void testGetLowestFValue() {
    Position testPosition = new Position();
    testPosition.setLng(3.1);
    testPosition.setLat(55.1);
    Node node1 = new Node(null, testPosition);
    node1.setF(10);

    Node node2 = new Node(null, testPosition);
    node2.setF(5);

    Node node3 = new Node(null, testPosition);
    node3.setF(15);

    Set<Node> nodeSet = Set.of(node1, node2, node3);

    Node result = CalculatePath.getLowestFValue(nodeSet);

    assertEquals(node2, result, "The node with the lowest F value should be returned.");
  }

  @Test
  public void testBacktrack() {

    Position testPosition1 = new Position();
    testPosition1.setLng(3.1);
    testPosition1.setLat(55.1);

    Position testPosition2 = new Position();
    testPosition2.setLng(3.15);
    testPosition2.setLat(55.13);

    Position testPosition3 = new Position();
    testPosition3.setLng(3.16);
    testPosition3.setLat(55.15);

    Node node3 = new Node(null, testPosition1);
    Node node2 = new Node(node3, testPosition2);
    Node node1 = new Node(node2, testPosition3);

    Set<Position> path = CalculatePath.backtrack(node1);

    assertEquals(3, path.size(), "The backtracked path should contain all parent nodes.");
    assertTrue(path.contains(testPosition1));
    assertTrue(path.contains(testPosition2));
    assertTrue(path.contains(testPosition3));
  }
*/
  @Test
  public void testAStarSearch() {
    // Define test data
    LngLat test_start = new LngLat();
    test_start.setLat(55.944369);
    test_start.setLng(-3.19026);


    // Implement a mock CENTRAL_REGION
    // Call the method
    Region[] noFlyZones = restHandler.fetchNoFlyZones(SystemConstants.NOFLY_URL);
    List<LngLat> path = CalculatePath.astarSearch(test_start, noFlyZones);

    // Assert the path is found and contains the end position
    assertNotNull(path, "The path should not be null.");
    //assertTrue(path.contains(end), "The path should contain the end position.");

    System.out.printf(path.toString());
  }
  @Test
  public void testAStarSearchToGeoJson() {
    // Define test data
    LngLat test_start = new LngLat();
    //test_start.setLat(55.943284737579376);
    //test_start.setLng(-3.202541470527649);

    //LngLat test_start = new LngLat();
    //test_start.setLat(55.945535152517735);
    //test_start.setLng(-3.1912869215011597);

    test_start.setLat(55.944369);
    test_start.setLng(-3.19026);


    // Implement a mock CENTRAL_REGION
    // Call the method

    Region[] noFlyZones = restHandler.fetchNoFlyZones(SystemConstants.NOFLY_URL);

    List<LngLat> path = CalculatePath.astarSearch(test_start, noFlyZones);
    String result = GeoJsonHandler.mapToGeoJson(path);

    //Assert the path is found
    assertNotNull(result, "The path should not be null.");
    System.out.printf(result);

  }




}