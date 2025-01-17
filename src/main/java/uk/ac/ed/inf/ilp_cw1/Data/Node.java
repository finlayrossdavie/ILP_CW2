package uk.ac.ed.inf.ilp_cw1.Data;

public class Node {
  /**
   * Represents a node in the A* search algorithm, storing the node's position (LngLat),
   * parent node, and the values used to calculate the node's cost (f, g, h).
   */

  /**
   * The parent node of this node, used to trace the path back to the start node.
   */
  Node parent;
  /**
   * The position of the node, represented by latitude and longitude.
   */
  LngLat lngLat;
  /**
   * The cost from the start node to this node.
   * This value is used in pathfinding algorithms to evaluate the total cost of a node.
   */
  Double g;
  /**
   * The heuristic cost from this node to the goal node.
   * This is an estimate of the remaining distance.
   */
  Double h;

  /**
   * The total cost (f) of this node, calculated as f = g + h.
   * f is used in A* to prioritize nodes.
   */
  Double f;

  /**
   * Constructs a new node with the specified parent node and position (LngLat).
   * The cost values (f, g, h) are initialized to 0.0.
   *
   * @param parent The parent node of this node, used to trace the path back.
   * @param lngLat The position of the node, represented by latitude and longitude.
   */


  public Node(Node parent, LngLat lngLat){
    this.parent = parent;
    this.lngLat = lngLat;

    this.f = 0.0;
    this.g = 0.0;
    this.h = 0.0;

  }


  public LngLat getPosition() {
    return lngLat;
  }

  public Node getParent() {
    return parent;
  }

  public Double getF() {
    return f;
  }

  public Double getG() {
    return g;
  }

  public Double getH() {
    return h;
  }

  public void setF(Double f) {
    this.f = f;
  }

  public void setG(Double g) {
    this.g = g;
  }
  public void setH(Double h) {
    this.h = h;
  }

  public void setParent(Node parent) {
    this.parent = parent;
  }

  public void setPosition(LngLat lngLat) {
    this.lngLat = lngLat;
  }
}

