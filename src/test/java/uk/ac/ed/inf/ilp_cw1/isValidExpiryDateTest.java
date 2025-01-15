package uk.ac.ed.inf.ilp_cw1;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import uk.ac.ed.inf.ilp_cw1.service.OrderValidationImpl;


public class isValidExpiryDateTest {
  OrderValidationImpl validator = new OrderValidationImpl();

  LocalDate validOrderDate = LocalDate.parse("2025-01-17"); //Since we are informed order date will always be valid

  @Test
  void testValidExpiryDates() {
    assertTrue(validator.isValidExpiryDate("08/25", validOrderDate)); // Random valid number
    assertTrue(validator.isValidExpiryDate("04/27", validOrderDate));
    assertTrue(validator.isValidExpiryDate("02/25", validOrderDate));
    assertTrue(validator.isValidExpiryDate("01/25", validOrderDate));
    assertTrue(validator.isValidExpiryDate("08/26", validOrderDate));
  }

  @Test
  void testInvalidExpiryDates(){
    assertFalse(validator.isValidExpiryDate("08/2", validOrderDate)); // Random valid number
    assertFalse(validator.isValidExpiryDate("March/2025", validOrderDate));
    assertFalse(validator.isValidExpiryDate("0/0", validOrderDate));
    assertFalse(validator.isValidExpiryDate("01,25", validOrderDate));
    assertFalse(validator.isValidExpiryDate("0826", validOrderDate));
  }
  @Test
  void testExpiredExpiryDates(){
    assertFalse(validator.isValidExpiryDate("08/24", validOrderDate)); // Random valid number
    assertFalse(validator.isValidExpiryDate("12/24", validOrderDate));
    assertFalse(validator.isValidExpiryDate("09/20", validOrderDate));
    assertFalse(validator.isValidExpiryDate("01/16", validOrderDate));
    assertFalse(validator.isValidExpiryDate("05/23", validOrderDate));
  }

}

