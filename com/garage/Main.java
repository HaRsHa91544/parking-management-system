package com.garage;

import com.garage.utils.CurrentTime;
import com.garage.parking.*;

import java.util.Scanner;
import com.garage.threads.UpdateFeeThread;

import com.garage.exceptions.ParkingSlotsFullException;

public class Main {
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    TicketablePark fileLogger = new TicketablePark();

    // Manage number of floors and slots per floor in garage here!
    int TOTAL_FLOORS = 2; // Minimum 2 must be there.
    int TOTAL_SLOTS_PER_FLOOR = 2;

    // 2D - Array of slots.
    ParkingSlot[][] parkingGrid = new ParkingSlot[TOTAL_FLOORS][TOTAL_SLOTS_PER_FLOOR];

    // EV reserved floor, last floor in all garages.
    int EV_FLOOR_INDEX = TOTAL_FLOORS - 1;

    UpdateFeeThread feeUpdater = new UpdateFeeThread(parkingGrid);
    Thread feeUpdateThread = new Thread(feeUpdater);
    feeUpdateThread.start();

    while (true) {
      System.out.println("\n--- PARKING LAYOUT ---");

      // Print top headers (Slots)
      System.out.print("               ");
      for (int s = 0; s < TOTAL_SLOTS_PER_FLOOR; s++) {
        System.out.printf("  S%d   ", (s + 1));
      }
      System.out.println();

      // Print rows (Floors)
      for (int f = 0; f < TOTAL_FLOORS; f++) {

        // Visual indicator for EV floor
        String floorLabel = (f == EV_FLOOR_INDEX) ? String.format("Floor %d (EV) | ", (f + 1))
            : String.format("Floor %d      | ", (f + 1));

        System.out.print(floorLabel);

        for (int s = 0; s < TOTAL_SLOTS_PER_FLOOR; s++) {
          if (parkingGrid[f][s] == null) {
            System.out.printf("[ S-%d ] ", (s + 1));
          } else {
            System.out.printf("[ OCC ] ");
          }
        }
        System.out.println();
      }
      System.out.println("----------------------\n");

      System.out.println("1. Park Car\n2. Exit Car\n3. Quit");
      System.out.print("Choose: ");
      int choice = scanner.nextInt();
      scanner.nextLine(); // clear buffer

      if (choice == 1) {

        try {
          // Check whether the entire garage is full
          if (isGarageFull(parkingGrid)) {
            throw new ParkingSlotsFullException("Parking Garage is Full!");
          }

          // 1. Get vehicle number
          System.out.print("Enter Vehicle Number: ");
          String vehicle = scanner.nextLine();

          // 2. Check whether it is an EV
          System.out.print("Is this an EV? (yes/no): ");
          String isEvInput = scanner.nextLine();

          boolean isEV = isEvInput.equalsIgnoreCase("yes")
              || isEvInput.equalsIgnoreCase("y");

          int floor = -1;

          // 3. Select floor
          if (isEV) {
            System.out.println("-> EVs can only park on Floor " + (EV_FLOOR_INDEX + 1) + ".");
            floor = EV_FLOOR_INDEX; // Uses the dynamic EV floor
          } else {
            System.out.println("-> Normal cars can only park on Floors 1 to " + EV_FLOOR_INDEX + ".");
            System.out.print("Enter Floor (1 to " + EV_FLOOR_INDEX + "): ");
            floor = scanner.nextInt() - 1;
            scanner.nextLine();
          }

          // 4. Select slot
          System.out.print("Enter Slot (1-" + TOTAL_SLOTS_PER_FLOOR + "): ");
          int slot = scanner.nextInt() - 1;
          scanner.nextLine();

          // 5. Validate floor and slot
          boolean validFloor = isEV ? (floor == EV_FLOOR_INDEX) : (floor >= 0 && floor < EV_FLOOR_INDEX);
          if (validFloor && slot >= 0 && slot < TOTAL_SLOTS_PER_FLOOR) {

            synchronized (parkingGrid) {

              if (parkingGrid[floor][slot] == null) {

                String entryTime = CurrentTime.getTime();

                // 6. Create appropriate object
                if (isEV) {
                  EVReservedSlot evCar = new EVReservedSlot(vehicle, entryTime);
                  parkingGrid[floor][slot] = evCar;
                  evCar.startCharging();
                } else {
                  parkingGrid[floor][slot] = new ParkingSlot().carIn(vehicle);
                }

                System.out.println("Parked " + vehicle
                    + " at Floor " + (floor + 1)
                    + ", Slot " + (slot + 1));

              } else {
                System.out.println("Slot is occupied!");
              }
            }

          } else {
            System.out.println("Invalid Floor or Slot!");
          }

        } catch (ParkingSlotsFullException e) {
          System.out.println(e.getMessage());
        }

      } else if (choice == 2) {
        System.out.print("Enter Floor to exit (1-" + TOTAL_FLOORS + "): ");
        int floor = scanner.nextInt() - 1;
        
        System.out.print("Enter Slot to exit (1-" + TOTAL_SLOTS_PER_FLOOR + "): ");
        int slot = scanner.nextInt() - 1;
        scanner.nextLine(); // clear buffer


        if (floor >= 0 && floor < TOTAL_FLOORS && slot >= 0 && slot < TOTAL_SLOTS_PER_FLOOR) {
          synchronized (parkingGrid) {

            ParkingSlot car = parkingGrid[floor][slot];

            if (car != null) {

              if (car instanceof EVReservedSlot) {
                ((EVReservedSlot) car).stopCharging();
              }

              fileLogger.processCarExit(car);

              parkingGrid[floor][slot] = null;

              System.out.println("Car exited and bill saved to receipts.txt.");

            } else {
              System.out.println("No car at this slot!");
            }
          }
        } else {
          System.out.println("Invalid Floor or Slot!");
        }
      }

      else if (choice == 3) {
        feeUpdateThread.interrupt(); // Stop the fee update thread
        System.out.println("Goodbye!");
        break;
      } else {
        System.out.println("Invalid choice.");
      }
    }
    scanner.close();
  }

  public static boolean isGarageFull(ParkingSlot[][] grid) {

    for (int i = 0; i < grid.length; i++) {
      for (int j = 0; j < grid[i].length; j++) {

        if (grid[i][j] == null) {
          return false;
        }
      }
    }

    return true;
  }

}