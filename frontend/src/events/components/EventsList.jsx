
export const EventsList = ({ events = [] }) => {

    console.log(events)

    return (
        <div>
            {
                events.map( ev => (
                    <div key={ ev.id } className="border rounded p-3 mb-2">
                        <p>{ev.name}</p>
                        <p>{ev.description}</p>
                        <p>{ev.status}</p>
                        <p>{ev.totalParticipants}</p>
                    </div>
                ))
            }
        </div>
        
    )
}
