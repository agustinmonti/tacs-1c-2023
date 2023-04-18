import { createSlice } from '@reduxjs/toolkit';

export const eventsSlice = createSlice({
    name: 'events',
    initialState: {
        currentEvent: {}
    },
    reducers: {
        onSetCurrentEvent: (state, { payload } ) => {
            state.currentEvent = payload;
        },
        onRemoveCurrentEvent: (state) => {
            state.currentEvent = {};
        }
    }
});


// Action creators are generated for each case reducer function
export const { 
    onSetCurrentEvent,
    onRemoveCurrentEvent,
    onAddNewOptionToCurrentEvent,
    onRemoveOptionToCurrentEvent
} = eventsSlice.actions;