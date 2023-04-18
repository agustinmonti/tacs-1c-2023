import React from 'react'
import { Navbar } from '../components/Navbar'
import { CreateEventModal } from '../'
import { useUiStore } from '../../hooks/useUiStore'

export const EventsPage = () => {

    const { openCreateEventModal } = useUiStore();

    return (
        <>
            <Navbar />
            <header>
                <div className="container">
                    <div className="row">
                        <div className="col-12 d-flex flex-column justify-content-center vh-100">
                            <div className="p-5 mb-4 bg-primary-subtle rounded-3 text-dark">
                                <div className="container-fluid py-5">
                                    <h1 className="display-5 fw-bold">Event-Organizer</h1>
                                    <p className="col-md-8 fs-4">Publica tus eventos, vota por los horarios que mejor se adaptan a tí, ten una lista de invitados.</p>
                                    <button 
                                        className="btn btn-primary btn-lg" 
                                        type="button"
                                        onClick={ openCreateEventModal }
                                    >
                                        Crear un evento
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </header>
            <CreateEventModal />
        </>
    )
}
