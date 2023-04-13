import { configureStore } from "@reduxjs/toolkit";
import { uiSlice, authSlice } from './';

export const store = configureStore({
    reducer:{
        ui:         uiSlice.reducer,
        auth:       authSlice.reducer,
    },
    middleware: ( getDefaultMiddleware ) => getDefaultMiddleware({
        serializableCheck: false
    })
})