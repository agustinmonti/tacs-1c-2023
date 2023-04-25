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

            const { status, data } = await api.post('/events', newEvent); 

            if( status === 201 ){
                dispatch(onCloseCreateEventModal());
                navigate(`/event/${ data.id }`);
            }else{
                console.error( data.msg );
            }

        } catch (error) {
            console.log(error);
            Swal.fire('Error al guardar', 'El evento no se guardó correctamente', 'error');
        }
    }

    const startVoting = async( optionIds = [] ) => {

        try {

            const { status, data } = await api.post(`/events/${currentEvent.id}/vote`, optionIds );

            if( status === 201 ){
                console.log('Votado con exito');
            }else{
                console.error(data.msg);
            }
            
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