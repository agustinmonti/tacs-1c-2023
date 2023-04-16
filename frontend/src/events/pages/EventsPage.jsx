import React from 'react'
import { Navbar } from '../components/Navbar'
import { CreateEventForm } from '../components/CreateEventForm'

export const EventsPage = () => {
    return (
        <>
            <Navbar />
            <div className="container">
                <div className="row">
                    <div className="col"></div>
                    <div className="col-12 col-md-6">
                        <CreateEventForm />
                    </div>
                    <div className="col"></div>
                </div>
            </div>
        </>
    )
}
