import { useNavigate, useParams  } from 'react-router-dom'
import React, { useState, useEffect } from 'react';

export const EventOptionsPage = () => {
  const [eventOptions, setEventOptions] = useState([]);
  const [votes, setVotes] = useState([]);
  const [user, setUser] = useState();
  const navigate = useNavigate();
  const { idEvent } = useParams();
  const { idOption } = useParams();
  useEffect(()=>{
        const getEventOptions = async () => {
        const response = await fetch(`http://localhost:8080/events/${idEvent}/options`);
        const data = await response.json();
        console.log("data",data);
        setEventOptions(data);
      };

      getEventOptions();
    }, []);

  if(!eventOptions){
    return <div>Loading...</div>
  }
  useEffect(()=>{
          const getVotes = async () => {
          const response = await fetch(`http://localhost:8080/events/${idEvent}/options/${idOption}/votes`);
          const data = await response.json();
          console.log("data",data);
          setVotes(data);
        };

        getVotes();
      }, []);

    if(!votes){
      return <div>Loading...</div>
    }
  const handleDelete = () => {
          fetch(`http://localhost:8080/events/${idEvent}/options`, {
              method: 'DELETE'
          })
          .then(response => {
              if (response.ok) {
                  navigate(`/events/${idEvent}/options`);
          } else if(response.status == 404) {
              navigate(`/events/${idEvent}/options`);
          } else {
              navigate(`/events/${idEvent}/options`);
              throw new Error('Error sending request');
          }
          })
          .then(data => {
              console.log('Response data:', data);
          })
          .catch(error => {
              console.error('Error:', error);
          });
  }


  return (
    <div>
      <h3>Event:{idEvent}</h3>
      <table>
        <tbody>
          <tr>
            <th>Event Option ID |</th>
            <th>Start DD/MM/YYYY|</th>
            <th>End DD/MM/YYYY|</th>
            <th>Votes Count</th>
          </tr>
          {Array.isArray(eventOptions) &&
            eventOptions.map((eventOption) => (
              <tr key={eventOption.id}>
                <td>{eventOption.id}</td>
                <td>{eventOption.readAbleStart}</td>
                <td>{eventOption.readAbleEnd}</td>
                <td>{votes.length}</td>
              </tr>
            ))}
        </tbody>
      </table>
      <button onClick={() => handleDelete()}>Delete All</button>
    </div>
  );
};

