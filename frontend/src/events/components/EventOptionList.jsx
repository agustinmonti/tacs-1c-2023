import { useEffect, useState } from 'react'
import { EventOption } from './EventOption'

export const EventOptionList = ({ options = [] }) => {
    
    return (
        <>
            {
                options.map( (option) => (
                    <EventOption key={option.id} option={ option } />
                ))
            }
        </>
    )
}
