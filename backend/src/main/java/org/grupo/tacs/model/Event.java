package org.grupo.tacs.model;

import java.time.LocalDateTime;
import java.util.List;

public class Event {
    Long id;
    EventOption OpcionSeleccionada;

    User creador;

    LocalDateTime tiempoDeCreacion;

    Event(User creador){
        this.creador=creador;
        this.tiempoDeCreacion = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public EventOption getOpcionSeleccionada() {
        return OpcionSeleccionada;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
