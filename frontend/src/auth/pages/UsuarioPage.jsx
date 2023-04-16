import { useParams } from 'react-router-dom'
import React, { useState, useEffect } from 'react';

export const UsuarioPage = () => {
  const [user, setUser] = useState({});
  const { id } = useParams();

  const getUser = async () => {
    const response = await fetch(`http://localhost:8080/usuario/${id}`);
    const data = await response.json();
    setUser(data);
  };

  useEffect(() => {
    getUser();
  }, [id]);

  return (
      <div>
        <h3>Usuario {id}</h3>
        <p>Nombre: {user.nombre}</p>
        <p>Email: {user.email}</p>
      </div>
    );
};


