import React, { useEffect } from 'react'
import { Navbar } from '../components/Navbar'
import { CreateEventModal } from '../components/CreateEvent'
import { useUiStore, useAuthStore, useEventsStore } from '../../hooks'
import { EventsList } from '../components/EventsList'
import { Footer } from '../components/Footer'

export const EventsPage = () => {

    const { openCreateEventModal } = useUiStore();
    const { user } = useAuthStore();
    const { myEvents, participantEvents, startGettingEvents } = useEventsStore();

    useEffect(() => {
        startGettingEvents();
    }, [ user ])
    

    return (
        <>
            <Navbar />

            <header style={{paddingTop: '70px'}} className='container'>
                <div className="row">
                    <div className="col-12">
                        <div className="border rounded bg-white shadow-sm p-3 d-flex justify-content-between animate__animated animate__fadeIn">
                            <div className="user-info">
                                <h3>{ user.name } { user.lastname }</h3>
                                <p style={{margin: 0}}>{ user.email }</p>
                            </div>
                            <button 
                                className="btn btn-primary" 
                                type="button"
                                onClick={ openCreateEventModal }
                            >
                                <i className="fa-solid fa-plus fa-xl"></i> Crear
                            </button>
                        </div>
                    </div>
                </div>
            </header>

            <main className='container mt-3'>
                <div className="row">
                    <div className="col-12 col-lg-6">
                        <div className='border rounded mb-3 bg-white shadow-sm p-3 animate__animated animate__fadeIn'>
                            <h3>Mis eventos:</h3>
                            <EventsList events={ myEvents }/>
                        </div>
                    </div>
                    <div className="col-12 col-lg-6">
                        <div className="border rounded mb-3 bg-white shadow-sm p-3 animate__animated animate__fadeIn">
                            <h3>Participando en:</h3>
                            <EventsList events={ participantEvents }/>
                        </div>
                    </div>
                </div>
            </main>

            <Footer />

            <CreateEventModal />
        </>
    )
}
