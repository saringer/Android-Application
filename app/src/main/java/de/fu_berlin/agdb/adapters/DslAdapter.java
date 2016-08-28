package de.fu_berlin.agdb.adapters;

import de.fu_berlin.agdb.data.Constraints;

import static de.fu_berlin.agdb.data.Constants.CUSTOM_ALERT;

/**
 * Created by Riva on 07.06.2016.
 */
public class DslAdapter {

    private Double locationIdentifier;
    private Integer unixDate;
    private int alertTypeFlag;

    public DslAdapter(Double locationIdentifier, int alertTypeFlag) {
        this.locationIdentifier = locationIdentifier;
        this.alertTypeFlag = alertTypeFlag;
    }


    public String listOfConstraintsToDSL(ConstraintsListAdapter adapter) {
        String stringBuilder = "";
        for (int i = 0; i < adapter.getCount(); i++) {

            if (i == 0) {

                stringBuilder = checkCurrentConstraint(adapter.getItem(i), i);
            } else {
                stringBuilder = "and(" + stringBuilder + ", " + checkCurrentConstraint(adapter.getItem(i), i) + ")";
            }


        }
        return stringBuilder;
    }

    public String temperatureConstraint(Constraints constraint, int matchCounter) {
        // "Greater" operator selected
        if (constraint.getNumericalOperator().equals("greater")) {
            // Alert with date
            if (alertTypeFlag == CUSTOM_ALERT) {
                String temp = "greater(\"temperatureHigh\", " + constraint.getWeatherValue() + ")";
                String date = "forEvent(m" + matchCounter + ", equal(\"unixDate\", " + constraint.getUnixDate() + "))";
                String dsl = "sequence(m" + matchCounter + " = equal(\"forecastIOLocationIdentifier\", " + locationIdentifier + ")," +
                        date + "," +
                        "forEvent(m" + matchCounter + ", " + temp + "))";
                return dsl;
            }
            // Alert without date
            else {
                String temp = "greater(\"temperatureHigh\", " + constraint.getWeatherValue() + ")";
                String dsl = "sequence(m" + matchCounter + " = equal(\"forecastIOLocationIdentifier\", " + locationIdentifier + ")," +
                        "forEvent(m" + matchCounter + ", " + temp + "))";
                return dsl;
            }
            // "Lower" operator selected
        } else {
            // Alert with date
            if (alertTypeFlag == CUSTOM_ALERT) {
                String temp = "less(\"temperatureLow\", " + constraint.getWeatherValue() + ")";
                String date = "forEvent(m" + matchCounter + ", equal(\"unixDate\", " + constraint.getUnixDate() + "))";
                String dsl = "sequence(m" + matchCounter + " = equal(\"forecastIOLocationIdentifier\", " + locationIdentifier + ")," +
                        date + "," +
                        "forEvent(m" + matchCounter + ", " + temp + "))";
                return dsl;
            }
            // Alert without date
            else {
                String temp = "greater(\"temperatureHigh\", " + constraint.getWeatherValue() + ")";
                String dsl = "sequence(m" + matchCounter + " = equal(\"forecastIOLocationIdentifier\", " + locationIdentifier + ")," +
                        "forEvent(m" + matchCounter + ", " + temp + "))";
                return dsl;
            }
        }

    }

    public String frostConstraint(Constraints constraint, int matchCounter) {
        // Alert with date
        if (alertTypeFlag == CUSTOM_ALERT) {
            String temp = "forEvent(m" + matchCounter + ", less(\"temperatureLow\", " + "0.0" + "))";
            String date = "forEvent(m" + matchCounter + ", equal(\"unixDate\", " + constraint.getUnixDate() + "))";

            String dsl = "sequence(m" + matchCounter + " = equal(\"forecastIOLocationIdentifier\", " + locationIdentifier + ")," +
                    date + "," + temp + ")";
            return dsl;
        }
        // Alert without date
        else {
            String temp = "forEvent(m" + matchCounter + ", less(\"temperatureLow\", " + "0.0" + "))";
            String dsl = "sequence(m" + matchCounter + " = equal(\"forecastIOLocationIdentifier\", " + locationIdentifier + ")," +
                    temp + ")";
            return dsl;
        }
    }

    public String precipitationConstraint(Constraints constraint, int matchCounter) {
        // Alert with date
        if (alertTypeFlag == CUSTOM_ALERT) {
            String precipitation = "forEvent(m" + matchCounter + ", greaterEqual(\"precipitationProbability\"," + constraint.getProbability() + "))";
            String date = "forEvent(m" + matchCounter + ", equal(\"unixDate\", " + constraint.getUnixDate() + "))";

            String dsl = "sequence(m" + matchCounter + " = equal(\"forecastIOLocationIdentifier\", " + locationIdentifier + ")," +
                    date + "," + precipitation + ")";
            return dsl;
        }
        // Alert without date
        else {
            String precipitation = "forEvent(m" + matchCounter + ", greaterEqual(\"precipitationProbability\"," + constraint.getProbability() + "))";
            String dsl = "sequence(m" + matchCounter + " = equal(\"forecastIOLocationIdentifier\", " + locationIdentifier + ")," +
                      precipitation + ")";
            return dsl;
        }
    }

    public String windConstraint(Constraints constraint, int matchCounter) {
        // Alert with date
        if (alertTypeFlag == CUSTOM_ALERT) {
            String wind = "forEvent(m" + matchCounter + ", greaterEqual(\"maximumWindSpeed\"," + constraint.getBeaufortWindspeed() + "))";
            String date = "forEvent(m" + matchCounter + ", equal(\"unixDate\", " + constraint.getUnixDate() + "))";

            String dsl = "sequence(m" + matchCounter + " = equal(\"forecastIOLocationIdentifier\", " + locationIdentifier + ")," +
                    date + "," + wind + ")";
            return dsl;
        }
        // Alert without date
        else {
            String wind = "forEvent(m" + matchCounter + ", greaterEqual(\"maximumWindSpeed\"," + constraint.getBeaufortWindspeed() + "))";

            String dsl = "sequence(m" + matchCounter + " = equal(\"forecastIOLocationIdentifier\", " + locationIdentifier + ")," +
                     wind + ")";
            return dsl;
        }
    }

    public String checkCurrentConstraint(Constraints constraint, int matchCounter) {
        if (constraint.getWeather().equals("temperature")) {
            return temperatureConstraint(constraint, matchCounter);
        } else if (constraint.getWeather().equals("frost")) {
            return frostConstraint(constraint, matchCounter);
        } else if (constraint.getWeather().equals("windspeed")) {
            return windConstraint(constraint, matchCounter);
        } else if (constraint.getWeather().equals("precipitation")) {
            return precipitationConstraint(constraint, matchCounter);
        } else {
            return null;
        }
    }


}
