package uk.ac.ed.inf.ilp_cw1;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import uk.ac.ed.inf.ilp_cw1.service.OrderValidationImpl;
public class isValidNumPizzasTest {
  OrderValidationImpl validator = new OrderValidationImpl();

  @Test
  void testValidNumPizzas(){
    assertTrue(validator.isValidNumPizzas(1)); // Random valid number
    assertTrue(validator.isValidNumPizzas(2));
    assertTrue(validator.isValidNumPizzas(3));
    assertTrue(validator.isValidNumPizzas(4));
  }
  @Test
  void testInvalidNumPizzas(){
    assertFalse(validator.isValidNumPizzas(0)); // Random invalid number
    assertFalse(validator.isValidNumPizzas(5));
    assertFalse(validator.isValidNumPizzas(7));
    assertFalse(validator.isValidNumPizzas(100));
  }
@Test
  void testNegativeNumPizzas(){
    assertFalse(validator.isValidNumPizzas(-0)); // Random invalid number
    assertFalse(validator.isValidNumPizzas(-5));
    assertFalse(validator.isValidNumPizzas(-7));
    assertFalse(validator.isValidNumPizzas(-100));
  }



}
