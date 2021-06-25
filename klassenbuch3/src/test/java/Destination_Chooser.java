import javax.swing.*;

public class Destination_Chooser {
    public static String pdfFilePath() {
        // JFileChooser-Objekt erstellen
        JFileChooser chooser = new JFileChooser();
        // Dialog zum Speichern von Dateien anzeigen
        return String.valueOf(chooser.showSaveDialog(null));

    }
}
