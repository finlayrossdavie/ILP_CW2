package uk.ac.ed.inf.ilp_cw1.Data;

public class Node {
  Node parent;
  Position position;

  double g;
  double h;
  double f;

  public Node(Node parent, Position position){
    this.parent = parent;
    this.position = position;

    this.f = 0.0;
    this.g = 0.0;
    this.h = 0.0;

  }

  public Position getPosition() {
    return position;
  }

  public Node getParent() {
    return parent;
  }

  public double getF() {
    return f;
  }

  public double getG() {
    return g;
  }

  public double getH() {
    return h;
  }

  public void setF(double f) {
    this.f = f;
  }

  public void setG(double g) {
    this.g = g;
  }
  public void setH(double h) {
    this.h = h;
  }
}
