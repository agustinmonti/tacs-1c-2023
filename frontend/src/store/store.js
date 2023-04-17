import { configureStore } from "@reduxjs/toolkit";
import { uiSlice, authSlice, eventsSlice } from './';

export const store = configureStore({
    reducer:{
        ui:         uiSlice.reducer,
        auth:       authSlice.reducer,
        events:     eventsSlice.reducer,
    },
    middleware: ( getDefaultMiddleware ) => getDefaultMiddleware({
        serializableCheck: false
    })
})