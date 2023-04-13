import { createSlice } from "@reduxjs/toolkit";

export const uiSlice = createSlice({
	name: "ui",
	initialState: {
		isModalOpen: false,
	},
	reducers: {
		onOpenModal: (state) => {
			state.isDateOpen = true;
		},

	},
});

// Action creators are generated for each case reducer function
export const { onOpenModal } = uiSlice.actions;