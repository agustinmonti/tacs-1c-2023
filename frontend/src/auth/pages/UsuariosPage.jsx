import { useNavigate } from 'react-router-dom'
import React, { useState, useEffect } from 'react';

export const UsuariosPage = () => {
  const [users, setUsers] = useState([]);
  const navigate = useNavigate();

  const getUsers = async () => {
    const response = await fetch('http://localhost:8080/usuarios');
    const data = await response.json();
    setUsers(data);
  };

  useEffect(() => {
    getUsers();
  }, []);

  const handleDelete = () => {
          fetch('http://localhost:8080/usuarios', {
              method: 'DELETE'
          })
          .then(response => {
              if (response.ok) {
                  navigate('/usuarios');
          } else if(response.status == 404) {
              navigate('/usuarios');
          } else {
              navigate('/usuarios');
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
      <h3>Usuarios</h3>
      <table>
        <tbody>
          <tr>
            <th>Id</th>
            <th>Nombre</th>
            <th>Email</th>
          </tr>
          {Array.isArray(users) &&
            users.map((user) => (
              <tr key={user.id}>
                <td>{user.id}</td>
                <td>{user.nombre}</td>
                <td>{user.email}</td>
              </tr>
            ))}
        </tbody>
      </table>
      <button onClick={() => handleDelete()}>Delete All</button>
    </div>
  );
};

