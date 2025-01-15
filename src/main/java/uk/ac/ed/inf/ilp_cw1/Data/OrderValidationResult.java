package uk.ac.ed.inf.ilp_cw1.Data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

public class OrderValidationResult {
  @Getter
  @Setter

  @JsonProperty("orderStatus")
  private OrderStatus orderStatus;

  @JsonProperty("orderValidationCode")
  private OrderValidationCode orderValidationCode;

  public OrderValidationResult(OrderStatus orderStatus, OrderValidationCode orderValidationCode) {
    this.orderStatus = orderStatus;
    this.orderValidationCode = orderValidationCode;
  }
}
