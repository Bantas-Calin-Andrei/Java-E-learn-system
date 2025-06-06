import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class AuditService {
    private static final String FILE_NAME = "audit.csv";

    public static void log(String action) {
        try (FileWriter writer = new FileWriter(FILE_NAME, true)) {
            // fiecare linie: nume_actiune, TIMESTAMP
            writer.append(action)
                    .append(",")
                    .append(LocalDateTime.now().toString())
                    .append("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
