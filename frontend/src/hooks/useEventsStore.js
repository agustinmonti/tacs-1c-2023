import { useDispatch, useSelector } from "react-redux"

export const useUiStore = () => {

    const dispatch = useDispatch();

    const { currentEvent } = useSelector( state => state.events );



    return {
        currentEvent,
    }
}