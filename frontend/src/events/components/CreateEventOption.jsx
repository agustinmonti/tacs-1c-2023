export const CreateEventOption = ({ option, handleRemoveOption }) => {

    const optionId = option.id;

    const onRemove = () => {
        handleRemoveOption(optionId)
    }

    return (
        <div style={{
            display:'flex',
            justifyContent:'space-between',
            alignItems:'center',
            padding:'5px',
            marginBottom:'5px',
            border: '1px solid #ccc',
            borderRadius:'5px'
        }}>

            <p style={{margin:0}}>
                Inicio: { option.start.toLocaleString() }
            </p>
            <p style={{margin:0}}>
                Fin: { option.end.toLocaleString() }
            </p>

            <button
                type='button'
                className='btn btn-danger btn-sm'
                onClick={ onRemove }
            >
                X
            </button>
        </div>
    )
}
