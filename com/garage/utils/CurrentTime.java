package com.garage.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CurrentTime {
  public static String getTime() {
    LocalDateTime now = LocalDateTime.now();
    // Format: YYYY-MM-DD HH:mm:ss
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    return now.format(formatter);
  }
}