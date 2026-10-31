package com.garage.parking;

import java.io.FileWriter;
import java.io.IOException;

public class TicketablePark {
    
    public void processCarExit(ParkingSlot slot) {
        // 1. Call carOut, which prints the bill and returns the string
        String billData = slot.carOut();

        // 2. Append that single string into the file
        try (FileWriter writer = new FileWriter("receipts.txt", true)) {
            writer.write(billData);
            writer.write("\n"); // Add an extra line between bills
        } catch (IOException e) {
            System.out.println("Error saving to file: " + e.getMessage());
        }
    }
}