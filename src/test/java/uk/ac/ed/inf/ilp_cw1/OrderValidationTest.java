package uk.ac.ed.inf.ilp_cw1;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import org.junit.jupiter.api.Test;
import uk.ac.ed.inf.ilp_cw1.Data.Order;
import uk.ac.ed.inf.ilp_cw1.Data.OrderStatus;
import uk.ac.ed.inf.ilp_cw1.Data.OrderValidationCode;
import uk.ac.ed.inf.ilp_cw1.Data.Restaurant;
import uk.ac.ed.inf.ilp_cw1.Data.SystemConstants;
import uk.ac.ed.inf.ilp_cw1.service.OrderValidation;
import uk.ac.ed.inf.ilp_cw1.service.OrderValidationImpl;
import uk.ac.ed.inf.ilp_cw1.service.restHandler;

public class OrderValidationTest {

  @Test
  public void OrderValidationTests(){
    Restaurant[] restaurants = restHandler.fetchRestaurants(SystemConstants.RESTAURANT_URL);
    Order[] orders = restHandler.fetchOrders(SystemConstants.ORDERS_URL);

    for(Order order : orders){
      OrderValidationCode realValidationCode = order.getOrderValidationCode();
      OrderStatus realOrderStatus = order.getOrderStatus();

      OrderValidationImpl orderValidator = new OrderValidationImpl();
      Order validatedOrder = orderValidator.validateOrder(order, restaurants);

      System.out.printf("Expiry Date: %s OrderDate: %s Card Number: %s ", validatedOrder.getCreditCardInformation().getCreditCardExpiry(), validatedOrder.getOrderDate().toString(),validatedOrder.getCreditCardInformation().getCreditCardNumber());

      System.out.printf("Order Validation Code: %s, %s \n", validatedOrder.getOrderValidationCode(), realValidationCode);

      assertSame(validatedOrder.getOrderValidationCode(), realValidationCode);
      assertSame(validatedOrder.getOrderStatus(), realOrderStatus);


    }
  }

}
