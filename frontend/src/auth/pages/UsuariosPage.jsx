import React, { useState, useEffect } from 'react';

export const UsuariosPage = () => {
  const [users, setUsers] = useState([]);

  const getUsers = async () => {
    const response = await fetch('http://localhost:8080/usuarios');
    const data = await response.json();
    setUsers(data);
  };

  useEffect(() => {
    getUsers();
  }, []);

  return (
    <div>
      <h3>Usuarios</h3>
      <table>
        <tbody>
          <tr>
            <th>Nombre</th>
            <th>Email</th>
          </tr>
          {Array.isArray(users) &&
            users.map((user) => (
              <tr key={user.id}>
                <td>{user.nombre}</td>
                <td>{user.email}</td>
              </tr>
            ))}
        </tbody>
      </table>
    </div>
  );
};

