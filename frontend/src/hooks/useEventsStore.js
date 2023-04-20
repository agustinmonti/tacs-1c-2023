import { useDispatch, useSelector } from "react-redux"

import Swal from 'sweetalert2'
import { api } from "../api";
import { onCloseCreateEventModal, onSetCurrentEvent } from "../store";
import { useNavigate } from "react-router-dom";

export const useEventsStore = () => {

    const dispatch = useDispatch();
    const navigate = useNavigate();

    const { currentEvent } = useSelector( state => state.events );

    const startCreatingEvent = async( newEvent ) => {

        try {

            const response = await api.post('/events', newEvent); 
            console.log(response);
            //const eventCreated = response.data.event;
            const eventCreated = newEvent;

            dispatch(onCloseCreateEventModal());
            dispatch(onSetCurrentEvent( eventCreated ));
            navigate(`/event/${ 1 }`);

        } catch (error) {
            console.log(error);
            Swal.fire('Error al guardar', 'El evento no se guardó correctamente', 'error');
        }
    }

    const startVoting = async( optionId ) => {

        try {

            const response = await api.post('/events', optionId); 
            console.log(response);

            
        } catch (error) {
            console.log(error);
            Swal.fire('Error al votar', 'El voto no se guardó correctamente', 'error');
        }

    }


    return {
        currentEvent,

        startCreatingEvent,
        startVoting,
    }
}