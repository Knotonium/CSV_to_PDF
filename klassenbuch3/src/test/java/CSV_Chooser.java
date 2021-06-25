import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class CSV_Chooser {
    public static String csvFilePath() {

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

        return chooser.getSelectedFile().getPath();
    }
}
