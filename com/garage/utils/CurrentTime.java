package com.garage.utils;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class CurrentTime {
  public static String getTime() {
    LocalTime now = LocalTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    return now.format(formatter);
  }
}