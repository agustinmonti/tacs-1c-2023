import { createSlice } from "@reduxjs/toolkit";

export const uiSlice = createSlice({
	name: "ui",
	initialState: {
		isCreateEventModalOpen: false,
	},
	reducers: {
		onOpenCreateEventModal: (state) => {
			state.isCreateEventModalOpen = true;
		},
		onCloseCreateEventModal: (state) => {
			state.isCreateEventModalOpen = false;
		},

	},
});

// Action creators are generated for each case reducer function
export const { onOpenCreateEventModal, onCloseCreateEventModal } = uiSlice.actions;