package uk.ac.ed.inf.ilp_cw1;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import uk.ac.ed.inf.ilp_cw1.service.OrderValidation;
import uk.ac.ed.inf.ilp_cw1.service.OrderValidationImpl;

public class isValidCreditCardNumberTest {
  OrderValidation validator = new OrderValidationImpl();
  @Test
  void testValidCreditCardNumber() {
    assertTrue(validator.isValidCreditCardNumber("1234567890123456"));
    assertTrue(validator.isValidCreditCardNumber("6200001234567890"));
    assertTrue(validator.isValidCreditCardNumber("9999999999999999"));
    assertTrue(validator.isValidCreditCardNumber("4000123456789001"));
    assertTrue(validator.isValidCreditCardNumber("5123456789012345"));
  }

  @Test
  void testInvalidLength() {
    assertFalse(validator.isValidCreditCardNumber("12345678901234"));
    assertFalse(validator.isValidCreditCardNumber("12345678901234567"));
    assertFalse(validator.isValidCreditCardNumber("12345678901234567890"));
    assertFalse(validator.isValidCreditCardNumber("1234"));
    assertFalse(validator.isValidCreditCardNumber("12345678901234567"));
  }

  @Test
  void testNonNumericInput() {
    assertFalse(validator.isValidCreditCardNumber("1234abcd5678efgh"));
    assertFalse(validator.isValidCreditCardNumber("1234!@#$5678%^&*"));
    assertFalse(validator.isValidCreditCardNumber("1234 5678 9012 3456"));
    assertFalse(validator.isValidCreditCardNumber("12.34567890123456"));
    assertFalse(validator.isValidCreditCardNumber("abcd123456789012"));
  }

  @Test
  void testNegativeNumber() {
    assertFalse(validator.isValidCreditCardNumber("-123456789012345"));
    assertFalse(validator.isValidCreditCardNumber("-12345678901234567"));
    assertFalse(validator.isValidCreditCardNumber("-847692754927572857"));
    assertFalse(validator.isValidCreditCardNumber("-02948673928573718"));
    assertFalse(validator.isValidCreditCardNumber("-758275927572847"));
  }

  @Test
  void testNullInput() {
    assertThrows(NullPointerException.class, () ->
        validator.isValidCreditCardNumber(null)
    );
  }

}
