import { useDispatch, useSelector } from "react-redux"
import { onCloseCreateEventModal, onOpenCreateEventModal } from "../store";

export const useUiStore = () => {

    const dispatch = useDispatch();

    const { 
        isCreateEventModalOpen, 
    } = useSelector( state => state.ui );

    const openCreateEventModal = () => {
        dispatch( onOpenCreateEventModal() );
    }

    const closeCreateEventModal = () => {
        dispatch( onCloseCreateEventModal() );
    }

    return {
        isCreateEventModalOpen,

        openCreateEventModal,
        closeCreateEventModal,
    }
}