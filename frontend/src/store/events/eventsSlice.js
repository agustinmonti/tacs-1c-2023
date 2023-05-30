import { createSlice } from '@reduxjs/toolkit';

export const eventsSlice = createSlice({
    name: 'events',
    initialState: {
        currentEvent: {},
        myEvents: [],
        participantEvents: []
    },
    reducers: {
        onSetCurrentEvent: (state, { payload } ) => {
            state.currentEvent = payload;
        },
        onRemoveCurrentEvent: (state) => {
            state.currentEvent = {};
        },
        onSetEvents: (state, { payload }) => {
            state.myEvents = payload.myEvents;
            state.participantEvents = payload.participantEvents;
        },
        onRemoveEvents: (state) => {
            state.myEvents = [];
        },
        onLogoutEvents: (state) => {
            state.myEvents = [];
            state.participantEvents = [];
            state.currentEvent = {};
        },
        onAddVote: (state, { payload }) => {
            state.currentEvent.options = state.currentEvent.options.map( option => {
                if( option.id === payload ){
                    return {
                        ...option,
                        votes: option.votes + 1,
                        selected: true
                    }
                }
                return option;
            })
        },
        onRemoveVote: (state, { payload }) => {
            state.currentEvent.options = state.currentEvent.options.map( option => {
                if( option.id === payload ){
                    return {
                        ...option,
                        votes: option.votes - 1,
                        selected: false
                    }
                }
                return option;
            })
        },
        onRemoveParticipant: (state, { payload }) => {
            state.currentEvent.participants = state.currentEvent.participants.filter( participant => participant.id !== payload )
        },
        onAddParticipant: (state, { payload }) => {
            state.currentEvent.participants.push( payload );
        },
        onChangeCurrentEventStatus: (state, { payload }) => {
            state.currentEvent.status = payload;
        },
        onDeleteCurrentEvent: (state, { payload }) => {
            state.currentEvent = {};
        },
    }
});


// Action creators are generated for each case reducer function
export const { 
    onSetCurrentEvent,
    onRemoveCurrentEvent,
    onSetEvents,
    onRemoveEvents,
    onLogoutEvents,
    onAddVote,
    onRemoveVote,
    onRemoveParticipant,
    onAddParticipant,
    onChangeCurrentEventStatus,
    onDeleteCurrentEvent
} = eventsSlice.actions;