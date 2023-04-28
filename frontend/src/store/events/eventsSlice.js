import { createSlice } from '@reduxjs/toolkit';

export const eventsSlice = createSlice({
    name: 'events',
    initialState: {
        currentEvent: {},
        events: []
    },
    reducers: {
        onSetCurrentEvent: (state, { payload } ) => {
            state.currentEvent = payload;
        },
        onRemoveCurrentEvent: (state) => {
            state.currentEvent = {};
        },
        onSetEvents: (state, { payload }) => {
            state.events = payload;
        },
        onRemoveEvents: (state) => {
            state.events = [];
        },
        onLogoutEvents: (state) => {
            state.events = [],
            state.currentEvent = {}
        }
    }
});


// Action creators are generated for each case reducer function
export const { 
    onSetCurrentEvent,
    onRemoveCurrentEvent,
    onSetEvents,
    onRemoveEvents,
    onLogoutEvents
} = eventsSlice.actions;