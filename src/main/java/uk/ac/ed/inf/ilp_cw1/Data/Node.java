package uk.ac.ed.inf.ilp_cw1.Data;

public class Node {
  Node parent;
  LngLat lngLat;

  Double g;
  Double h;
  Double f;

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

