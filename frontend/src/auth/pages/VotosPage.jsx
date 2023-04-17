import { useNavigate, useParams  } from 'react-router-dom'
import React, { useState, useEffect } from 'react';

export const VotosPage = () => {
  const [votes, setVotes] = useState([]);
  const [user, setUser] = useState();
  const navigate = useNavigate();
  const { idEvento } = useParams();
  const { idOpcion } = useParams();
  useEffect(()=>{
        const getVotes = async () => {
        const response = await fetch(`http://localhost:8080/eventos/${idEvento}/opciones/${idOpcion}/votos`);
        const data = await response.json();
        console.log("data",data);
        setVotes(data);
      };

      getVotes();
    }, []);

  if(!votes){
    return <div>Cargando...</div>
  }
  const handleVotar = () => {
        event.preventDefault();
        fetch(`http://localhost:8080/eventos/${idEvento}/opciones/${idOpcion}/votos`, {
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
          fetch(`http://localhost:8080/eventos/${idEvento}/opciones/${idOpcion}/votos`, {
              method: 'DELETE'
          })
          .then(response => {
              if (response.ok) {
                  navigate(`/eventos/${idEvento}/opciones/${idOpcion}/votos`);
          } else if(response.status == 404) {
              navigate(`/eventos/${idEvento}/opciones/${idOpcion}/votos`);
          } else {
              navigate(`/eventos/${idEvento}/opciones/${idOpcion}/votos`);
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
      <h3>Votos del Evento:{idEvento} Opcion:{idOpcion}</h3>
      <table>
        <tbody>
          <tr>
            <th>Id</th>
            <th>Nombre</th>
            <th>Email</th>
            <th>Fecha</th>
          </tr>
          {Array.isArray(votes) &&
            votes.map((vote) => (
              <tr key={vote.id}>
                <td>{vote.id}</td>
                <td>{vote.user.nombre}</td>
                <td>{vote.user.email}</td>
                <td>{vote.fechaDeVotacion.date.day}/{vote.fechaDeVotacion.date.month}/{vote.fechaDeVotacion.date.year}</td>
              </tr>
            ))}
        </tbody>
      </table>
      <button onClick={() => handleVotar()}>Votar</button>
      <button onClick={() => handleDelete()}>Delete All</button>
    </div>
  );
};

