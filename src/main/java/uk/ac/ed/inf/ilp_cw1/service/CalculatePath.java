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

  public class CalculatePath {


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
          if(!(isInsideNoFlyZone)){

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

    // Backtrack from the goal to reconstruct the path
    public static List<LngLat> backtrack(Node node) {
      List<LngLat> path = new LinkedList<>();
      while (node != null) {
        path.add(0, node.getPosition()); // Add to the beginning for correct order
        node = node.getParent();
      }
      return path;
    }

    // A* search algorithm with optimized data structures and logic
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
