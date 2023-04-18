package org.grupo.tacs.extras;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Helper {
    public static String getReadableDate(LocalDateTime date){
        DateTimeFormatter formatterLocalDateTime =
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return formatterLocalDateTime.format(date);
    }
}
