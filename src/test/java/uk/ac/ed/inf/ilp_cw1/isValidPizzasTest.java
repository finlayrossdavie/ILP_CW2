package uk.ac.ed.inf.ilp_cw1;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import uk.ac.ed.inf.ilp_cw1.Data.Pizza;
import uk.ac.ed.inf.ilp_cw1.Data.Restaurant;
import uk.ac.ed.inf.ilp_cw1.Data.SystemConstants;
import uk.ac.ed.inf.ilp_cw1.service.OrderValidationImpl;
import uk.ac.ed.inf.ilp_cw1.service.restHandler;

public class isValidPizzasTest {
  Restaurant[] restaurants = restHandler.fetchRestaurants(SystemConstants.RESTAURANT_URL);
  OrderValidationImpl validator = new OrderValidationImpl();

}
