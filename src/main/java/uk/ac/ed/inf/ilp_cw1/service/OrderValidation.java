package uk.ac.ed.inf.ilp_cw1.service;

import java.util.HashSet;
import uk.ac.ed.inf.ilp_cw1.Data.CreditCardInformation;
import uk.ac.ed.inf.ilp_cw1.Data.Order;
import uk.ac.ed.inf.ilp_cw1.Data.OrderValidationCode;
import uk.ac.ed.inf.ilp_cw1.Data.Pizza;
import uk.ac.ed.inf.ilp_cw1.Data.Restaurant;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * interface to validate an order
 */
public interface OrderValidation {
  /**
   * validate an order and deliver a validated version where the
   * OrderStatus and OrderValidationCode are set accordingly.
   *
   * The order validation code is defined in the enum @link uk.ac.ed.inf.ilp.constant.OrderValidationStatus
   *
   * <p>
   * Fields to validate include (among others - for details please see the OrderValidationStatus):
   * <p>
   * number (16 digit numeric)
   * CVV
   * expiration date
   * the menu items selected in the order
   * the involved restaurants
   * if the maximum count is exceeded
   * if the order is valid on the given date for the involved restaurants (opening days)
   *
   * @param orderToValidate    is the order which needs validation
   * @param definedRestaurants is the vector of defined restaurants with their according menu structure
   * @return the validated order
   */

  Order validateOrder(Order orderToValidate, Restaurant[] definedRestaurants);
  Boolean isValidCreditCardNumber(String creditCardNumber);
  Boolean isValidCVVNumber(String CVV);
  Boolean isValidExpiryDate(String expiryDate);
  Boolean isValidNumPizzas(int numPizzas);
  Boolean isValidPizzas(Pizza[] pizzasInOrder, Restaurant[] definedRestaurants);
  Boolean isCorrectPizzaPrice(Pizza[] pizzasInOrder, Restaurant[] definedRestaurants);
  Boolean isTotalCorrect(Pizza[] pizzasInOrder, Restaurant[] definedRestaurants, int orderTotal);
  Boolean isOneRestaurant(Pizza[] pizzasInOrder, Restaurant[] definedRestaurants);
  Boolean isRestaurantOpen(Pizza[]pizzasInOrder, Restaurant[] definedRestaurants, LocalDate orderDate);
  Pizza getRestaurantPizzaRecord(Pizza pizzaInOrder, Restaurant[] definedRestaurants);
  Restaurant getOrderRestaurant(Pizza[] pizzasInOrder, Restaurant[] definedRestaurants);
  //TODO Empty Order

}