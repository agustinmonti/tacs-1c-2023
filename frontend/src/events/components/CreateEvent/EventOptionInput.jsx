import { addHours, differenceInMilliseconds } from 'date-fns';
import es from 'date-fns/locale/es';
import { useState } from 'react';
import DatePicker, { registerLocale } from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
registerLocale('es', es)

const defaultDates = {
    start: new Date(),
    end: addHours( new Date(), 2)
}

export const EventOptionInput = ({ handleAddOption }) => {

    const [dateValues, setDateValues] = useState(defaultDates);

    const { start, end } = dateValues;

    const onDateChange = ( event, changing) => {
        setDateValues({
            ...dateValues,
            [changing]: event
        })
    }

    const handleSubmit = () => {
        if( differenceInMilliseconds( end, start ) > 0 ){
            handleAddOption( dateValues );
        }
    }

    return (
        <>
            <div className="container-fluid border rounded p-2 animate__animated animate__fadeIn">
                <div className="row">
                    <div className="col-11">
                        <div className="container-fluid p-0">
                            <div className="row">
                                <div className="col-6">
                                    <label className="form-label mb-0">Inicio:</label>
                                    <DatePicker 
                                        minDate={ defaultDates.start }
                                        selected={ start }
                                        onChange={ (event) => onDateChange( event, 'start') }
                                        className="form-control form-control-sm"
                                        dateFormat="Pp"
                                        showTimeSelect
                                        locale="es"
                                        timeCaption="Hora"
                                    />
                                </div>
                                <div className="col-6">
                                    <label className="form-label mb-0">Fin:</label>
                                    <DatePicker 
                                        minDate={ start }
                                        selected={ end }
                                        onChange={ (event) => onDateChange( event, 'end') }
                                        className="form-control form-control-sm"
                                        dateFormat="Pp"
                                        showTimeSelect
                                        locale="es"
                                        timeCaption="Hora"
                                    />
                                </div>
                            </div>
                        </div>
                    </div>
                    
                    <div className="col-1 d-flex justify-content-end align-items-center">
                        <button 
                            type='button' 
                            className='btn btn-primary btn-sm'
                            onClick={ handleSubmit }
                        >
                            <i className="fa-solid fa-plus"></i>
                        </button>
                    </div>
                </div>
            </div>

        </>
    )
}
