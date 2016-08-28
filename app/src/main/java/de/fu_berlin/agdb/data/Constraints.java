package de.fu_berlin.agdb.data;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.TextView;
import de.fu_berlin.agdb.adapters.ConstraintsListAdapter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static de.fu_berlin.agdb.data.Constants.CUSTOM_ALERT;

/**
 * Created by Riva on 25.05.2016.
 */
public class Constraints {

    private double probability = 0.30;
    private String numericalOperator = "greater";
    private String weather = "temperature";
    private double weatherValue = 15.0;
    private Activity activity;
    private TextView textView;
    private final Calendar calendar = Calendar.getInstance();
    private long unixDate = calculateUnixDate();
    private int alertTypeFlag;
    private String beaufortWindDescription = "light air";
    private Double beaufortWindspeed = 1.0;

    // currently selected items
    private int numericOperatorSelected = 0;
    private int windSpeedSelected = 0;
    private int probabilitySelected = 3;
    private ConstraintsListAdapter adapter;


    public Constraints(Activity activity, String weather, int alertTypeFlag) {
        this.activity = activity;
        this.weather = weather;
        this.alertTypeFlag = alertTypeFlag;
    }

    public TextView createConstraintTextView(TextView textView, ConstraintsListAdapter adapter) {
        this.textView = textView;
        this.adapter = adapter;
        //TextView textView = new TextView(activity);

        // this step is mandated for the clickable styles.
        textView.setMovementMethod(LinkMovementMethod.getInstance());


        createText();
        return textView;
    }


    public long getUnixDate() {
        return unixDate;
    }

    public void setUnixDate(long unixDate) {
        this.unixDate = unixDate;
    }

    private ClickableSpan createNumericOperatorSpan() {
        // Weather Element
        ClickableSpan clickableSpan = new ClickableSpan() {

            @Override
            public void onClick(View widget) {

                // Strings to Show In Dialog with Radio Buttons
                final CharSequence[] items = {" Greater ", " Lower "};



                // Creating and Building the Dialog
                final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Greater/Lower");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }

                });
                builder.setSingleChoiceItems(items, numericOperatorSelected, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {


                        switch (item) {
                            case 0:
                                numericalOperator = "greater";
                                numericOperatorSelected = 0;
                                createText();
                                adapter.notifyDataSetChanged();
                                break;
                            case 1:
                                numericalOperator = "lower";
                                numericOperatorSelected = 1;
                                createText();
                                adapter.notifyDataSetChanged();
                                break;

                            default:
                                break;

                        }

                    }
                });

                builder.create().show();


            }
        };
        return clickableSpan;
    }

    private ClickableSpan createWindspeedSpan() {
        // Weather Element
        ClickableSpan clickableSpan = new ClickableSpan() {

            @Override
            public void onClick(View widget) {

                // Strings to Show In Dialog with Radio Buttons
                final CharSequence[] items = {"  Light air  ", " Light breeze ", " Moderate breeze ",
                        " Strong breeze ", " Gale "};

                // Creating and Building the Dialog
                final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Wind force scale");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }

                });
                builder.setSingleChoiceItems(items, windSpeedSelected, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {


                        switch (item) {
                            case 0:
                                //beaufortItem.setDescription("light air");
                                //beaufortItem.setValue(1.0);
                                beaufortWindDescription = "light air";
                                beaufortWindspeed = 1.0;
                                windSpeedSelected = 0;
                                createText();
                                adapter.notifyDataSetChanged();
                                break;
                            case 1:
                               // beaufortItem.setDescription("light breeze");
                                //beaufortItem.setValue(4.0);
                                beaufortWindDescription = "light breeze";
                                beaufortWindspeed = 4.0;
                                windSpeedSelected = 1;
                                adapter.notifyDataSetChanged();
                                createText();
                                break;
                            case 2:
                                //beaufortItem.setDescription("moderate breeze");
                               // beaufortItem.setValue(12.7);
                                beaufortWindDescription = "moderate breeze";
                                beaufortWindspeed = 12.7;
                                windSpeedSelected = 2;
                                adapter.notifyDataSetChanged();
                                createText();
                                break;
                            case 3:
                               // beaufortItem.setDescription("strong breeze");
                               // beaufortItem.setValue(25.0);
                                beaufortWindDescription = "strong breeze";
                                beaufortWindspeed = 25.0;
                                windSpeedSelected = 3;
                                adapter.notifyDataSetChanged();
                                createText();
                                break;
                            case 4:
                               // beaufortItem.setDescription("gale");
                               // beaufortItem.setValue(32.0);
                                beaufortWindDescription = "gale";
                                beaufortWindspeed = 32.0;
                                windSpeedSelected = 4;
                                adapter.notifyDataSetChanged();
                                createText();
                                break;

                            default:
                               // beaufortItem.setDescription("light air");
                               // beaufortItem.setValue(1.0);
                                beaufortWindDescription = "light air";
                                beaufortWindspeed = 1.0;
                                windSpeedSelected = 0;
                                adapter.notifyDataSetChanged();
                                createText();
                                break;

                        }

                    }
                });

                builder.create().show();


            }
        };
        return clickableSpan;
    }

    private ClickableSpan createProbabilitySpan() {
        // Weather Element
        ClickableSpan clickableSpan = new ClickableSpan() {

            @Override
            public void onClick(View widget) {

                // Strings to Show In Dialog with Radio Buttons
                final CharSequence[] items = {" 1% ", " 10% ", " 20% ", " 30% ", " 40% ", " 50% ", " 60% ", " 70% ", " 80% ", " 90% "};

                // Creating and Building the Dialog
                final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Probability of precipitation");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }

                });
                builder.setSingleChoiceItems(items, probabilitySelected, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {


                        switch (item) {
                            case 0:
                                probability = 0.01;
                                probabilitySelected = 0;
                                createText();
                                adapter.notifyDataSetChanged();
                                break;
                            case 1:
                                probability = 0.10;
                                probabilitySelected = 1;
                                createText();
                                adapter.notifyDataSetChanged();
                                break;
                            case 2:
                                probability = 0.20;
                                probabilitySelected = 2;
                                createText();
                                adapter.notifyDataSetChanged();
                                break;
                            case 3:
                                probability = 0.30;
                                probabilitySelected = 3;
                                createText();
                                adapter.notifyDataSetChanged();
                                break;
                            case 4:
                                probability = 0.40;
                                probabilitySelected = 4;
                                createText();
                                adapter.notifyDataSetChanged();
                                break;
                            case 5:
                                probability = 0.50;
                                probabilitySelected = 5;
                                createText();
                                adapter.notifyDataSetChanged();
                                break;
                            case 6:
                                probability = 0.60;
                                probabilitySelected = 6;
                                createText();
                                adapter.notifyDataSetChanged();
                                break;
                            case 7:
                                probability = 0.70;
                                probabilitySelected = 7;
                                createText();
                                break;
                            case 8:
                                probability = 0.80;
                                probabilitySelected = 8;
                                createText();
                                adapter.notifyDataSetChanged();
                                break;
                            case 9:
                                probability = 0.90;
                                probabilitySelected = 9;
                                createText();
                                adapter.notifyDataSetChanged();
                                break;

                            default:
                                probability = 0.01;
                                probabilitySelected = 0;
                                createText();
                                adapter.notifyDataSetChanged();
                                break;

                        }

                    }
                });

                builder.create().show();


            }
        };
        return clickableSpan;
    }


    private ClickableSpan createDegreeSpan() {
        // Weather Element
        final ClickableSpan clickableSpan = new ClickableSpan() {

            @Override
            public void onClick(View widget) {

                final int minValue = -40;
                final int maxValue = 40;


                // Creating and Building the Dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Select Temperature in Celsius°");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }

                });
                final NumberPicker numberPicker = new NumberPicker(activity);
                numberPicker.setWrapSelectorWheel(true);
                numberPicker.setMinValue(0);
                numberPicker.setMaxValue(maxValue - minValue);
                numberPicker.setWrapSelectorWheel(false);
                numberPicker.setValue(15 - minValue);
                numberPicker.setFormatter(new NumberPicker.Formatter() {
                    @Override
                    public String format(int index) {
                        return Integer.toString(index + minValue);
                    }
                });
                numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                        weatherValue = (double) (newVal + minValue);

                        createText();
                        adapter.notifyDataSetChanged();
                    }
                });
                // hack for common numberpicker bug
                try {
                    Method method = numberPicker.getClass().getDeclaredMethod("changeValueByOne", boolean.class);
                    method.setAccessible(true);
                    method.invoke(numberPicker, true);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }

                builder.setView(numberPicker);

                builder.create().show();


            }
        };

        return clickableSpan;
    }

    private ClickableSpan createDateSpan() {
        // Weather Element
        final ClickableSpan clickableSpan = new ClickableSpan() {

            @Override
            public void onClick(View widget) {


                // Creating and Building the Dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Select Temperature in Celsius°");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }

                });
                // Use the current date as the default date in the picker
                //Calendar now = Calendar.getInstance();
                //final Calendar c = Calendar.getInstance();

                DatePickerDialog dpd = new DatePickerDialog(activity,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                calendar.set(year, monthOfYear, dayOfMonth);
                                unixDate = calculateUnixDate();
                                createText();
                                adapter.notifyDataSetChanged();

                            }
                        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
                dpd.getDatePicker().setMinDate(new Date().getTime());
                dpd.show();


                //builder.create().show();


            }
        };

        return clickableSpan;
    }


    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }

    public String getNumericalOperator() {
        return numericalOperator;
    }

    public void setNumericalOperator(String numericalOperator) {
        this.numericalOperator = numericalOperator;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public double getWeatherValue() {
        return weatherValue;
    }

    public void setWeatherValue(double weatherValue) {
        this.weatherValue = weatherValue;
    }

    public Double getBeaufortWindspeed() {
        return this.beaufortWindspeed;
    }


    public Calendar getCalendar() {
        return calendar;
    }

    private void createText() {


        if (weather.equals("frost")) {
            // Weather Element
            ClickableSpan dateSpan = createDateSpan();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
            SpannableStringBuilder constraintDescription = new SpannableStringBuilder();
            constraintDescription.append("Notify me if there will be ");
            constraintDescription.append(weather);
            if (alertTypeFlag == CUSTOM_ALERT) {
                constraintDescription.append(" on ");
                int start = constraintDescription.length();
                constraintDescription.append(simpleDateFormat.format(calendar.getTime()));
                constraintDescription.setSpan(dateSpan, start, constraintDescription.length(), Spannable.SPAN_COMPOSING);
                textView.setText(constraintDescription);
            } else {
                textView.setText(constraintDescription);
            }

        }

        if (weather.equals("temperature")) {
            ClickableSpan degreeSpan = createDegreeSpan();
            ClickableSpan dateSpan = createDateSpan();
            ClickableSpan numericalOperatorSpan = createNumericOperatorSpan();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
            SpannableStringBuilder constraintDescription = new SpannableStringBuilder();
            constraintDescription.append("Notify me if ");
            constraintDescription.append(weather);
            constraintDescription.append(" will be ");
            int start = constraintDescription.length();
            constraintDescription.append(numericalOperator);
            constraintDescription.setSpan(numericalOperatorSpan, start, constraintDescription.length(), Spannable.SPAN_COMPOSING);
            constraintDescription.append(" than ");
            start = constraintDescription.length();
            constraintDescription.append(Double.toString(weatherValue));
            constraintDescription.setSpan(degreeSpan, start, constraintDescription.length(), Spannable.SPAN_COMPOSING);
            if (alertTypeFlag == CUSTOM_ALERT) {
                constraintDescription.append(" on ");
                start = constraintDescription.length();
                constraintDescription.append(simpleDateFormat.format(calendar.getTime()));
                constraintDescription.setSpan(dateSpan, start, constraintDescription.length(), Spannable.SPAN_COMPOSING);
                textView.setText(constraintDescription);
            } else {
                textView.setText(constraintDescription);
            }
        }

        if (weather.equals("precipitation")) {
            ClickableSpan dateSpan = createDateSpan();
            ClickableSpan probabilitySpan = createProbabilitySpan();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

            SpannableStringBuilder constraintDescription = new SpannableStringBuilder();
            constraintDescription.append("Notify me if the chance of ");
            constraintDescription.append(weather);
            constraintDescription.append(" will be greater than ");
            int start = constraintDescription.length();
            constraintDescription.append(Integer.toString((int) (probability * 100)) + "%");
            constraintDescription.setSpan(probabilitySpan, start, constraintDescription.length(), Spannable.SPAN_COMPOSING);
            if (alertTypeFlag == CUSTOM_ALERT) {
                constraintDescription.append(" on ");
                start = constraintDescription.length();
                constraintDescription.append(simpleDateFormat.format(calendar.getTime()));
                constraintDescription.setSpan(dateSpan, start, constraintDescription.length(), Spannable.SPAN_COMPOSING);
                textView.setText(constraintDescription);
            }
            else {
                textView.setText(constraintDescription);

            }

        }

        if (weather.equals("windspeed")) {

            ClickableSpan dateSpan = createDateSpan();
            ClickableSpan windspeedSpan = createWindspeedSpan();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
            SpannableStringBuilder constraintDescription = new SpannableStringBuilder();
            constraintDescription.append("Notify me if ");
            constraintDescription.append(weather);
            constraintDescription.append(" will reach ");
            int start = constraintDescription.length();
            constraintDescription.append(beaufortWindDescription);
            constraintDescription.setSpan(windspeedSpan, start, constraintDescription.length(), Spannable.SPAN_COMPOSING);
            if (alertTypeFlag == CUSTOM_ALERT) {
                constraintDescription.append(" on ");
                start = constraintDescription.length();
                constraintDescription.append(simpleDateFormat.format(calendar.getTime()));
                constraintDescription.setSpan(dateSpan, start, constraintDescription.length(), Spannable.SPAN_COMPOSING);
                textView.setText(constraintDescription);
            }
            else {
                textView.setText(constraintDescription);

            }

        }


    }



    // get Date only in unix-time format(seconds)
    public long calculateUnixDate() {
        // Set time fields to zero
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTimeInMillis() / 1000;
    }


}
