import { useNavigate } from 'react-router-dom'
import React, { useState, useEffect } from 'react';

export const UsersPage = () => {
  const [users, setUsers] = useState([]);
  const navigate = useNavigate();

  const getUsers = async () => {
    const response = await fetch('http://localhost:8080/users');
    const data = await response.json();
    setUsers(data);
  };

  useEffect(() => {
    getUsers();
  }, []);

  const handleDelete = () => {
          fetch('http://localhost:8080/users', {
              method: 'DELETE'
          })
          .then(response => {
              if (response.ok) {
                  navigate('/users');
          } else if(response.status == 404) {
              navigate('/users');
          } else {
              navigate('/users');
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
      <h3>Users</h3>
      <table>
        <tbody>
          <tr>
            <th>Id</th>
            <th>Name</th>
            <th>Email</th>
          </tr>
          {Array.isArray(users) &&
            users.map((user) => (
              <tr key={user.id}>
                <td>{user.id}</td>
                <td>{user.name}</td>
                <td>{user.email}</td>
              </tr>
            ))}
        </tbody>
      </table>
      <button onClick={() => handleDelete()}>Delete All</button>
    </div>
  );
};

