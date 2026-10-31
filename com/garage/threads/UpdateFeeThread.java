package com.garage.threads;

import com.garage.parking.ParkingSlot;

public class UpdateFeeThread implements Runnable {

    private ParkingSlot[][] parkingGrid;

    public UpdateFeeThread(ParkingSlot[][] parkingGrid) {
        this.parkingGrid = parkingGrid;
    }

    @Override
    public void run() {

        while (!Thread.currentThread().isInterrupted()) {

            synchronized (parkingGrid) {

                for (int f = 0; f < parkingGrid.length; f++) {

                    for (int s = 0; s < parkingGrid[f].length; s++) {

                        if (parkingGrid[f][s] != null) {

                            parkingGrid[f][s].updateParkingFee();

                        }
                    }
                }
            }

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                System.out.println("Fee calculation thread interrupted.");
                Thread.currentThread().interrupt();
                break;
            }
        }

        System.out.println("Fee calculator stopped.");
    }
}
