import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CSV_Reader {
    // Mit List hat Benutzer genaue Kontrolle darüber, wo in der Liste jedes Element eingefügt wird.
    // Man kann auf Elemente über ihren ganzzahligen Index (Position in der Liste) zugreifen und nach Elementen in der Liste suchen.
    // Im Gegensatz zu Mengen sind in Listen typischerweise doppelte Elemente erlaubt.
    // Genauer gesagt, erlauben Listen typischerweise Paare von Elementen e1 und e2, so dass e1.equals(e2) ist,
    // und sie erlauben typischerweise mehrere Nullelemente, wenn sie überhaupt Nullelemente erlauben.

    public static List<Data> getDataFromCSV(File filename) throws IOException {

        try {
            Reader in = new FileReader(filename);
            // Durch die Implementierung des Iterable-Interface kann ein Objekt das Ziel der erweiterten for-Anweisung (manchmal auch "for-each-Schleife" genannt) sein.
            Iterable<CSVRecord> csvRecords = CSVFormat.DEFAULT.withDelimiter(',').withHeader().parse(in);
            // Resizable-Array-Implementierung der List-Schnittstelle. Implementiert alle optionalen Listenoperationen und erlaubt alle Elemente, einschließlich Null.
            // Zusätzlich zur Implementierung der List-Schnittstelle bietet diese Klasse Methoden zur Manipulation der Größe des Arrays,
            // das intern zum Speichern der Liste verwendet wird.
            ArrayList<Data> dataArrayList = new ArrayList<>();

            for (CSVRecord csvRecord : csvRecords) {
                // Data Object wird für jede Zeile der CSV neu erstellt
                Data newData = new Data();
                newData.setDate(csvRecord.get("Datum"));
                newData.setWeekday(csvRecord.get("Wochentag"));
                newData.setSubject(csvRecord.get("Stunde_1_und_2"));
                newData.setUnit1(csvRecord.get("Stunde_1_und_2"));
                newData.setTeacherUnit1(csvRecord.get("Stunde_1_und_2_Dozent"));
                newData.setUnit2(csvRecord.get("Stunde_3_und_4"));
                newData.setTeacherUnit2(csvRecord.get("Stunde_3_und_4_Dozent"));
                newData.setUnit3(csvRecord.get("Stunde_5_und_6"));
                newData.setTeacherUnit3(csvRecord.get("Stunde_5_und_6_Dozent"));
                newData.setUnit4(csvRecord.get("Stunde_7_und_8"));
                newData.setTeacherUnit4(csvRecord.get("Stunde_7_und_8_Dozent"));
                // Objekte werden in die ArrayList geschrieben
                dataArrayList.add(newData);
            }
            return dataArrayList;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}