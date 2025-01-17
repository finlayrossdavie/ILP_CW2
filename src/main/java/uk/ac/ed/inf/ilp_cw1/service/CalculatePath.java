package uk.ac.ed.inf.ilp_cw1.service;

import static uk.ac.ed.inf.ilp_cw1.Data.SystemConstants.ANGLES;
import static uk.ac.ed.inf.ilp_cw1.Data.SystemConstants.CENTRAL_REGION;

import java.util.HashSet;
import java.util.Set;
import uk.ac.ed.inf.ilp_cw1.Data.Node;
import uk.ac.ed.inf.ilp_cw1.Data.LngLat;
import uk.ac.ed.inf.ilp_cw1.Data.Region;
import uk.ac.ed.inf.ilp_cw1.Data.SystemConstants;

import java.util.*;

/**
 * The CalculatePath class provides methods for calculating paths while considering various constraints,
 * such as no-fly zones.
 *
 * Implements a hybrid pathfinding approach in which a straight line is drawn until
 * a point near enough to Appleton is found to perform efficient A* search
 */

public class CalculatePath {

  /**
   * Get valid neighboring nodes from the target node by checking position validity and ensuring the
   * neighboring positions are not inside no-fly zones.
   *
   * Ensures that if the drone is within the central area it cannot leave.
   *
   * @param targetNode The node for which we need to find valid neighbors.
   * @param NoFlyZones An array of no-fly zones to avoid during pathfinding.
   * @return A set of valid neighboring nodes.
   */
  // Get valid neighboring nodes
  public static Set<Node> getValidNeighbours(Node targetNode, Region[] NoFlyZones) {
    Set<Node> neighbours = new HashSet<>();
    for (Double angle : ANGLES) {
      LngLat tempLngLat = Calculations.nextPos(targetNode.getPosition(), angle);

      // Validate position early to avoid unnecessary processing
      if (Validations.isValid(tempLngLat)) {

        boolean isInsideNoFlyZone = false;

        for(Region NoFlyZone : NoFlyZones){
          if(NoFlyZone.isInside(tempLngLat)){
            isInsideNoFlyZone = true;
            break;
          }
        }
        //Skip if position is inside a no-fly zone
        if(!(isInsideNoFlyZone)){
          // Ensure if the node is within the central area its neighbour is not outside
          if(CENTRAL_REGION.isInside(targetNode.getPosition()) && CENTRAL_REGION.isInside(
              tempLngLat)){
            Node tempNode = new Node(targetNode, tempLngLat);
            neighbours.add(tempNode);
          } else if (!(CENTRAL_REGION.isInside(targetNode.getPosition()))) {
            Node tempNode = new Node(targetNode, tempLngLat);
            neighbours.add(tempNode);
          }

        }
      }
    }
    return neighbours;
  }
  /**
   * Backtrack from the given node to reconstruct the path from start to goal.
   *
   * @param node The node to start backtracking from, should be the goal node.
   * @return A list of LngLat points representing the path from start to goal.
   */

  public static List<LngLat> backtrack(Node node) {
    List<LngLat> path = new LinkedList<>();
    while (node != null) {
      path.add(0, node.getPosition()); // Add to the beginning for correct order
      node = node.getParent();
    }
    return path;
  }

  /**
   * Hybrid pathfinding algorithm that first uses a greedy approach to quickly approach the goal,
   * and then switches to A* for fine-tuned pathfinding when the goal is close.
   *
   * @param start The starting position for the pathfinding algorithm.
   * @param noFlyZones A list of no-fly zones that must be avoided.
   * @return A list of LngLat points representing the path from start to goal.
   * @throws IllegalArgumentException if the start position is invalid.
   */
  public static List<LngLat> hybridPathfinding(LngLat start, Region[] noFlyZones) {
    if (!Validations.isValid(start)) {
      throw new IllegalArgumentException("Invalid starting position");
    }

    LngLat goalLngLat = SystemConstants.APPLETON_POS;
    double thresholdDistance = 0.004; // Example threshold value in meters

    List<LngLat> path = new ArrayList<>();
    LngLat currentPosition = start;
    Node previousNode = new Node(null, start);


    // Greedy phase
    while (Calculations.eucDistance(currentPosition, goalLngLat) > thresholdDistance) {
      Node currentNode = previousNode;
      Set<Node> neighbours = getValidNeighbours(currentNode, noFlyZones);
      if (neighbours.isEmpty()) {
        throw new RuntimeException("No valid path to the goal.");
      }

      // Select the neighbor with the lowest straight-line distance to the goal
      Node bestNeighbour = neighbours.stream()
          .min(Comparator.comparingDouble(n -> Calculations.eucDistance(n.getPosition(), goalLngLat)))
          .orElseThrow();

      previousNode = new Node(currentNode, bestNeighbour.getPosition());
      currentPosition = bestNeighbour.getPosition();
      path.add(currentPosition);
    }
    //path = backtrack(previousNode);


    // Switch to A* for fine-tuned pathfinding
    List<LngLat> finalPath = astarSearch(currentPosition, noFlyZones);

    path.addAll(finalPath);

    return path;
  }

  /**
   * A* search algorithm for pathfinding, ensuring the shortest path is found while avoiding no-fly zones.
   *
   * @param start The starting position for the pathfinding algorithm.
   * @param noFlyZones A list of no-fly zones that must be avoided.
   * @return A list of LngLat points representing the path from start to goal.
   * @throws IllegalArgumentException if the start position is invalid.
   * @throws RuntimeException if no valid path can be found.
   */


  public static List<LngLat> astarSearch(LngLat start, Region[] noFlyZones) {
    // Validate the starting position
    if (!Validations.isValid(start)) {
      throw new IllegalArgumentException("Invalid starting position");
    }
    Node startNode = new Node(null, start);
    LngLat goalLngLat = SystemConstants.APPLETON_POS;

    // Priority queue with a comparator for lowest F-value
    PriorityQueue<Node> openList = new PriorityQueue<>(Comparator.comparingDouble(Node::getF));
    Map<LngLat, Node> openListMap = new HashMap<>();
    Map<LngLat, Node> closedList = new HashMap<>();

    // Initialize start node
    startNode.setG(0.0);
    startNode.setH(Calculations.eucDistance(start, goalLngLat));
    startNode.setF(startNode.getG() + startNode.getH());
    openList.add(startNode);
    openListMap.put(start, startNode);


    while (!openList.isEmpty()) {
      // Get the node with the lowest F-value
      Node currentNode = openList.poll();
      openListMap.remove(currentNode.getPosition());
      closedList.put(currentNode.getPosition(), currentNode);

      // Check if the goal is reached
      if (goalLngLat.isEqual(currentNode.getPosition())) {
        return backtrack(currentNode);
      }

      // Process valid neighbors
      for (Node neighbour : getValidNeighbours(currentNode, noFlyZones)) {
        if (closedList.containsKey(neighbour.getPosition())) {
          continue; // Skip already processed nodes
        }

        // Calculate tentative G-value
        Double tentativeG = currentNode.getG() +
            Calculations.eucDistance(currentNode.getPosition(), neighbour.getPosition());

        // If neighbor not in openList or found a better G-value
        if (!openListMap.containsKey(neighbour.getPosition()) ||
            tentativeG < openListMap.get(neighbour.getPosition()).getG()) {

          neighbour.setParent(currentNode);
          neighbour.setG(tentativeG);
          neighbour.setH(Calculations.eucDistance(neighbour.getPosition(), goalLngLat));
          neighbour.setF(neighbour.getG() + neighbour.getH());

          // Add neighbor to the open list if not already there
          if (!openListMap.containsKey(neighbour.getPosition())) {
            openList.add(neighbour);
            openListMap.put(neighbour.getPosition(), neighbour);
          }
        }
      }
    }
    // If we exit the loop, no path was found
    throw new RuntimeException("No path found to the target position");
  }
}
