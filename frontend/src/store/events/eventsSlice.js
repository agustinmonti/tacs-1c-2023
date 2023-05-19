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
        onToggleVote: (state, { payload }) => {
            state.currentEvent.options = state.currentEvent.options.map( option => {
                if( option.id === payload ){
                    let votes = option.votes;
                    if( option.selected ){
                        votes--;
                    }else{
                        votes++;
                    }
                    return {
                        ...option,
                        votes:votes,
                        selected: !option.selected
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
        }
    }
});


// Action creators are generated for each case reducer function
export const { 
    onSetCurrentEvent,
    onRemoveCurrentEvent,
    onSetEvents,
    onRemoveEvents,
    onLogoutEvents,
    onToggleVote,
    onRemoveParticipant,
    onAddParticipant
} = eventsSlice.actions;