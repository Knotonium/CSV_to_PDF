import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CSV_Reader {

    public static List<Data> getListFromCSV(File filename) throws IOException {

        try {
            Reader in = new FileReader(filename);
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withDelimiter(',').withHeader().parse(in);
            ArrayList<Data> list = new ArrayList<>();

            for (CSVRecord record : records) {

                Data newData = new Data();
                newData.setWeekday(record.get("Wochentag"));
                newData.setDate(record.get("Datum"));
                newData.setSubject(record.get("Stunde_1_und_2"));
                newData.setUnit1(record.get("Stunde_1_und_2"));
                newData.setTeacherUnit1(record.get("Stunde_1_und_2_Dozent"));
                newData.setUnit2(record.get("Stunde_3_und_4"));
                newData.setTeacherUnit2(record.get("Stunde_3_und_4_Dozent"));
                newData.setUnit3(record.get("Stunde_5_und_6"));
                newData.setTeacherUnit3(record.get("Stunde_5_und_6_Dozent"));
                newData.setUnit4(record.get("Stunde_7_und_8"));
                newData.setTeacherUnit4(record.get("Stunde_7_und_8_Dozent"));

                list.add(newData);
            }
            return list;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}