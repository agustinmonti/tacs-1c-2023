import { CreateEventOption } from './CreateEventOption'
import { EventOptionInput } from './EventOptionInput'

export const CreateEventOptionsForm = ({ options = [], handleAddOption, handleRemoveOption}) => {

    return (
        <>
            <h4>Opciones</h4>

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
