package uk.ac.ed.inf.ilp_cw1.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import uk.ac.ed.inf.ilp_cw1.Data.Order;
import uk.ac.ed.inf.ilp_cw1.Data.OrderStatus;
import uk.ac.ed.inf.ilp_cw1.Data.OrderValidationCode;
import uk.ac.ed.inf.ilp_cw1.Data.Pizza;
import uk.ac.ed.inf.ilp_cw1.Data.Restaurant;
import uk.ac.ed.inf.ilp_cw1.Data.SystemConstants;

public class OrderValidationImpl implements OrderValidation{


  @Override
  public Order validateOrder(Order orderToValidate, Restaurant[] definedRestaurants) {

    OrderStatus orderStatus = OrderStatus.VALID;

    if (!(isValidCreditCardNumber(orderToValidate.getCreditCardInformation().getCreditCardNumber()))){
      orderStatus = OrderStatus.INVALID;
      orderToValidate.setOrderValidationCode(OrderValidationCode.CARD_NUMBER_INVALID);
    }

    if (!(isValidCVVNumber(orderToValidate.getCreditCardInformation().getCvv()))){
      orderStatus = OrderStatus.INVALID;
      orderToValidate.setOrderValidationCode(OrderValidationCode.CVV_INVALID);
    }

    if (!(isValidExpiryDate(orderToValidate.getCreditCardInformation().getCreditCardExpiry()))){
      orderStatus = OrderStatus.INVALID;
      orderToValidate.setOrderValidationCode(OrderValidationCode.EXPIRY_DATE_INVALID);
    }

    if (!(isValidNumPizzas(orderToValidate.getPizzasInOrder().length))){
      orderStatus = OrderStatus.INVALID;
      orderToValidate.setOrderValidationCode(OrderValidationCode.MAX_PIZZA_COUNT_EXCEEDED);
    }

    if(orderToValidate.getPizzasInOrder().length == 0){
      orderStatus = OrderStatus.INVALID;
      orderToValidate.setOrderValidationCode(OrderValidationCode.EMPTY_ORDER);
    }

    if(!(isValidPizzas(orderToValidate.getPizzasInOrder(), definedRestaurants))){
      orderStatus = OrderStatus.INVALID;
      orderToValidate.setOrderValidationCode(OrderValidationCode.PIZZA_NOT_DEFINED);
    }

    if(!(isCorrectPizzaPrice(orderToValidate.getPizzasInOrder(), definedRestaurants))){
      orderStatus = OrderStatus.INVALID;
      orderToValidate.setOrderValidationCode(OrderValidationCode.PRICE_FOR_PIZZA_INVALID);
    }


    orderToValidate.setOrderStatus(orderStatus);

    return orderToValidate;
  }

  @Override
  public Boolean isValidCreditCardNumber(String creditCardNumber) {
    if (creditCardNumber.length() != 16){
      return false;
    }
    return Integer.parseInt(creditCardNumber) >= 0;
  }

  @Override
  public Boolean isValidCVVNumber(String CVV) {
    if (CVV.length() != 3){
      return false;
    } else
      return Integer.parseInt(CVV) >= 0 && Integer.parseInt(CVV) <= 999;
  }

  @Override
  public Boolean isValidExpiryDate(String expiryDate) {

    String regex = "^\\d{2}/\\d{2}$";

    if (!expiryDate.matches(regex)) {
      return false; // If the format doesn't match, it's invalid
    }
    try {
      // Parse the date using a formatter
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM");
      LocalDate.parse(expiryDate + "/2000", formatter); // Use a leap year for February 29
    } catch (DateTimeParseException e) {
      return false; // Parsing failed, date is invalid
    }
    return true;
  }

  @Override
  public Boolean isValidNumPizzas(int numPizzas) {
    return (numPizzas <= SystemConstants.MAX_PIZZAS_PER_ORDER);
  }

  @Override
  public Boolean isValidPizzas(Pizza[] pizzasInOrder, Restaurant[] definedRestaurants) {

    for (Pizza pizza : pizzasInOrder) {

      boolean found = false;

      for (Restaurant restaurant : definedRestaurants) {
        for (Pizza item : restaurant.menu()) {
          if (Objects.equals(pizza.name(), item.name())) {
            found = true;
            break;
          }
        }
      }
      if (!found) {
        return false;
      }
    }
    return true;
  }

  @Override
  public Boolean isCorrectPizzaPrice(Pizza[] pizzasInOrder, Restaurant[] definedRestaurants) {

    for (Pizza pizza : pizzasInOrder) {

      boolean found = false;

      for (Restaurant restaurant : definedRestaurants) {
        for (Pizza item : restaurant.menu()) {
          if ((Objects.equals(pizza.name(), item.name())) && (pizza.priceInPence() == item.priceInPence())) {
            found = true;
            break;
          }
        }
      }
      if (!found) {
        return false;
      }
    }
    return true;
  }
}
