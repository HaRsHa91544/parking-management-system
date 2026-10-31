package com.garage.parking;

public class EVReservedSlot extends ParkingSlot {

    public EVReservedSlot(String vehicleNumber, String entryTime) {
        super(vehicleNumber, entryTime, true); 
    }

    public void startCharging()
    {
        System.out.println("\n\n🔋 Charger Connected.");
    }

    public void stopCharging()
    {
        System.out.println("\n\n🔋 Charger Disconnected.");
    }
}