import { EventOption } from "./EventOption";
import { EventOptionList } from "./EventOptionList";

export const Event = ({ event }) => {

    const { name, description, options = [], isPublic } = event;

    return (
        <div className="border rounded">
            <div className="event-header p-3 border-bottom">
                <h3>{ name }</h3>
            </div>
            <div className="event-body p-3">
                <div className="p-3 mb-3">
                    <p className="fs-4 fw-semibold mb-2">Descripción:</p>
                    <p className="m-0">{ description }</p>
                </div>
                <div className="p-3">
                    <p className="fs-4 fw-semibold mb-2">Opciones:</p>
                    <EventOptionList options={ options } />
                </div>
            </div>
        </div>
    )
}
