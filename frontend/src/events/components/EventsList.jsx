import { Link } from "react-router-dom"
import './eventList.css'

export const EventsList = ({ events = [] }) => {

    console.log(events)

    const statusClass = ( event ) => {
        if( event.status === 'Activo'){
            return 'badge text-bg-info'
        }

        return 'badge text-bg-secondary'
    }

    const descriptionText = ( event ) => {
        if( event.description.length > 75 ){
            return event.description.slice(0,50) + '...';
        }

        return event.description;
    }

    return (
        <div>
            {
                events.map( ev => (
                    <div key={ ev.id } 
                        className="border rounded mb-2 d-flex justify-content-between align-items-center"
                        style={{height:'115px'}}
                    >
                        <div className="p-3">
                            <p className="fs-4 m-0">{ev.name}</p>
                            <p className="fs-6 m-0">{descriptionText(ev)}</p>
                            <span className={statusClass(ev)}>{ ev.status }</span>
                        </div>
                        <div className="d-flex">
                            <button 
                                className="btn btn-success px-4 rounded-0"
                            >
                                <i className="fa-solid fa-user"></i> 
                                <br />
                                {ev.totalParticipants}
                            </button>
                            <Link 
                                to={`/event/${ev.id}`} 
                                style={{height:'115px',borderStartStartRadius:0,borderEndStartRadius:0}}
                                className="btn btn-primary px-4 d-flex align-items-center"
                            >
                                <i className="fa-solid fa-eye"></i>
                            </Link>
                        </div>
                    </div>
                ))
            }
        </div>
        
    )
}
