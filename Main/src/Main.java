import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Scanner in = new Scanner(System.in);
        System.out.print("Waiting room capacity: ");
        int capacity = in.nextInt();
        System.out.print("Number of doctors: ");
        int numDoctors = in.nextInt();
        System.out.print("Number of patients: ");
        int numPatients = in.nextInt();
        in.close();
        
        WaitingRoom waitingRoom = new WaitingRoom(capacity);
        TreatmentRooms rooms = new TreatmentRooms();
        AtomicInteger treated = new AtomicInteger(0);

        for (int i = 1; i <= numDoctors; i++) {
            Thread d = new Thread(new Doctor(i, waitingRoom, rooms, treated, numPatients));
            d.setDaemon(true);
            d.start();
        }

        for (int i = 1; i <= numPatients; i++) {
            new Thread(new Patient(i, waitingRoom)).start();
        }

        while (treated.get() < numPatients) {
            Thread.sleep(100);
        }

        Logger.log("All patients have been treated.", "Simulation completed.");
    }
}
