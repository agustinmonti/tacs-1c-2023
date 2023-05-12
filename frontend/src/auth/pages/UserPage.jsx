import { useNavigate, useParams } from 'react-router-dom'
import React, { useState, useEffect } from 'react';

export const UserPage = () => {
    const [user, setUser] = useState({});
    const { id } = useParams();
    const [username, setUsername] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const navigate = useNavigate();

    const getUser =() => {
      const response = fetch(`http://localhost:8080/v2/users/${id}`,{})
        .then(response => {
          if (response.ok){
            return response.json();
          } else if (response.status === 404) {
            navigate('/users');
          }
          throw new Error('Error retrieving user');
        })
        .then(data => {
          setUser(data);
        })
        .catch(error => {
          console.error('Error:', error);
        });
    };

    useEffect(() => {
        getUser();
    }, [id]);

    useEffect(() => {
        setUsername(user.name);
        setEmail(user.email);
    }, [user]);

    const handleDelete = () => {
        fetch(`http://localhost:8080/users/${id}`, {
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

    const handleSubmit = (event) => {
        event.preventDefault();
        const updatedFields = {};
        if (username !== user.name){
            updatedFields.name = username;
        }
        if (email !== user.email){
            updatedFields.email = email;
        }
        if (password !== '') {
            updatedFields.password = password;
        }
        if (confirmPassword !== '') {
            updatedFields.confirmPassword = confirmPassword;
        }
        fetch(`http://localhost:8080/users/${id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(updatedFields)
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
    };

    return (
    <div>
        <h3>User {id}</h3>
        <form onSubmit={handleSubmit}>
            <label>
                Username:
                <input type="username" value={user ? username : ''} onChange={(event)=> setUsername(event.target.value)} />
                {/*<input type="username" value={username} onChange={(event)=> setUsername(event.target.value)} />*/}
            </label>
            <br />
            <label>
                Email:
                <input type="email" value={user ? email : ''} onChange={(event)=> setEmail(event.target.value)} />
                {/*<input type="email" value={email} onChange={(event)=> setEmail(event.target.value)} />*/}
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
            <button type="submit">Update</button>
            <button onClick={() => handleDelete()}>Delete</button>
        </form>
    </div>
    );

};