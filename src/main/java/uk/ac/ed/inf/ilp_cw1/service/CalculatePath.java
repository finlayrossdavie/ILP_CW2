package uk.ac.ed.inf.ilp_cw1.service;

import static uk.ac.ed.inf.ilp_cw1.Data.SystemConstants.ANGLES;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import uk.ac.ed.inf.ilp_cw1.Data.Node;
import uk.ac.ed.inf.ilp_cw1.Data.Position;
import uk.ac.ed.inf.ilp_cw1.Data.SystemConstants;

import java.util.*;

  public class CalculatePath {


    // Get valid neighboring nodes
    public static Set<Node> getValidNeighbours(Node targetNode) {
      Set<Node> neighbours = new HashSet<>();
      for (Double angle : ANGLES) {
        Position tempPosition = Calculations.nextPos(targetNode.getPosition(), angle);

        // Validate position early to avoid unnecessary processing
        if (Validations.isValid(tempPosition)) {
          Node tempNode = new Node(targetNode, tempPosition);
          neighbours.add(tempNode);
        }
      }
      return neighbours;
    }

    // Backtrack from the goal to reconstruct the path
    public static List<Position> backtrack(Node node) {
      List<Position> path = new LinkedList<>();
      while (node != null) {
        path.add(0, node.getPosition()); // Add to the beginning for correct order
        node = node.getParent();
      }
      return path;
    }

    // A* search algorithm with optimized data structures and logic
    public static List<Position> astarSearch(Position start) {
      // Validate the starting position
      if (!Validations.isValid(start)) {
        throw new IllegalArgumentException("Invalid starting position");
      }

      Node startNode = new Node(null, start);
      Position goalPosition = SystemConstants.APPLETON_POS;

      // Priority queue with a comparator for lowest F-value
      PriorityQueue<Node> openList = new PriorityQueue<>(Comparator.comparingDouble(Node::getF));
      Map<Position, Node> openListMap = new HashMap<>();
      Map<Position, Node> closedList = new HashMap<>();

      // Initialize start node
      startNode.setG(0.0);
      startNode.setH(Calculations.eucDistance(start, goalPosition));
      startNode.setF(startNode.getG() + startNode.getH());
      openList.add(startNode);
      openListMap.put(start, startNode);


      while (!openList.isEmpty()) {
        // Get the node with the lowest F-value
        Node currentNode = openList.poll();
        openListMap.remove(currentNode.getPosition());
        closedList.put(currentNode.getPosition(), currentNode);

        // Check if the goal is reached
        if (goalPosition.isEqual(currentNode.getPosition())) {
          System.out.printf("Final Position Found! Lat: %f, Lng: %f", currentNode.getPosition().getLat(), currentNode.getPosition().getLng());
          return backtrack(currentNode);
        }

        // Process valid neighbors
        for (Node neighbour : getValidNeighbours(currentNode)) {
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
            neighbour.setH(Calculations.eucDistance(neighbour.getPosition(), goalPosition));
            neighbour.setF(neighbour.getG() + neighbour.getH());

            // Add neighbor to the open list if not already there
            if (!openListMap.containsKey(neighbour.getPosition())) {
              openList.add(neighbour);
              openListMap.put(neighbour.getPosition(), neighbour);
              System.out.printf("Added node: Lng: %f Lat: %f Distance to goal: %f \n", neighbour.getPosition().getLng(), neighbour.getPosition().getLat(), Calculations.eucDistance(neighbour.getPosition(), goalPosition));
            }

          }
        }
      }

      // If we exit the loop, no path was found
      throw new RuntimeException("No path found to the target position");
    }
  }
