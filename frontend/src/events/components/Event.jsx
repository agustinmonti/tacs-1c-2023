import { Link } from "react-router-dom";
import { EventOptionList } from "./EventOptionList";
import { useEventsStore } from "../../hooks";

export const Event = ({ event }) => {

    const { name, description, options = [], owner = {}, participants = [], createdDate } = event;

    const { showActions } = useEventsStore();

    const hasParticipants = () => {
        return participants.length > 0
    }

    return (
        <div className="row">
            <div className="col-12 mb-3">
                <div className="border rounded p-3 d-flex justify-content-between align-items-center">
                    <div className="event-header-info">
                        <h3>{ name }</h3>
                        <p className="m-0">
                            Creado por: 
                            { <Link to={`/user/${ owner.id }`}> { owner.email } </Link> }                       
                        </p>
                        <p className="m-0">
                            Fecha de creación: { createdDate?.toLocaleString() } 
                        </p>
                    </div>
                    {
                        showActions() && 
                        <div className="event-actions">
                            <div className="btn-group">
                                <button className="btn btn-secondary">
                                    Cerrar votación
                                </button>
                                <button className="btn btn-danger">
                                    Eliminar
                                </button>
                            </div>
                        </div>
                    }
                </div>
            </div>
            <div className="col-12 col-lg-8">
                <div className="border rounded mb-3">
                    <div className="event-body p-3">
                        <div className="p-3">
                            <p className="fs-4 fw-semibold mb-2">Descripción:</p>
                            <p className="m-0">{ description }</p>
                        </div>
                        <div className="p-3">
                            <p className="fs-4 fw-semibold mb-2">Opciones:</p>
                            <EventOptionList options={ options } />
                        </div>
                        <div className="text-end p-3">
                            <button className="btn btn-primary">
                                Ver resultados
                            </button>
                        </div>
                    </div>
                </div>
            </div>
            <div className="col-12 col-lg-4">
                <div className="border rounded p-3 mb-3">
                    <h3>Participantes ({ participants.length }) </h3>
                    <ul className="list-group list-group-flush">
                        {
                            hasParticipants()
                            ? participants.map( participant => (
                                <li key={ participant.id } className="list-group-item">{ participant.email }</li>
                            ))
                            : <p>No hay participantes</p>
                        }
                    </ul>
                </div>
            </div>
        </div>
    )
}
