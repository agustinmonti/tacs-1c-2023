import { useMemo, useState } from "react";
import { useEventsStore } from "../../hooks";

export const EventOption = ({ option }) => {

    const { id, start, end, selected, votes } = option;

    const [isLoading, setIsLoading] = useState(false)

    const { isActive, startVoting } = useEventsStore();
    
    const handleVote = async() => {
        setIsLoading(true);
        await startVoting(id);
        setIsLoading(false);
    }

    const buttonInfo = useMemo(() => {
        if( selected ){
            return {
                className: 'btn btn-success',
                icon: <i className="fa-solid fa-check"></i>
            }
        }else{
            return {
                className: 'btn btn-secondary',
                icon: <i className="fa-solid fa-plus"></i>
            }
        }
    }, [selected])
        
    return (
        <div className="border rounded p-2 mb-2 w-100 d-flex justify-content-between align-items-center">
            <div className="option-info">
                <p style={{margin:0}}>
                    <span className="fw-semibold">Inicio: </span> { start.toLocaleString() }
                </p>
                <p style={{margin:0}}>
                    <span className="fw-semibold">Fin: </span> { end.toLocaleString() }
                </p>
            </div>
            <div className="option-actions d-flex">
                <div className="border rounded px-3 mx-2">
                    <span className="fw-bold" style={{fontSize:'30px', width:'40px'}}>{ votes }</span>
                </div>

                <button className={ buttonInfo.className }
                    style={{width: '40px'}}
                    onClick={ handleVote }
                    disabled={ isLoading || !isActive }
                >
                    {
                        isLoading
                        ?
                            <div className="spinner-border spinner-border-sm mt-1 p-0" role="status">
                                <span className="visually-hidden">Loading...</span>
                            </div>
                        :
                        buttonInfo.icon
                    }
                </button>
            </div>
        </div>
    )
}
