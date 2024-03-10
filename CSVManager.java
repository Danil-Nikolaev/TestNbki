import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CSVManager {

    private static int getKey(String[] line) {
        return Integer.parseInt(line[0]);
    }
    private final String path;
    private final String splitBy = ",";
    private final int chunkSize = 1_000_000;

    private final String output = "output.csv";

    public CSVManager(String path) {
        this.path = path;
    }

    public void sortedFile() {
        String line;

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {

            File file = new File(output);
            if (file.exists())
                file.delete();
            file.createNewFile();

            List<String[]> data = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                data.add(line.split(splitBy));
                if (data.size() >= chunkSize)
                    sorted(data);
            }
            if (!data.isEmpty())
                sorted(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sorted(List<String[]> lines) throws IOException {
        lines.sort(Comparator.comparingInt(CSVManager::getKey));
        merge(lines);
        lines.clear();
    }

    private void merge(List<String[]> lines) throws IOException {
        String fileName = output;
        File tempFile = new File("temp.csv");

        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

        String line;
        int i = 0;
        while ((line = reader.readLine()) != null) {
            String[] rowInFile = line.split(splitBy);
            while (i < lines.size() && getKey(rowInFile) > getKey(lines.get(i))) {
                writer.write(String.join(",", lines.get(i)));
                writer.newLine();
                i++;
            }
            writer.write(line);
            writer.newLine();
        }

        while (i < lines.size()) {
            writer.write(String.join(",", lines.get(i)));
            writer.newLine();
            i++;
        }

        if (!tempFile.renameTo(new File(fileName))) {
            System.err.println("Failed to rename temp file to " + fileName);
        }

        reader.close();
        writer.close();
    }

}