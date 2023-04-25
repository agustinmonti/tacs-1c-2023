import React, { useEffect } from 'react'
import { Navbar } from '../components/Navbar'
import { CreateEventModal } from '../'
import { useUiStore, useAuthStore, useEventsStore } from '../../hooks'
import { EventsList } from '../components/EventsList'

export const EventsPage = () => {

    const { openCreateEventModal } = useUiStore();
    const { user } = useAuthStore();
    const { events, startGettingEvents } = useEventsStore();

    useEffect(() => {
        startGettingEvents();
    }, [])
    

    return (
        <>
            <Navbar />

            <header style={{marginTop: '70px'}} className='container'>
                <div className="row">
                    <div className="col border rounded p-3 d-flex justify-content-between">
                        <div className="user-info">
                            <h3>{ user.name } { user.lastname }</h3>
                            <p style={{margin: 0}}>{ user.email }</p>
                        </div>
                        <button 
                            className="btn btn-primary" 
                            type="button"
                            onClick={ openCreateEventModal }
                        >
                            Crear un evento
                        </button>
                    </div>
                </div>
            </header>

            <main className='container mt-3'>
                <div className="row">
                    <div className="col border rounded p-3">
                        <h3>Eventos creados</h3>
                        <EventsList events={ events }/>
                    </div>
                </div>
            </main>

            <CreateEventModal />
        </>
    )
}
