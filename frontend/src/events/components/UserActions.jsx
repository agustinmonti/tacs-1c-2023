import React, { useMemo, useState } from 'react'
import { useEventsStore } from '../../hooks'

export const UserActions = () => {

    const { isParticipating, startToggleSignUpForCurrentEvent } = useEventsStore();

    const [isLoading, setIsLoading] = useState( false );

    const handleParticipate = async () => {
        setIsLoading( true );
        await startToggleSignUpForCurrentEvent();
        setIsLoading( false );
    }

    const {icon, text, className} = useMemo( () => {
        if( isParticipating ){
            return {
                icon: <i className="fa-solid fa-user-check"></i>,
                text: 'Participando',
                className: 'btn btn-primary'
            }
        }{
            return {
                icon: <i className="fa-solid fa-user-plus"></i>,
                text: 'Participar',
                className: 'btn btn-outline-primary'
            }
        }
    }, [ isParticipating ]);

    return (
        <button 
            type="button"
            style={{width:'140px'}}
            className={ className }
            onClick={ handleParticipate }
            disabled= { isLoading }
        >
            {
                !isLoading
                ?
                    <> {icon} {text} </>
                :
                    <div className="spinner-border spinner-border-sm p-0" role="status">
                        <span className="visually-hidden">Loading...</span>
                    </div>
                }
        </button>
    )
}
