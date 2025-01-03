package uk.ac.ed.inf.ilp_cw1.service;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import uk.ac.ed.inf.ilp_cw1.Data.Node;
import uk.ac.ed.inf.ilp_cw1.Data.Position;
import uk.ac.ed.inf.ilp_cw1.Data.SystemConstants;

public class CalculatePath {

  public static final double[] ANGLES = {
      0, 22.5, 45, 67.5, 90, 112.5, 135, 157.5,
      180, 202.5, 225, 247.5, 270, 292.5, 315, 337.5
  };

  public Set<Node> getValidNeighbours(Node targetNode){
    Set<Node> neighbours = new HashSet<>();


    for(double angle : ANGLES){
      Position tempPosition = Calculations.nextPos(targetNode.getPosition(), angle);

      if (!(SystemConstants.CENTRAL_REGION.isInside(tempPosition))){
        Node tempNode = new Node(targetNode, tempPosition);
        neighbours.add(tempNode);
      }
    }

    return neighbours;
  }

  public Node getLowestFValue(Set<Node> list){

    Node lowestNode = new Node(null, null);
    lowestNode.setF(100000);

    for(Node item : list){
      if(item.getF() < lowestNode.getF()){
        lowestNode = item;
      }
    }

    return lowestNode;
  }

  public Set<Position> backtrack(Node node){
    Set<Position> path = new HashSet<>();
    path.add(node.getPosition());

    while(node.getParent()!=null){
      node = node.getParent();
      path.add(node.getPosition());
    }
    return path;
  }
  public Set<Position> astarSearch(Position start, Position end){
    Set<Position> path = null;

    Node startNode = new Node(null, start);
    Node endNode = new Node(null, end);

    Set<Node> open_list = new HashSet<>();
    Set<Node> closed_list = null;

    open_list.add(startNode);

    while (open_list.size() > 0){
      Node currentNode = getLowestFValue(open_list);

      open_list.remove(currentNode);
      closed_list.add(currentNode);

      if (end == currentNode.getPosition()){
        path = backtrack(currentNode);
      }

      Set<Node> neighbours = getValidNeighbours(currentNode);

      for(Node neighbour : neighbours){

        if(closed_list.contains(neighbour)){
          break;
        }

        neighbour.setG(currentNode.getG()+Calculations.eucDistance(currentNode.getPosition(), neighbour.getPosition()));
        neighbour.setH(Calculations.eucDistance(neighbour.getPosition(), end));
        neighbour.setF(neighbour.getG()+ neighbour.getH());

        if(open_list.contains(neighbour)){
          if(neighbour.getG() > currentNode.getG()){
            continue;
          }
        }
        open_list.add(neighbour);
      }
    }
    return path;
  }
}