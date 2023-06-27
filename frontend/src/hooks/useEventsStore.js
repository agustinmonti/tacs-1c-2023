import { useDispatch, useSelector } from "react-redux"

import Swal from 'sweetalert2'
import { api } from "../api";
import { onAddParticipant, onAddVote, onChangeCurrentEventStatus, onCloseCreateEventModal, onRemoveParticipant, onRemoveVote, onSetCurrentEvent, onSetEvents, onStartLoading, onStopLoading } from "../store";
import { useNavigate } from "react-router-dom";
import { addHours, format } from "date-fns";
import { useMemo } from "react";
import { parseOptions } from "../helpers";

export const useEventsStore = () => {

    const dispatch = useDispatch();
    const navigate = useNavigate();

    const { currentEvent, myEvents, participantEvents } = useSelector( state => state.events );
    const { user } = useSelector( state => state.auth );

    const isOwner = useMemo(() => {
        return currentEvent.owner?.id === user?._id;
    }, [ currentEvent.owner ]);

    const isParticipating = useMemo (() => {
        return currentEvent.participants?.some( participant => participant.id === user._id );
    }, [ currentEvent.participants ]);

    const isActive = useMemo(() => {
        return currentEvent.status === 'Active'
    }, [ currentEvent.status ]);

    const startCreatingEvent = async( newEvent ) => {

        const eventToCreate = { ...newEvent };

        eventToCreate.options = newEvent.options.map( option => {
            return {
                ...option,
                start: format(option.start,"yyyy-MM-dd'T'HH:mm:ss'Z'"),
                end: format(option.end,"yyyy-MM-dd'T'HH:mm:ss'Z'")
            }
        })

        try {

            const { status, data } = await api.post('/events', eventToCreate); 

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

    const startAddingVote = async( optionId ) => {
        try {

            const {status, data} = await api.put(`/events/${currentEvent.id}/vote`, { optionIndex: optionId } );

            if( status === 201 ){
                dispatch(onAddVote(optionId));
            }else{
                console.error(data.msg);
            }
            
        } catch (error) {
            console.log(error);
            Swal.fire('Error al votar', 'El voto no se guardó correctamente', 'error');
        }
    }

    const startRemovingVote = async( optionId ) => {
        try {

            const {status, data} = await api.delete(`/events/${currentEvent.id}/vote`, {data : { optionIndex: optionId }} );

            if( status === 200 ){
                dispatch(onRemoveVote(optionId));
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
            const { status, data } = await api.get(`/events?userId=${ user._id }`);

            if( status === 200 ){
                dispatch(onSetEvents( {
                    myEvents: data.myEvents,
                    participantEvents: data.participants
                } ));
            }else{
                console.error(data.msg);
            }
            
        } catch (error) {
            console.error(error);
        }
    }

    const startGettingEvent = async ( id ) => {
        try {
            const { status, data } = await api.get(`/events/${ id }`);

            data.event.options = parseOptions( data.event.options, data.optionsVoted );
            data.event.createdDate = new Date( data.event.createdDate );

            if( status === 200 ){
                dispatch(onSetCurrentEvent( data.event ));
            }else{
                console.error(data.msg);
            }
            
        } catch (error) {
            console.error(error);
        }
    }

    const startSignUpForCurrentEvent = async () => {
        const newParticipant = {
            id: user._id,
            email: user.email
        }

        try {

            const { status, data } = await api.put(`/events/${ currentEvent.id }/participant`);

            if( status === 201 ){
                dispatch( onAddParticipant( newParticipant ));
            }else{
                console.error(error);
            }

        } catch (error) {
            console.error(error);
            Swal.fire('Error al participar', 'La participación no se guardó correctamente', 'error');
        }
    }

    const startRemoveSignUpForCurrentEvent = async () => {
        try {

            const { status, data } = await api.delete(`/events/${ currentEvent.id }/participant`);

            if( status === 200 ){
                dispatch( onRemoveParticipant( user._id ));
            }else{
                console.error(error);
            }

        } catch (error) {
            console.error(error);
            Swal.fire('Error al participar', 'La participación no se guardó correctamente', 'error');
        }
    }

    const startChangingEventStatus = async ( newStatus ) => {
        try {

            const { status, data } = await api.put(`/events/${ currentEvent.id }`, newStatus);

            if( status === 201 ){
                dispatch( onChangeCurrentEventStatus('Cerrado') );
                Swal.fire('Votación cerrada', data.msg, 'success');
            }else{
                console.error(error);
                Swal.fire('Error al cambiar el estado', data.msg, 'error');
            }

        } catch (error) {
            console.error(error);
            Swal.fire('Error al cambiar el estado', data.msg, 'error');
        }
    }

    const startDeletingEvent = async () => {
        if( !isOwner ) return;

        try {

            const { status, data } = await api.delete(`/events/${ currentEvent.id }`);

            if( status === 200 ){
                Swal.fire('Evento eliminado', data.msg, 'success');
                navigate(`/`);
            }else{
                console.error(error);
                Swal.fire('Error al eliminar', data.msg, 'error');
            }

        } catch (error) {
            console.error(error);
            Swal.fire('Error al eliminar', data.msg, 'error');
        }
    }


    return {
        currentEvent,
        myEvents,
        participantEvents,
        isOwner,
        isParticipating,
        isActive,

        startCreatingEvent,
        startAddingVote,
        startRemovingVote,
        startGettingEvents,
        startGettingEvent,
        startSignUpForCurrentEvent,
        startRemoveSignUpForCurrentEvent,
        startChangingEventStatus,
        startDeletingEvent
    }
}