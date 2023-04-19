import { useDispatch, useSelector } from "react-redux"

import Swal from 'sweetalert2'
import { api } from "../api";

export const useEventsStore = () => {

    const dispatch = useDispatch();

    const { currentEvent } = useSelector( state => state.events );

    const startCreatingEvent = async( newEvent ) => {

        try {

            const response = await api.post('/events', newEvent); 
            console.log(response)
        } catch (error) {
            console.log(error);
            Swal.fire('Error al guardar', error.response, 'error');
        }
    }


    return {
        currentEvent,

        startCreatingEvent
    }
}