export const CreateEventOption = ({ option, handleRemoveOption }) => {

    const optionId = option.id;

    const onRemove = () => {
        handleRemoveOption(optionId)
    }

    return (
        <div className="d-flex justify-content-between align-items-center border rounded p-2 mb-2  animate__animated animate__fadeIn">

            <p style={{margin:0}}>
                Inicio: { option.start.toLocaleString() }
            </p>
            <p style={{margin:0}}>
                Fin: { option.end.toLocaleString() }
            </p>

            {
                ( handleRemoveOption ) &&
                <button
                    type='button'
                    className='btn btn-danger btn-sm'
                    onClick={ onRemove }
                >
                    X
                </button>

            }
        </div>
    )
}
