package com.garage.parking;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.Duration;

import com.garage.utils.CurrentTime;

public class ParkingSlot {
    int ticketId;
    int fee;

    String vehicleNumber;
    String entryTime;
    String exitTime;

    boolean isEV;

    static int currentTicketId = 101;

    public ParkingSlot() {

    }

    ParkingSlot(String VehicleNumber, String entryTime, boolean isEV) {
        this.ticketId = currentTicketId++;
        this.fee = 0;
        this.vehicleNumber = VehicleNumber;
        this.entryTime = entryTime;
        this.isEV = isEV;
    }

    // Car Entry Functionality.
    public ParkingSlot carIn(String vehicleNumber) {

        String formattedTime = CurrentTime.getTime();
        ParkingSlot newCar = new ParkingSlot(vehicleNumber, formattedTime, false);

        return newCar;
    }

    // Car Exit Funtionality.
    String carOut()
    {
      this.exitTime = CurrentTime.getTime();
      updateParkingFee();
      String billString = generateBill();

      return billString;
    }

    // Generating bill for exiting car.
    String generateBill()
    {
      return String.format(
        "========================================\n" +
        "           PARKING RECEIPT              \n" +
        "========================================\n" +
        "Ticket ID     : %d\n" +
        "Vehicle No.   : %s\n" +
        "Entry Time    : %s\n" +
        "Exit Time     : %s\n" +
        "----------------------------------------\n" +
        "TOTAL FEE     : ₹ %d\n" +
        "========================================\n",
        this.ticketId, this.vehicleNumber, this.entryTime, this.exitTime, this.fee
      );
    }
    
    // Updating parking fee.
    void updateParkingFee()
    {
      try {
        // 1. Parse the entry time string back into a LocalTime object
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalTime entry = LocalTime.parse(this.entryTime, formatter);
        
        // 2. Get current time (or you can parse this.exitTime)
        LocalTime exit = LocalTime.now();

        // 3. Calculate difference in seconds
        long secondsParked = Duration.between(entry, exit).getSeconds();
        
        // 4. Avoid negative time if car exits in the same second (or clock issue)
        if (secondsParked < 0) secondsParked = 0;

        // 5. Multiply by 0.1 and cast to int (to drop decimals)
        this.fee = (int) (secondsParked * 0.1);

        } catch (Exception e) {
          this.fee = 0; 
        }
      
      }
}