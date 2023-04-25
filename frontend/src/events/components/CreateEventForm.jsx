import { useState } from "react";

import { useEventsStore } from '../../hooks'
import { CreateEventOptionsForm } from "./CreateEventOptionsForm";

export const CreateEventForm = () => {

    const [ formValues, setFormValues] = useState({
        name: '',
        description: '',
        options: []
    });

    const [isLoading, setIsLoading] = useState(false);

    const { name, description, options } = formValues;

    const { startCreatingEvent } = useEventsStore();

    const onChange = ( e ) => {

        setFormValues( state => {
            return {
                ...state,
                [e.target.name] : e.target.value || e.target.checked
            }
        })
    }

    const handleAddOption = ( newOption ) => {

        if( options.some(op => op === newOption)) return;

        newOption.id = Math.floor(Math.random() * (0 + 99999) + 0);

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
        setIsLoading( true );

        console.log(formValues)
        await startCreatingEvent(formValues);

        setIsLoading(false);
    }

    return (
        <form onSubmit={ onSubmit }>
            <div className="mb-3">
                <label htmlFor="eventName" className="form-label">Nombre</label>
                <input 
                    type="text" 
                    className="form-control form-control-sm" 
                    id="name" 
                    value={ name }
                    onChange={ onChange }
                    name="name"
                />
            </div>
            <div className="mb-3">
                <label htmlFor="eventDescription" className="form-label">Descripción</label>
                <input 
                    type="text" 
                    className="form-control form-control-sm" 
                    id="description"
                    value={ description }
                    onChange={ onChange }
                    name="description"
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
