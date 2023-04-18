import { useNavigate, useParams  } from 'react-router-dom'
import React, { useState, useEffect } from 'react';

export const EventOptionsPage = () => {
  const [eventOptions, setEventOptions] = useState([]);
  const [votes, setVotes] = useState([]);
  const [user, setUser] = useState();
  const [start, setStart] = useState('');
  const [end, setEnd] = useState('');
  const navigate = useNavigate();
  const { idEvent } = useParams();
  const { idOption } = useParams();
  const [optionStart, setOptionStart] = useState("");
  const [optionEnd, setOptionEnd] = useState("");


  useEffect(() => {
    async function fetchEventOptions() {
      const response = await fetch(`http://localhost:8080/events/${idEvent}/options`);
      const optionsData = await response.json();

      const optionsWithVotes = await Promise.all(
        optionsData.map(async (option) => {
          const votesResponse = await fetch(`http://localhost:8080/events/${idEvent}/options/${option.id}/votes`);
          const votesData = await votesResponse.json();
          return { ...option, votes: votesData.length };
        })
      );

      setEventOptions(optionsWithVotes);
    }

    fetchEventOptions();
  }, [idEvent]);

  const handleDelete = () => {
    fetch(`http://localhost:8080/events/${idEvent}/options`, {
      method: 'DELETE'
    })
      .then(response => {
        if (response.ok) {
          navigate(`/events/${idEvent}/options`);
        } else {
          throw new Error('Error sending request');
        }
      })
      .catch(error => {
        console.error('Error:', error);
      });
  }

    const handleSubmit = (event) => {
      event.preventDefault();
      fetch(`http://localhost:8080/events/${idEvent}/options`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          eventOptionParentId: idEvent,
          start: optionStart,
          end: optionEnd,
        }),
      })
        .then((response) => {
          if (response.ok) {
            navigate(`/events/${idEvent}/options`);
          } else if (response.status === 404) {
            navigate(`/events/${idEvent}/options`);
          } else {
            navigate(`/events/${idEvent}/options`);
            throw new Error('Error sending request');
          }
        })
        .then(() => {
          window.location.reload();
        })
        .catch((error) => {
          console.error('Error:', error);
        });
    };



  if (!eventOptions) {
    return <div>Loading...</div>;
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
                  <td>{eventOption.votes}</td>
                </tr>
              ))}
          </tbody>
        </table>
    <button onClick={() => handleDelete()}>Delete All</button>
    <div>
      <h3>Event:{idEvent}</h3>
      <form onSubmit={handleSubmit}>
        <label>
          Start:
          <input
            type="datetime-local"
            name="optionStart"
            value={optionStart}
            min="2023-04-18T00:00"
            max="2024-04-18T23:59"
            onChange={(event) => setOptionStart(event.target.value)}
          />
        </label>
        <br />
        <label>
          End:
          <input
            type="datetime-local"
            name="optionEnd"
            value={optionEnd}
            min="2023-04-18T00:00"
            max="2024-04-18T23:59"
            onChange={(event) => setOptionEnd(event.target.value)}
          />
        </label>
        <br />
        <button type="submit">Create</button>
      </form>
    </div>
  </div>
);
};

