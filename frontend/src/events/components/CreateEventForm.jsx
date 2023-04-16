import { useState } from "react";

const defaultEvent = {
    name: '',
    description: '',
    isPublic: false
}

export const CreateEventForm = () => {

    const [ formValues, setFormValues] = useState( defaultEvent );

    const { name, description, isPublic } = formValues;

    const onChange = ( e ) => {
        setFormValues( state => {
            return {
                ...state,
                [e.target.name] : e.target.value
            }
        })
    }

    const onSubmit = async(e) => {
        e.preventDefault();

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
                    className="form-control" 
                    id="eventName" 
                    value={ name }
                    onChange={ onChange }
                    name="name"
                />
            </div>
            <div className="mb-3">
                <label htmlFor="eventDescription" className="form-label">Descripción</label>
                <input 
                    type="text" 
                    className="form-control" 
                    id="eventDescription"
                    value={ description }
                    onChange={ onChange }
                    name="description"
                />
            </div>
            <div className="mb-3 form-check">
                <input 
                    type="checkbox" 
                    className="form-check-input" 
                    id="eventIsPublic"
                    value={ isPublic }
                    onChange={ onChange }
                    name="isPublic"
                />
                <label className="form-check-label" htmlFor="eventIsPublic">¿Evento público?</label>
            </div>
            <button type="submit" className="btn btn-primary w-100">Submit</button>
        </form>
    )
}
