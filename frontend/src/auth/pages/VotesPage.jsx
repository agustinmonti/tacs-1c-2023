import { useNavigate, useParams  } from 'react-router-dom'
import React, { useState, useEffect } from 'react';

export const VotesPage = () => {
  const [votes, setVotes] = useState([]);
  const [user, setUser] = useState();
  const navigate = useNavigate();
  const { idEvent } = useParams();
  const { idOption } = useParams();
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
  const handleVoting = () => {
        event.preventDefault();
        fetch(`http://localhost:8080/events/${idEvent}/options/${idOption}/votes`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
        })
        .then(response => {
             if (response.ok) {
                 return response.json();
             } else {
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

  const handleDelete = () => {
          fetch(`http://localhost:8080/events/${idEvent}/options/${idOption}/votes`, {
              method: 'DELETE'
          })
          .then(response => {
              if (response.ok) {
                  navigate(`/events/${idEvent}/options/${idOption}/votes`);
          } else if(response.status == 404) {
              navigate(`/events/${idEvent}/options/${idOption}/votes`);
          } else {
              navigate(`/events/${idEvent}/options/${idOption}/votes`);
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
      <h3>Event:{idEvent} Option:{idOption} votes:</h3>
      <table>
        <tbody>
          <tr>
            <th>Vote Id |</th>
            <th>User Id |</th>
            <th>Name |</th>
            <th>Email |</th>
            <th>Date DD/MM/YYYY</th>
          </tr>
          {Array.isArray(votes) &&
            votes.map((vote) => (
              <tr key={vote.id}>
                <td>{vote.id}</td>
                <td>{vote.user.id}</td>
                <td>{vote.user.name}</td>
                <td>{vote.user.email}</td>
                <td>{vote.votingDate.date.day}/{vote.votingDate.date.month}/{vote.votingDate.date.year}</td>
              </tr>
            ))}
        </tbody>
      </table>
      <button onClick={() => handleVoting()}>Votar</button>
      <button onClick={() => handleDelete()}>Delete All</button>
    </div>
  );
};

