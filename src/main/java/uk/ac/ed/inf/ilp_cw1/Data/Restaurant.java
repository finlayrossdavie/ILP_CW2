package uk.ac.ed.inf.ilp_cw1.Data;

import uk.ac.ed.inf.ilp_cw1.Data.Position;

import java.time.DayOfWeek;

/**
 * Acts as a simple pizza supplier for the PizzaDronz service
 *
 * @param name the name of the restaurant
 * @param location where is the restaurant located
 * @param menu which pizzas are on offer from this restaurant
 * @param openingDays which days of the week is the restaurant open
 */
public record Restaurant(String name, Position location, DayOfWeek[] openingDays, Pizza[] menu) {
}