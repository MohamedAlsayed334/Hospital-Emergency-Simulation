import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class Hospital
{
    public static void main(String[] args) throws InterruptedException {
        String Terminal_UI =
                "⠀⠀⣠⣴⣶⣦⣄⡀⠀⠀⠀⠀⠀⠀⠀⠀⣠⣤⣶⣶⣤⣄⠀⠀\n" +
                "⣰⣿⣿⣿⠁⢹⣿⣿⣦⡀⠀⠀⠀⠀⣠⣾⣿⣿⠁⢹⣿⣿⣷⡄\n" +
                "⣿⠟⠛⣿⣿⣿⡟⠛⣿⣿⣦⡀⠀⢾⣿⡟⠛⣿⣿⣿⡟⠛⣿⡗\n" +
                "⢿⣷⣶⣿⠿⣿⣷⣶⣿⡿⢿⣿⣦⡀⠙⢷⣶⣿⠿⢿⣷⣶⣿⠏\n" +
                "⠀⠻⣿⣿⣤⣴⣿⣿⣿⣤⣴⣿⣿⣿⣦⡀⠙⢿⣄⣼⣿⡿⠋⠀ ██  ██  ▄▄▄   ▄▄▄▄ ▄▄▄▄  ▄▄ ▄▄▄▄▄▄ ▄▄▄  ▄▄      ██████ ▄▄   ▄▄ ▄▄▄▄▄ ▄▄▄▄   ▄▄▄▄ ▄▄▄▄▄ ▄▄  ▄▄  ▄▄▄▄ ▄▄ ▄▄   █████▄   ▄▄▄   ▄▄▄  ▄▄   ▄▄   ▄█████ ▄▄ ▄▄   ▄▄ ▄▄ ▄▄ ▄▄     ▄▄▄ ▄▄▄▄▄▄ ▄▄  ▄▄▄  ▄▄  ▄▄ \n" +
                "⠀⠀⠀⠻⣿⣿⣏⣀⣿⣿⣿⣿⣿⣿⣿⣿⣦⡀⠙⠿⠋⠀⠀⠀  ██████ ██▀██ ███▄▄ ██▄█▀ ██   ██  ██▀██ ██      ██▄▄   ██▀▄▀██ ██▄▄  ██▄█▄ ██ ▄▄ ██▄▄  ███▄██ ██▀▀▀ ▀███▀   ██▄▄██▄ ██▀██ ██▀██ ██▀▄▀██   ▀▀▀▄▄▄ ██ ██▀▄▀██ ██ ██ ██    ██▀██  ██   ██ ██▀██ ███▄██ \n" +
                "⠀⠀⠀⠀⠀⠻⣿⣿⣿⣿⣿⣿⣿⣿⣿⡿⢿⣿⣦⡀⠀⠀⠀⠀   ██  ██ ▀███▀ ▄▄██▀ ██    ██   ██  ██▀██ ██▄▄▄   ██▄▄▄▄ ██   ██ ██▄▄▄ ██ ██ ▀███▀ ██▄▄▄ ██ ▀██ ▀████   █     ██   ██ ▀███▀ ▀███▀ ██   ██   █████▀ ██ ██   ██ ▀███▀ ██▄▄▄ ██▀██  ██   ██ ▀███▀ ██ ▀██ \n" +
                "⠀⠀⣠⣾⣷⣄⠀⠻⣿⣿⣿⣿⣿⣿⣿⣇⣀⣿⣿⣿⣦⡀⠀⠀\n" +
                "⣠⣾⣿⣿⠉⢹⣷⣄⠀⠻⣿⣿⣏⠉⢹⣿⣿⣿⠁⢹⣿⣿⣦⠀\n" +
                "⣿⠟⠛⣿⣿⣿⡟⠛⣷⣄⠀⠻⣿⣿⣿⡟⠛⣿⣿⣿⡟⠛⣿⡧\n" +
                "⣿⣷⣶⣿⠿⢿⣷⣶⣿⡿⠃⠀⠀⠻⣿⣷⣶⣿⠿⢿⣷⣶⣿⡗\n" +
                "⠙⢿⣿⣿⣤⣴⣿⡿⠋⠀⠀⠀⠀⠀⠀⠻⣿⣿⣤⣼⣿⣿⠟⠀\n" +
                "⠀⠀⠉⠙⠙⠋⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠋⠋⠉⠀⠀⠀";
        System.out.println(Terminal_UI);
        System.out.println("Please Enter the following (: ");
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
            Thread d = new Thread(new Doctor(i, waitingRoom, rooms, treated));
            d.setDaemon(true);
            d.start();
        }

        Patient[] patients = new Patient[numPatients];
        StringBuilder labels = new StringBuilder();
        for (int i = 1; i <= numPatients; i++) {
            patients[i - 1] = new Patient(i, waitingRoom);
            labels.append("P").append(i).append(" ");
        }
        Logger.log("Patients arriving:", labels.toString().trim());

        for (Patient p : patients) {
            p.arrive();      // prints "Pn arrived." then enters or waits outside
        }

        while (treated.get() < numPatients) {
            Thread.sleep(100);
        }

        Logger.log("All patients have been treated.", "Simulation completed.");
    }
}
