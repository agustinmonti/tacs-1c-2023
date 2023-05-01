import { Link } from "react-router-dom"
import './eventList.css'

export const EventsList = ({ events = [] }) => {

    const statusClass = ( event ) => {
        if( event.status === 'Activo'){
            return 'badge text-bg-primary'
        }

        return 'badge text-bg-dark'
    }

    return (
        <div className="mt-3">
            {
                events.map( ev => (
                    <div key={ ev.id } 
                        className="border rounded mb-2 d-flex justify-content-between align-items-center mw-100"
                        style={{height:'115px'}}
                    >
                        <div className="p-3" style={{maxWidth:'calc(100% - 115px)'}}>
                            <p className="fs-4 m-0 text-nowrap">{ ev.name }</p>
                            <p 
                                className="fs-6 m-0 text-nowrap" 
                                style={{boxSizing:'border-box',textOverflow:'ellipsis', overflow:'hidden'}}
                            >
                                { ev.description }
                            </p>
                            <span className={statusClass(ev)}>{ ev.status }</span>
                            { ev.totalParticipants !== undefined && <span className="badge text-bg-secondary ms-2"><i className="fa-solid fa-user"></i>  {ev.totalParticipants}</span> }
                        </div>
                        <div className="d-flex">
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
