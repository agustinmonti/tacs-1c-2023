import React, { useState } from 'react'



export const RegisterPage = () => {
    const [username, setUsername] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');

    const handleSubmit = (event) => {
        event.preventDefault();
        fetch('http://localhost:8080/usuarios', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({
            nombre: username,
            email: email,
            password: password,
            confirmPassword: confirmPassword
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
        })
        .catch(error => {
          console.error('Error:', error);
        });
    };

    return (
        <div>
            <h2>Sign Up</h2>
            <form onSubmit={handleSubmit}>
                <label>
                    Username:
                    <input type="username" value={username} onChange={(event)=> setUsername(event.target.value)} />
                </label>
                <br />
                <label>
                    Email:
                    <input type="email" value={email} onChange={(event)=> setEmail(event.target.value)} />
                </label>
                <br />
                <label>
                    Password:
                    <input type="password" value={password} onChange={(event)=> setPassword(event.target.value)} />
                </label>
                <br />
                <label>
                    Confirm Password:
                    <input type="password" value={confirmPassword} onChange={(event)=> setConfirmPassword(event.target.value)} />
                </label>
                <br />
                <button type="submit">Sign Up</button>
            </form>
        </div>
    );
}