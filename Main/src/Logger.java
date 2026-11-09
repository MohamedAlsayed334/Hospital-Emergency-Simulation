public class Logger {
    public static  synchronized void log(String... message) {
        for (String m : message) {
            System.out.println(m + '\n');
        }
    }
}
