import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Epoch_Converter {
    public static String epochConverter(String epoch){
        LocalDate ddMMyyyy = LocalDate.ofInstant(Instant.ofEpochSecond(Long.parseLong(epoch)), ZoneId.systemDefault());
        DateTimeFormatter f = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return ddMMyyyy.format(f);
    }
}
