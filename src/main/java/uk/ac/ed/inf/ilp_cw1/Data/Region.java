package uk.ac.ed.inf.ilp_cw1.Data;

import static uk.ac.ed.inf.ilp_cw1.service.Calculations.colinear;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Region {
  @JsonProperty("name")
  private String name;

  @JsonProperty("vertices")
  private Position[] vertices;

  }
