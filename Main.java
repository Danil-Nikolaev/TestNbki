
public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Введите путь до вашего файла");
            return;
        }
        String path = args[0];

        CSVManager csvManager = new CSVManager(path);
        csvManager.sortedFile();
    }
}
