import { useState } from "react";
import { useEventsStore } from "../../../hooks"

export const CloseEventAction = () => {

    const { isActive, startChangingEventStatus } = useEventsStore();
    const [ isLoading, setIsLoading ] = useState(false);

    const handleSubmit = async() => {

        setIsLoading(true);
        await startChangingEventStatus('Closed');
        setIsLoading(false);
    }

    return (
        isActive
        &&
        <button 
            className="btn btn-secondary"
            onClick={ handleSubmit }
        >
            {
                isLoading
                ?
                    <div className="spinner-border mt-1 p-0" role="status">
                        <span className="visually-hidden">Loading...</span>
                    </div>
                :
                'Cerrar votación'
            }
        </button>
    )
}
