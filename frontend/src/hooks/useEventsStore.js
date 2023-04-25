import { useDispatch, useSelector } from "react-redux"

import Swal from 'sweetalert2'
import { api } from "../api";
import { onCloseCreateEventModal, onSetCurrentEvent, onSetEvents } from "../store";
import { useNavigate } from "react-router-dom";

export const useEventsStore = () => {

    const dispatch = useDispatch();
    const navigate = useNavigate();

    const { currentEvent, events } = useSelector( state => state.events );
    const { user } = useSelector( state => state.auth );

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

    const startGettingEvents = async () => {
        
        try {

            //const { status, data } = await api.get(`/events?user=${ user.id }`);

            const { status, data } = {
                status: 200,
                data: {
                    events: [
                        {
                            id: 1,
                            name: 'Evento 1',
                            description: 'Descripción del evento 1, hola probando uno dos tres',
                            status: 'Activo',
                            totalParticipants: 50
                        },
                        {
                            id: 2,
                            name: 'Evento 2',
                            description: 'Descripción del evento 2, hola',
                            status: 'Cerrado',
                            totalParticipants: 21
                        },
                        {
                            id: 3,
                            name: 'Evento 2',
                            description: 'Descripción del evento 3, hola esto va a ser una descripción muy larga así que preparense para hacer un corte en algún lado si no quieren que se rompa todo el front de este lindo evento con descripción larga',
                            status: 'Activo',
                            totalParticipants: 0
                        }
                    ]
                }
            }

            if( status === 200 ){
                dispatch(onSetEvents( data.events ));
            }else{
                console.error(data.msg);
            }
            
        } catch (error) {
            console.error(error);
        }
    }


    return {
        currentEvent,
        events,

        startCreatingEvent,
        startVoting,
        startGettingEvents
    }
}