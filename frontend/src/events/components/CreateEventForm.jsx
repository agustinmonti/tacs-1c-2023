import { useState } from "react";

import { CreateEventOptionsForm } from "./CreateEventOptionsForm";

export const CreateEventForm = () => {

    const [ formValues, setFormValues] = useState({
        name: '',
        description: '',
        isPublic: false,
        options: []
    });

    const { name, description, isPublic, options } = formValues;

    const onChange = ( e ) => {
        console.log(e.target)
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

        console.log(formValues)

        try {
            const res = await fetch('http://localhost:8080/eventos', {
                method: 'POST',
                headers: {
                  'Content-Type': 'application/json'
                },
                body: JSON.stringify( formValues )
            });
            
            const data = res.json();
            console.log(data)

        } catch (error) {
            console.log(error);
        }

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

            <div className="mb-3 form-check">
                <input 
                    type="checkbox" 
                    className="form-check-input" 
                    id="isPublic"
                    checked={ isPublic }
                    onChange={ (e) => { setFormValues({...formValues, isPublic: e.target.checked}) } }
                    name="isPublic"
                />
                <label className="form-check-label" htmlFor="isPublic">¿Evento público?</label>
            </div>
            <button type="submit" className="btn btn-primary btn-lg w-100">Aceptar</button>
        </form>
    )
}
