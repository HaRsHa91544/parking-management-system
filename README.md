
# Parking Management System

This is a real-world college project developed using Java for Object-Oriented Programming (OOPs). The project simulates a parking management system that helps a Parking Supervisor monitor vehicle entry, exit, parking slots, EV parking, and fee calculation in a simple and efficient way.

## Project Overview

The Parking Management System is designed to manage a small multi-floor parking garage. It allows a supervisor to:
- Park a vehicle in an available slot
- Remove a vehicle from the garage
- Track occupied and free parking slots
- Allocate EV vehicles to a reserved EV floor
- Automatically update parking charges
- Generate and save parking bills/receipts

This project is a practical example of OOP concepts such as:
- Classes and Objects
- Inheritance
- Encapsulation
- Abstraction
- Polymorphism
- Threading
- File Handling

## Features

- Multi-floor parking layout
- Slot-based parking management
- EV-only reserved floor
- Vehicle entry and exit handling
- Automatic fee calculation
- Billing and receipt generation
- Real-time fee updates using thread-based logic
- Receipts saved in a file named `receipts.txt`

## Tech Stack

- Java
- Object-Oriented Programming (OOPs)
- Java Threads
- File I/O
- Console-based User Interface

## Project Structure

```text
parking-management-system
├── com
│   └── garage
│       ├── Main.java
│       ├── exceptions
│       │   └── ParkingSlotsFullException.java
│       ├── parking
│       │   ├── EVReservedSlot.java
│       │   ├── ParkingSlot.java
│       │   └── TicketablePark.java
│       ├── threads
│       │   └── UpdateFeeThread.java
│       └── utils
│           └── CurrentTime.java
├── receipts.txt
└── README.md
```

## How It Works

1. The supervisor starts the system.
2. The program displays the parking layout.
3. The user chooses whether to park or remove a vehicle.
4. The system validates floor and slot numbers.
5. EV vehicles are restricted to the reserved EV floor.
6. Parking fees are updated automatically while the vehicle remains parked.
7. On exit, a bill is generated and saved to `receipts.txt`.

## Example Use Cases

- Managing car parking in a college campus
- Simple parking supervisor dashboard
- EV reserved area management
- Parking fee calculation based on duration

## How to Run

Make sure Java is installed on your system, then run:

```bash
javac com/garage/Main.java
java com.garage.Main
```

If needed, you can compile the whole project from the root directory using:

```bash
javac com/garage/**/*.java
```

Then run:

```bash
java com.garage.Main
```

## Conclusion

This project demonstrates a simple yet effective parking management solution using Java and OOP principles. It is suitable for academic learning and showcases how software can be used to support real-world parking operations in a practical and structured way.
