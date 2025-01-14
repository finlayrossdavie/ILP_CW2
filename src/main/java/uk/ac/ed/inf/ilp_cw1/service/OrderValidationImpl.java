package uk.ac.ed.inf.ilp_cw1.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import uk.ac.ed.inf.ilp_cw1.Data.Order;
import uk.ac.ed.inf.ilp_cw1.Data.OrderStatus;
import uk.ac.ed.inf.ilp_cw1.Data.OrderValidationCode;
import uk.ac.ed.inf.ilp_cw1.Data.Pizza;
import uk.ac.ed.inf.ilp_cw1.Data.Restaurant;
import uk.ac.ed.inf.ilp_cw1.Data.SystemConstants;

public class OrderValidationImpl implements OrderValidation {

  @Override
  public Order validateOrder(Order orderToValidate, Restaurant[] definedRestaurants) {

    OrderStatus orderStatus = OrderStatus.VALID;

    if (!(isValidCreditCardNumber(
        orderToValidate.getCreditCardInformation().getCreditCardNumber()))) {
      orderStatus = OrderStatus.INVALID;
      orderToValidate.setOrderValidationCode(OrderValidationCode.CARD_NUMBER_INVALID);
    }



    if (!(isValidCVVNumber(orderToValidate.getCreditCardInformation().getCvv()))) {
      orderStatus = OrderStatus.INVALID;
      orderToValidate.setOrderValidationCode(OrderValidationCode.CVV_INVALID);
    }

    if (!(isValidExpiryDate(orderToValidate.getCreditCardInformation().getCreditCardExpiry()))) {
      orderStatus = OrderStatus.INVALID;
      orderToValidate.setOrderValidationCode(OrderValidationCode.EXPIRY_DATE_INVALID);
    }

    if (!(isValidNumPizzas(orderToValidate.getPizzasInOrder().length))) {
      orderStatus = OrderStatus.INVALID;
      orderToValidate.setOrderValidationCode(OrderValidationCode.MAX_PIZZA_COUNT_EXCEEDED);
    }

    if (orderToValidate.getPizzasInOrder().length == 0) {
      orderStatus = OrderStatus.INVALID;
      orderToValidate.setOrderValidationCode(OrderValidationCode.EMPTY_ORDER);
    }

    if (!(isValidPizzas(orderToValidate.getPizzasInOrder(), definedRestaurants))) {
      orderStatus = OrderStatus.INVALID;
      orderToValidate.setOrderValidationCode(OrderValidationCode.PIZZA_NOT_DEFINED);
    }

    if (!(isCorrectPizzaPrice(orderToValidate.getPizzasInOrder(), definedRestaurants))) {
      orderStatus = OrderStatus.INVALID;
      orderToValidate.setOrderValidationCode(OrderValidationCode.PRICE_FOR_PIZZA_INVALID);
    }

    if (!(isTotalCorrect(orderToValidate.getPizzasInOrder(), definedRestaurants,
        orderToValidate.getPriceTotalInPence()))) {
      orderStatus = OrderStatus.INVALID;
      orderToValidate.setOrderValidationCode(OrderValidationCode.TOTAL_INCORRECT);
    }

    if(!(isOneRestaurant(orderToValidate.getPizzasInOrder(), definedRestaurants))){
      orderStatus = OrderStatus.INVALID;
      orderToValidate.setOrderValidationCode(OrderValidationCode.PIZZA_FROM_MULTIPLE_RESTAURANTS);
    }

    if(!(isRestaurantOpen(orderToValidate.getPizzasInOrder(), definedRestaurants, orderToValidate.getOrderDate()))){
      orderStatus = OrderStatus.INVALID;
      orderToValidate.setOrderValidationCode(OrderValidationCode.RESTAURANT_CLOSED);
    }

    orderToValidate.setOrderStatus(orderStatus);


    return orderToValidate;
  }

  @Override
  public Boolean isValidCreditCardNumber(String creditCardNumber) {
    if (creditCardNumber.length() != 16) {
      return false;
    }
    return Long.parseLong(creditCardNumber) >= 0;
  }

  @Override
  public Boolean isValidCVVNumber(String CVV) {
    if (CVV.length() != 3) {
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
  public Pizza getRestaurantPizzaRecord(Pizza pizzaInOrder, Restaurant[] definedRestaurants) {
    boolean found = false;

    Pizza result = null;

    for (Restaurant restaurant : definedRestaurants) {
      for (Pizza item : restaurant.menu()) {
        if ((Objects.equals(pizzaInOrder.name(), item.name()))) {
          found = true;
          result = item;
          break;
        }
      }
    }

    if (found) {
      return result;
    } else {
      return null;
    }
  }
  @Override
  public Boolean isValidPizzas(Pizza[] pizzasInOrder, Restaurant[] definedRestaurants) {

    for (Pizza pizza : pizzasInOrder) {
      Pizza correctPizza = getRestaurantPizzaRecord(pizza, definedRestaurants);
      if (correctPizza == null) {
        return false;
      }
    }
    return true;
  }

  @Override
  public Boolean isCorrectPizzaPrice(Pizza[] pizzasInOrder, Restaurant[] definedRestaurants) {
    for (Pizza pizza : pizzasInOrder) {
      Pizza correctPizza = getRestaurantPizzaRecord(pizza, definedRestaurants);
      if (correctPizza == null) {
        return false;
      } else if (correctPizza.priceInPence() != pizza.priceInPence()) {
        return false;
      }
    }
    return true;

  }


  @Override
  public Boolean isTotalCorrect(Pizza[] pizzasInOrder, Restaurant[] definedRestaurants,
      int orderTotal) {
    int total = 0;
    for (Pizza pizza : pizzasInOrder) {
      Pizza correctPizza = getRestaurantPizzaRecord(pizza, definedRestaurants);
      if (correctPizza == null) {
        return false;
      } else if (correctPizza.priceInPence() != pizza.priceInPence()) {
        return false;
      } else {
        total += correctPizza.priceInPence();
      }
    }
    if((total+100) != orderTotal){
      System.out.printf("Calculated total: %s Real total: %s", total, orderTotal);
    }

    return ((total+100) == orderTotal);
  }

  /*
  Function which returns the restaurant which all pizza's in the order come from. Returns null if
  more than one restaurant is in the order.
   */
  @Override
  public Restaurant getOrderRestaurant(Pizza[] pizzasInOrder, Restaurant[] definedRestaurants) {
    Restaurant tempRestaurant = null;

    for (Pizza pizzaInOrder : pizzasInOrder) {
      for (Restaurant restaurant : definedRestaurants) {
        for (Pizza item : restaurant.menu()) {
          if ((Objects.equals(pizzaInOrder.name(), item.name()))) {

            if ((tempRestaurant != null) && (tempRestaurant != restaurant)) {
              return null;
            }
            tempRestaurant = restaurant;
            break;
          }
        }
      }
    }
    return tempRestaurant;
  }

  @Override
  public Boolean isOneRestaurant(Pizza[] pizzasInOrder, Restaurant[] definedRestaurants) {
    return (getOrderRestaurant(pizzasInOrder, definedRestaurants) != null);
  }
  @Override
  public Boolean isRestaurantOpen(Pizza[] pizzasInOrder, Restaurant[] definedRestaurants, LocalDate orderDate) {
    Restaurant orderRestaurant = getOrderRestaurant(pizzasInOrder, definedRestaurants);
    if(orderRestaurant == null){
      return false;
    }

    DayOfWeek[] openingDays = orderRestaurant.openingDays();

    for(DayOfWeek openDay : openingDays){
      if (openDay == orderDate.getDayOfWeek()){
        return true;
      }
    }
    return false;
  }
}
