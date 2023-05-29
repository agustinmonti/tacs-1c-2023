import { Link } from "react-router-dom";
import { EventOptionList } from "./EventOptionList";
import { useEventsStore } from "../../hooks";
import { OwnerActions, UserActions } from "./EventActions";
import { EventUrl } from "./EventUrl";

export const Event = ({ event }) => {

    const { name, description, options = [], owner = {}, participants = [], createdDate } = event;

    const { isOwner } = useEventsStore();

    const hasParticipants = () => {
        return participants.length > 0
    }

    return (
        <>
            <div className="row">
                <div className="col-12 mb-3">
                    <div className="border rounded bg-white shadow-sm p-3 d-flex justify-content-between align-items-center animate__animated animate__fadeIn">
                        <div className="event-header-info">
                            <h3>{ name }</h3>
                            <p className="m-0">
                                Creado por: { owner.email }                      
                            </p>
                            <p className="m-0">
                                Fecha de creación: { createdDate?.toLocaleString() } 
                            </p>
                        </div>
                            <div className="event-actions">
                                {
                                    isOwner
                                    ?
                                    <OwnerActions />
                                    :
                                    <UserActions />
                                }
                            </div>
                    </div>
                </div>
                <div className="col-12 col-lg-8">
                    <div className="border rounded shadow-sm bg-white mb-3 animate__animated animate__fadeIn">
                        <div className="event-body">
                            {
                                description
                                &&
                                <div className="p-3">
                                    <p className="fs-4 fw-semibold mb-2">Descripción:</p>
                                    <p className="m-0">{ description }</p>
                                </div>
                            }
                            <div className="p-3">
                                <p className="fs-4 fw-semibold mb-2">Opciones:</p>
                                <EventOptionList options={ options } />
                            </div>
                        </div>
                    </div>
                    <EventUrl />
                </div>
                <div className="col-12 col-lg-4">
                    <div 
                        style={{transition:'all .3s ease'}}
                        className="border rounded shadow-sm bg-white p-3 mb-3 animate__animated animate__fadeIn"
                        >
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
        </>
    )
}
