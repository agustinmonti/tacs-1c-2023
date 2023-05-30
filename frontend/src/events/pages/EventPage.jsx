import { useEffect } from "react";
import { useEventsStore } from "../../hooks"
import { useParams } from "react-router-dom";
import { Navbar } from "../components/Navbar";
import { Event } from "../components/Event";
import { Footer } from "../components/Footer";

export const EventPage = () => {

    const { currentEvent, startGettingEvent } = useEventsStore();
    const { id: eventId } = useParams();

    useEffect(() => {
        
        startGettingEvent( eventId );
    
    }, [ eventId ])
    

    return (
        <>
            <Navbar />
            <div className="container" style={{paddingTop:'70px'}}>
                <Event event={ currentEvent } />
            </div>
            <Footer />
        </>
    )
}
