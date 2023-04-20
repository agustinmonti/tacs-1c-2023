import { useEffect } from "react";
import { useEventsStore } from "../../hooks"
import { useParams } from "react-router-dom";
import { Navbar } from "../components/Navbar";
import { CreateEventOption } from "../components/CreateEventOption";

export const EventPage = () => {

    const { currentEvent } = useEventsStore();
    const { eventId } = useParams();

    useEffect(() => {
        
        if(!currentEvent){
            //load event by id in params
        }
    
        return () => {
            
        }
    }, [])
    

    return (
        <>
            <Navbar />
            <div className="container" style={{marginTop:'70px'}}>
                <div className="row">
                    <div className="col"></div>
                    <div className="col-12 col-md-8">
                        <div className="border rounded p-3">
                            <h2>{ currentEvent.name }</h2>
                            <p>{ currentEvent.description }</p>
                            <hr />
                            <h4>Opciones:</h4>
                            {
                                currentEvent.options.map( ( option, i ) => (
                                    <CreateEventOption option={option} />
                                ))
                            }
                        </div>
                    </div>
                    <div className="col"></div>
                </div>
            </div>
            
        </>
    )
}
