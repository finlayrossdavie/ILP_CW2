package uk.ac.ed.inf.ilp_cw1.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.springframework.cglib.core.Local;
import uk.ac.ed.inf.ilp_cw1.Data.Order;
import uk.ac.ed.inf.ilp_cw1.Data.OrderStatus;
import uk.ac.ed.inf.ilp_cw1.Data.OrderValidationCode;
import uk.ac.ed.inf.ilp_cw1.Data.Pizza;
import uk.ac.ed.inf.ilp_cw1.Data.Restaurant;
import uk.ac.ed.inf.ilp_cw1.Data.SystemConstants;

public class OrderValidationImpl implements OrderValidation {

  @Override
  public Order validateOrder(Order orderToValidate, Restaurant[] definedRestaurants) {

    if (orderToValidate.getPizzasInOrder().length == 0) {
      orderToValidate.setOrderValidationCode(OrderValidationCode.EMPTY_ORDER);
      orderToValidate.setOrderStatus(OrderStatus.INVALID);
      return orderToValidate;
    }


    if (!(isValidCreditCardNumber(
        orderToValidate.getCreditCardInformation().getCreditCardNumber()))) {
      orderToValidate.setOrderValidationCode(OrderValidationCode.CARD_NUMBER_INVALID);
      orderToValidate.setOrderStatus(OrderStatus.INVALID);
      return orderToValidate;
    }

    if (!(isValidCVVNumber(orderToValidate.getCreditCardInformation().getCvv()))) {
      orderToValidate.setOrderValidationCode(OrderValidationCode.CVV_INVALID);
      orderToValidate.setOrderStatus(OrderStatus.INVALID);
      return orderToValidate;
    }

    if(!(isOneRestaurant(orderToValidate.getPizzasInOrder(), definedRestaurants))){

      orderToValidate.setOrderValidationCode(OrderValidationCode.PIZZA_FROM_MULTIPLE_RESTAURANTS);
      orderToValidate.setOrderStatus(OrderStatus.INVALID);
      return orderToValidate;
    }

    if(!(isRestaurantOpen(orderToValidate.getPizzasInOrder(), definedRestaurants, orderToValidate.getOrderDate()))){

      orderToValidate.setOrderValidationCode(OrderValidationCode.RESTAURANT_CLOSED);
      orderToValidate.setOrderStatus(OrderStatus.INVALID);
      return orderToValidate;
    }

    if (!(isValidExpiryDate(orderToValidate.getCreditCardInformation().getCreditCardExpiry(), orderToValidate.getOrderDate()))) {
      orderToValidate.setOrderValidationCode(OrderValidationCode.EXPIRY_DATE_INVALID);
      orderToValidate.setOrderStatus(OrderStatus.INVALID);
      return orderToValidate;
    }

    if (!(isValidNumPizzas(orderToValidate.getPizzasInOrder().length))) {
      orderToValidate.setOrderValidationCode(OrderValidationCode.MAX_PIZZA_COUNT_EXCEEDED);
      orderToValidate.setOrderStatus(OrderStatus.INVALID);
      return orderToValidate;
    }


    if (!(isValidPizzas(orderToValidate.getPizzasInOrder(), definedRestaurants))) {

      orderToValidate.setOrderValidationCode(OrderValidationCode.PIZZA_NOT_DEFINED);
      orderToValidate.setOrderStatus(OrderStatus.INVALID);
      return orderToValidate;
    }

    if (!(isCorrectPizzaPrice(orderToValidate.getPizzasInOrder(), definedRestaurants))) {

      orderToValidate.setOrderValidationCode(OrderValidationCode.PRICE_FOR_PIZZA_INVALID);
      orderToValidate.setOrderStatus(OrderStatus.INVALID);
      return orderToValidate;
    }

    if (!(isTotalCorrect(orderToValidate.getPizzasInOrder(), definedRestaurants,
        orderToValidate.getPriceTotalInPence()))) {

      orderToValidate.setOrderValidationCode(OrderValidationCode.TOTAL_INCORRECT);
      orderToValidate.setOrderStatus(OrderStatus.INVALID);
      return orderToValidate;
    }


    orderToValidate.setOrderValidationCode(OrderValidationCode.NO_ERROR);
    orderToValidate.setOrderStatus(OrderStatus.VALID);

    return orderToValidate;
  }

  @Override
  public Boolean isValidCreditCardNumber(String creditCardNumber) {
    if (creditCardNumber.length() != 16) {
      return false;
    }

    try{
      long result = Long.parseLong(creditCardNumber);
      return result > 0;
    }
    catch (NumberFormatException e){
      return false;
    }

  }

  @Override
  public Boolean isValidCVVNumber(String CVV) {
    if (CVV.length() != 3) {
      return false;
    } else
      try{
        return Integer.parseInt(CVV) >= 0 && Integer.parseInt(CVV) <= 999;
      }
      catch (NumberFormatException e){
        return false;
      }
  }

  @Override
  public Boolean isValidExpiryDate(String expiryDate, LocalDate orderDate) {
    // Validate format using regex
    String regex = "^\\d{2}/\\d{2}$";
    if (!expiryDate.matches(regex)) {
      return false;
    }

    try {
      // Parse the date using LocalDate
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");
      YearMonth expiryYearMonth = YearMonth.parse(expiryDate, formatter);

      LocalDate expiryLocalDate = expiryYearMonth.atEndOfMonth();

      return !expiryLocalDate.isBefore(orderDate);

    } catch (DateTimeParseException e) {

      return false;
    }
  }


  @Override
  public Boolean isValidNumPizzas(int numPizzas) {
    return (numPizzas > 0 && numPizzas <= SystemConstants.MAX_PIZZAS_PER_ORDER);
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
