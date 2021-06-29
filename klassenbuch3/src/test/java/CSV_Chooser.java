import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;

public class CSV_Chooser {
    public static File csvFilePath() {

        // JFileChooser-Objekt erstellen
        JFileChooser chooser = new JFileChooser();

        // Filter fuer CSV-Dateityp
        chooser.setAcceptAllFileFilterUsed(false);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV-Dateien", "csv");
        chooser.addChoosableFileFilter(filter);

        // Dialog zum Oeffnen von Dateien anzeigen
        int returnValue = chooser.showOpenDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            System.out.println(chooser.getSelectedFile().getPath());
        }
        return new File(chooser.getSelectedFile().getPath());
    }
}
