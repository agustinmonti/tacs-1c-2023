import { CreateEventOption } from './CreateEventOption'
import { EventOptionInput } from './EventOptionInput'

export const CreateEventOptionsForm = ({ options = [], handleAddOption, handleRemoveOption}) => {

    return (
        <>
            <p className='mb-2'>Opciones</p>

            {
                options.map( (option) => (
                    <CreateEventOption 
                        key = { option.id } 
                        option = { option } 
                        handleRemoveOption = { handleRemoveOption }
                    />
                ))
            }

            <EventOptionInput 
                handleAddOption = { handleAddOption } 
            />

        </>
    )
}
