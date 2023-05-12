package org.grupo.tacs.extras;

import org.grupo.tacs.model.Event;
import org.grupo.tacs.model.EventOption;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Helper {
    public static String getReadableDate(LocalDateTime date){
        DateTimeFormatter formatterLocalDateTime =
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return formatterLocalDateTime.format(date);
    }
}
