import React from 'react'

export const CreateEventError = ({ errorMessage }) => {
    return (
        <div className="alert alert-danger animate__animated animate__fadeIn" role="alert">
            <p className='fw-bold p-0 m-0'>{ errorMessage }</p>
        </div>
    )
}
