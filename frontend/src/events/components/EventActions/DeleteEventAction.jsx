import { useState } from "react";
import { useEventsStore } from "../../../hooks";
import Swal from "sweetalert2";

export const DeleteEventAction = () => {
    
    const { startDeletingEvent } = useEventsStore();
    const [ isLoading, setIsLoading ] = useState(false);

    const handleSubmit = async() => {

        setIsLoading(true);

        Swal.fire({
            title: 'Seguro?',
            text: "El evento no se podrá recuperar",
            icon: 'warning',
            showCancelButton: true,
            confirmButtonText: 'Eliminar',
            cancelButtonText: 'Cancelar',
            buttonsStyling: false,
            customClass:{
                cancelButton:'btn btn-secondary',
                confirmButton:'btn btn-danger me-3'
            }}).then(async(result) => {
                if (result.isConfirmed) {
                    await startDeletingEvent();
                }
            })

        setIsLoading(false);
    }

    return (
        <button 
            className="btn btn-danger"
            onClick={ handleSubmit }
            disabled={ isLoading }
        >
            {
                isLoading
                ?
                    <div className="spinner-border mt-1 p-0" role="status">
                        <span className="visually-hidden">Loading...</span>
                    </div>
                :
                'Eliminar'
            }
        </button>
    )
}
