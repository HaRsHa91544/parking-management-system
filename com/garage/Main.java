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

    // 3 Floors, 5 Slots. null = Vacant, ParkingSlot = Occupied
    ParkingSlot[][] parkingGrid = new ParkingSlot[3][5];

    UpdateFeeThread feeUpdater = new UpdateFeeThread(parkingGrid);
    Thread feeUpdateThread = new Thread(feeUpdater);
    feeUpdateThread.start();

    while (true) {
      System.out.println("\n--- PARKING LAYOUT ---");

      // Print top headers (Slots)
      System.out.print("               ");
      for (int s = 0; s < 5; s++) {
        System.out.printf("  S%d   ", (s + 1));
      }
      System.out.println();

      // Print rows (Floors)
      for (int f = 0; f < 3; f++) {
        // Just visual indicator for EV floor
        String floorLabel = (f == 2) ? "Floor 3 (EV) | " : String.format("Floor %d      | ", (f + 1));
        System.out.print(floorLabel);

        for (int s = 0; s < 5; s++) {
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
                System.out.println("-> EVs can only park on Floor 3.");
                floor = 2;
            } else {
                System.out.println("-> Normal cars can only park on Floors 1 and 2.");
                System.out.print("Enter Floor (1 or 2): ");
                floor = scanner.nextInt() - 1;
                scanner.nextLine();
            }

            // 4. Select slot
            System.out.print("Enter Slot (1-5): ");
            int slot = scanner.nextInt() - 1;
            scanner.nextLine();

            // 5. Validate floor and slot
            boolean validFloor = isEV ? floor == 2 : floor >= 0 && floor <= 1;

            if (validFloor && slot >= 0 && slot < 5) {

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




        // // 1. Take car number first
        // System.out.print("Enter Vehicle Number: ");
        // String vehicle = scanner.nextLine();

        // // 2. Ask if it's an EV
        // System.out.print("Is this an EV? (yes/no): ");
        // String isEvInput = scanner.nextLine();
        // boolean isEV = isEvInput.equalsIgnoreCase("yes") || isEvInput.equalsIgnoreCase("y")
        //     || isEvInput.equalsIgnoreCase("s");

        // int floor = -1;

        // // 3. Show only respective floors
        // if (isEV) {
        //   System.out.println("-> EVs can only park on Floor 3.");
        //   floor = 2; // 0-based index for Floor 3
        // } else {
        //   System.out.println("-> Normal cars can only park on Floors 1 and 2.");
        //   System.out.print("Enter Floor (1 or 2): ");
        //   floor = scanner.nextInt() - 1;
        //   scanner.nextLine(); // clear buffer
        // }

        // System.out.print("Enter Slot (1-5): ");
        // int slot = scanner.nextInt() - 1;
        // scanner.nextLine(); // clear buffer

        // // 4. Validate and Park
        // if (floor >= 0 && floor < 3 && slot >= 0 && slot < 5) {

        //   synchronized (parkingGrid) {
        //     if (parkingGrid[floor][slot] == null) {
        //       String entryTime = CurrentTime.getTime();

        //       // Create the object (EVReservedSlot extends ParkingSlot, so it fits in the
        //       // array)
        //       if (isEV) {
        //         EVReservedSlot evCar = new EVReservedSlot(vehicle, entryTime);
        //         parkingGrid[floor][slot] = evCar;
        //         evCar.startCharging();
        //       } else {
        //         parkingGrid[floor][slot] = new ParkingSlot().carIn(vehicle);
        //       }

        //       System.out.println("Parked " + vehicle + " at Floor " + (floor + 1) + ", Slot " + (slot + 1));
        //     } else {
        //       System.out.println("Slot is occupied!");
        //     }
        //   }
        // } else {
        //   System.out.println("Invalid Floor or Slot!");
        // }

      } else if (choice == 2) {
        System.out.print("Enter Floor to exit (1-3): ");
        int floor = scanner.nextInt() - 1;
        System.out.print("Enter Slot to exit (1-5): ");
        int slot = scanner.nextInt() - 1;
        scanner.nextLine(); // clear buffer

        if (floor >= 0 && floor < 3 && slot >= 0 && slot < 5) {
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