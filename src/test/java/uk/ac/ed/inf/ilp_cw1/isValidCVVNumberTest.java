package uk.ac.ed.inf.ilp_cw1;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import uk.ac.ed.inf.ilp_cw1.service.OrderValidation;
import uk.ac.ed.inf.ilp_cw1.service.OrderValidationImpl;


public class isValidCVVNumberTest {

  OrderValidationImpl validator = new OrderValidationImpl();
  @Test
  void testValidCVVNumber() {
    assertTrue(validator.isValidCVVNumber("123")); // Random valid number
    assertTrue(validator.isValidCVVNumber("000")); // Smallest valid number
    assertTrue(validator.isValidCVVNumber("999")); // Largest valid number
    assertTrue(validator.isValidCVVNumber("456")); // Middle range number
    assertTrue(validator.isValidCVVNumber("001")); // Leading zeros
  }

  @Test
  void testInvalidLength() {
    assertFalse(validator.isValidCVVNumber("12"));   // Too short
    assertFalse(validator.isValidCVVNumber("1234")); // Too long
    assertFalse(validator.isValidCVVNumber(""));     // Empty string
    assertFalse(validator.isValidCVVNumber("1"));    // Single digit
    assertFalse(validator.isValidCVVNumber("12345"));// Excessively long
  }

  @Test
  void testNonNumericInput() {
    assertFalse(validator.isValidCVVNumber("abc"));   // Alphabetic input
    assertFalse(validator.isValidCVVNumber("12a"));   // Mixed numeric and alphabetic
    assertFalse(validator.isValidCVVNumber("!@#"));   // Special characters
    assertFalse(validator.isValidCVVNumber("1 3"));   // Contains spaces
    assertFalse(validator.isValidCVVNumber("1.2"));   // Decimal point
  }

  @Test
  void testBoundaryCases() {
    assertTrue(validator.isValidCVVNumber("000")); // Smallest valid CVV
    assertTrue(validator.isValidCVVNumber("999")); // Largest valid CVV
    assertFalse(validator.isValidCVVNumber("-001")); // Negative number
    assertFalse(validator.isValidCVVNumber("1000")); // Out of range (too large)
    assertFalse(validator.isValidCVVNumber("00"));   // Too short
  }

  @Test
  void testNullInput() {
    assertThrows(NullPointerException.class, () ->
        validator.isValidCVVNumber(null) // Explicit null input
    );
  }


}
