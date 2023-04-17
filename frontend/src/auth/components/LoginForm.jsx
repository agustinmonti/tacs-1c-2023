import { useNavigate, Link } from 'react-router-dom'
import React, { useState } from 'react'

export const LoginForm = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const navigate = useNavigate();

    const handleSubmit = (event) => {
            event.preventDefault();
            fetch('http://localhost:8080/login', {
              method: 'POST',
              headers: {
                'Content-Type': 'application/json'
              },
              body: JSON.stringify({
                email: email,
                password: password
              })
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
               navigate(`/usuarios/${data.id}`)
            })
            .catch(error => {
              console.error('Error:', error);
            });
        };


    return (
        <form className='login-form' onSubmit={handleSubmit}>
            <h3 className='text-center mt-0 pt-0 mb-4 text-white '>Ingresar</h3>
            <input type="email" className='form-control mb-3' placeholder='Correo electrónico' value={email} onChange={(event)=> setEmail(event.target.value)}/>
            <input type="password" className='form-control mb-3' placeholder='Contraseña' value={password} onChange={(event)=> setPassword(event.target.value)} />
            <input type="submit" className='btn btn-primary w-100' />
            <small style={{float:'right',marginTop:'10px'}}>
                ¿No tienes cuenta?
                <Link className="ms-1" to={'/register'}>Registrate aquí </Link>
            </small>
        </form>
    )
}
