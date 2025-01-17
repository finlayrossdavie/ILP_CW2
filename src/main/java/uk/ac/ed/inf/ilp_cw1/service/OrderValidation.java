package uk.ac.ed.inf.ilp_cw1.service;
import uk.ac.ed.inf.ilp_cw1.Data.Order;

import uk.ac.ed.inf.ilp_cw1.Data.Pizza;
import uk.ac.ed.inf.ilp_cw1.Data.Restaurant;

import java.time.LocalDate;


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
  /**
   * Validates a credit card number.
   *
   * @param creditCardNumber the credit card number to validate
   * @return true if the credit card number is valid, false otherwise
   */
  Boolean isValidCreditCardNumber(String creditCardNumber);
  /**
   * Validates a CVV number.
   *
   * @param CVV the CVV number to validate
   * @return true if the CVV number is valid, false otherwise
   */
  Boolean isValidCVVNumber(String CVV);
  /**
   * Validates the expiration date of a credit card.
   *
   * @param expiryDate the expiration date of the credit card in MM/YY format
   * @param orderDate  the date of the order
   * @return True if the expiration date is valid on the order date, false otherwise
   */
  Boolean isValidExpiryDate(String expiryDate, LocalDate orderDate);
  /**
   * Validates the number of pizzas in an order.
   *
   * @param numPizzas the number of pizzas in the order
   * @return true if the number of pizzas is valid, false otherwise
   */
  Boolean isValidNumPizzas(int numPizzas);

  /**
   * Validates the pizzas in an order against the defined restaurant menus.
   *
   * @param pizzasInOrder      the pizzas in the order
   * @param definedRestaurants an array of defined restaurants with their corresponding menu structures
   * @return true if all pizzas in the order are valid, false otherwise
   */

  Boolean isValidPizzas(Pizza[] pizzasInOrder, Restaurant[] definedRestaurants);
  /**
   * Validates the prices of pizzas in the order.
   *
   * @param pizzasInOrder      the pizzas in the order
   * @param definedRestaurants an array of defined restaurants with their corresponding menu structures
   * @return true if the pizza prices are correct, false otherwise
   */
  Boolean isCorrectPizzaPrice(Pizza[] pizzasInOrder, Restaurant[] definedRestaurants);
  /**
   * Validates the total price of the order.
   *
   * @param pizzasInOrder      the pizzas in the order
   * @param definedRestaurants an array of defined restaurants with their corresponding menu structures
   * @param orderTotal         the total price to verify
   * @return true if the total price is correct, false otherwise
   */
  Boolean isTotalCorrect(Pizza[] pizzasInOrder, Restaurant[] definedRestaurants, int orderTotal);
  /**
   * Checks if all pizzas in the order belong to one restaurant.
   *
   * @param pizzasInOrder      the pizzas in the order
   * @param definedRestaurants an array of defined restaurants with their corresponding menu structures
   * @return true if all pizzas belong to one restaurant, false otherwise
   */
  Boolean isOneRestaurant(Pizza[] pizzasInOrder, Restaurant[] definedRestaurants);
  /**
   * Validates if the involved restaurant(s) are open on the given order date.
   *
   * @param pizzasInOrder      the pizzas in the order
   * @param definedRestaurants an array of defined restaurants with their corresponding menu structures
   * @param orderDate          the date of the order (assumed to be error free)
   * @return true if the restaurant(s) are open, false otherwise
   */
  Boolean isRestaurantOpen(Pizza[]pizzasInOrder, Restaurant[] definedRestaurants, LocalDate orderDate);
  /**
   * Retrieves the pizza record for a specific pizza from the restaurant menu.
   *
   * @param pizzaInOrder       the pizza in the order
   * @param definedRestaurants an array of defined restaurants with their corresponding menu structures
   * @return the pizza record from the restaurant menu, or null if not found
   */
  Pizza getRestaurantPizzaRecord(Pizza pizzaInOrder, Restaurant[] definedRestaurants);
  /**
   * Identifies the restaurant associated with the pizzas in the order.
   *
   * @param pizzasInOrder      the pizzas in the order
   * @param definedRestaurants an array of defined restaurants with their corresponding menu structures
   * @return the Restaurant object associated with the order, or null if not determined
   */
  Restaurant getOrderRestaurant(Pizza[] pizzasInOrder, Restaurant[] definedRestaurants);
}