import { useState } from "react";
import { useEventsStore } from "../../hooks";

export const EventOption = ({ option }) => {

    const { id, start, end } = option;

    const [isLoading, setIsLoading] = useState(false)

    const { startVoting } = useEventsStore();
    
    const handleVote = async() => {
        setIsLoading(true);
        await startVoting(id);
        setIsLoading(false);
    }
    
    //TODO: useEffect para reconocer si la opción ya fue votada por el usuario
    
    return (
        <div className="border rounded p-2 mb-2 w-100 d-flex justify-content-between">
            <div className="option-info">
                <p style={{margin:0}}>
                    <span className="fw-semibold">Inicio: </span> { start.toLocaleString() }
                </p>
                <p style={{margin:0}}>
                    <span className="fw-semibold">Fin: </span> { end.toLocaleString() }
                </p>
            </div>
            <div className="option-actions">
                <button className="btn btn-success h-100"
                    style={{width: '80px'}}
                    onClick={ handleVote }
                    disabled={ isLoading }
                >
                    {
                        isLoading
                        ?
                            <div className="spinner-border spinner-border-sm mt-1 p-0" role="status">
                                <span className="visually-hidden">Loading...</span>
                            </div>
                        :
                        <span className="d-block my-1">Votar</span>
                    }
                </button>
            </div>
        </div>
    )
}
