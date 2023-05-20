import { useState } from "react";

import { useEventsStore } from '../../hooks'
import { CreateEventOptionsForm } from "./CreateEventOptionsForm";
import { CreateEventError } from "./CreateEventError";

export const CreateEventForm = () => {

    const [ formValues, setFormValues] = useState({
        name: '',
        description: '',
        options: [],
        participants: []
    });

    const [state, setState] = useState({
        isLoading: false,
        errorMessage: ''
    });
    const { isLoading, errorMessage } = state;

    const { name, desc, options } = formValues;

    const { startCreatingEvent } = useEventsStore();

    const onChange = ( e ) => {

        setFormValues( state => {
            return {
                ...state,
                [e.target.name] : e.target.value || e.target.checked
            }
        })
    }

    const setErrorMessage = ( message ) => {
        setState( prev => ({
            ...prev,
            errorMessage: message
        }));
    }

    const setIsLoading = ( value ) => {
        setState( prev => ({
            ...prev,
            isLoading: value
        }));
    }

    const isEventValid = () => {
        if( name.length < 1 ){
            setErrorMessage('El evento debe tener un nombre');
            return false;
        }
        if( options.length < 1 ){
            setErrorMessage('El evento debe tener al menos una opción');
            return false;
        }

        return true;
    }

    const handleAddOption = ( newOption ) => {

        if( options.some(op => op === newOption)) return;

        newOption.id = Math.floor(Math.random() * (0 + 99999) + 0);
        newOption.votes = [];

        setFormValues({
            ...formValues,
            options: [...options,newOption]
        })
    }

    const handleRemoveOption = ( id ) => {
        setFormValues({
            ...formValues,
            options: options.filter(op => op.id !== id)
        })
    }

    const onSubmit = async(e) => {
        e.preventDefault();
        setErrorMessage('');
        if( !isEventValid() ) return;

        setIsLoading( true );
        await startCreatingEvent(formValues);
        setIsLoading(false);
    }

    return (
        <form onSubmit={ onSubmit }>
            {
                errorMessage.length > 0
                && <CreateEventError errorMessage={ errorMessage }/>
            }
            <div className="mb-3">
                <label htmlFor="eventName" className="form-label">Nombre <span className="fw-bold text-danger">*</span></label>
                <input 
                    type="text" 
                    className="form-control form-control-sm" 
                    id="name" 
                    value={ name }
                    onChange={ onChange }
                    name="name"
                    required
                />
            </div>
            <div className="mb-3">
                <label htmlFor="eventdesc" className="form-label">Descripción</label>
                <input 
                    type="text" 
                    className="form-control form-control-sm" 
                    id="desc"
                    value={ desc }
                    onChange={ onChange }
                    name="desc"
                />
            </div>

            <CreateEventOptionsForm options = { options } handleAddOption={ handleAddOption } handleRemoveOption={ handleRemoveOption }/>

            <hr />
            <button type="submit" className="btn btn-primary btn-lg w-100" disabled={ isLoading }>
                {
                    isLoading
                    ?
                        <div className="spinner-border mt-1 p-0" role="status">
                            <span className="visually-hidden">Loading...</span>
                        </div>
                    :
                    <span className="d-block my-1">Aceptar</span>
                }
            </button>
        </form>
    )
}
