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
            <div className="container-fluid p-0">
                <div className="row">
                    <div className="col-6">
                        <div className="mb-3">
                            <label className="form-label">Inicio</label>
                            <DatePicker 
                                selected={ start }
                                onChange={ (event) => onDateChange( event, 'start') }
                                className="form-control form-control-sm"
                                dateFormat="Pp"
                                showTimeSelect
                                locale="es"
                                timeCaption="Hora"
                            />
                        </div>
                    </div>
                    <div className="col-6">
                        <div className="mb-3">
                            <label className="form-label">Fin</label>
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

            <button 
                type='button' 
                className='btn btn-primary btn-sm w-100'
                onClick={ handleSubmit }
            >
                Agregar opción
            </button>
        </>
    )
}
