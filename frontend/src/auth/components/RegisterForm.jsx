import React, { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom';

import { useForm, useAuthStore } from '../../hooks';

const PASSWORD_MIN_LENGTH = 6;
const NAME_MIN_LENGTH = 1;
const LASTNAME_MIN_LENGTH = 1;

const initialForm = {
    name: '',
    lastname: '',
    email: '',
    password: '',
    confirmPassword: '',
}

export const RegisterForm = ({ setShowRegister }) => {

    const [isLoading, setIsLoading] = useState(false);

    const navigate = useNavigate();

    const { formState, name, lastname, email, password, confirmPassword, onInputChange } = useForm(initialForm);

    const { startCreatingUser } = useAuthStore();

    const passwordsMatch = () => {
        return password === confirmPassword;
    }

    const handleSubmit = async(e) => {
        e.preventDefault();

        if( !passwordsMatch() ){
            return ;
        }

        setIsLoading(true);
        const success = await startCreatingUser( formState );
        if(success) handleGoToLogin();
        setIsLoading(false);
    }

    const handleGoToLogin = () => {
        setShowRegister(false);
        navigate('/login');
    } 

    return (
        <form className='login-form animate__animated animate__fadeIn' onSubmit={handleSubmit}>
            <h3 className='text-center mt-0 pt-0 mb-4 text-white'>Registrarse</h3>
            <div className="container-fluid p-0">
                <div className="row">
                    <div className="col-6">
                        <input 
                            type="Nombre"
                            className='form-control mb-3' 
                            placeholder='Nombre'
                            name='name'
                            minLength={NAME_MIN_LENGTH}
                            value={name}
                            onChange={onInputChange}
                        />
                    </div>
                    <div className="col-6">
                        <input 
                            type="Apellido"
                            className='form-control mb-3' 
                            placeholder='Apellido'
                            name='lastname'
                            minLength={LASTNAME_MIN_LENGTH}
                            value={lastname}
                            onChange={onInputChange}
                        />
                    </div>
                    <div className="col-12">
                        <input 
                            type="email" 
                            className='form-control mb-3' 
                            placeholder='Email'
                            name='email'
                            value={email}
                            onChange={onInputChange}
                        />
                    </div>
                    <div className="col-12">
                        <input 
                            type="password" 
                            className='form-control mb-3' 
                            placeholder='Contraseña'
                            name='password'
                            minLength={PASSWORD_MIN_LENGTH}
                            value={password}
                            onChange={onInputChange}
                        />
                    </div>
                    <div className="col-12">
                        <div className="input-group  mb-3">
                            <input 
                                type="password" 
                                className='form-control'
                                style={{borderRight:'none'}}
                                placeholder='Confirme contraseña'
                                name='confirmPassword'
                                minLength={PASSWORD_MIN_LENGTH}
                                value={confirmPassword}
                                onChange={onInputChange}
                            />
                            <span className="input-group-text" style={{background: '#fff', border:'none'}}>
                                {
                                    password.length > 0 && ( passwordsMatch()
                                    ? <i className="fa-solid fa-check"></i>
                                    : <i className="fa-solid fa-xmark" style={{color:'red'}}></i>)
                                }
                            </span>
                        </div>
                    </div>
                </div>
            </div>
            <button
                className='btn btn-primary w-100'
                type='submit'
                disabled={ isLoading }
            >
                {
                    isLoading
                    ?
                        <div className="spinner-border mt-1 p-0" role="status">
                            <span className="visually-hidden">Loading...</span>
                        </div>
                    :
                    <span className="d-block my-1">Registrarme</span>
                }
            </button>
            <small style={{float:'right',marginTop:'10px', color:'white'}}>
                ¿Ya tienes una cuenta?
                <Link className="ms-1" to={'/login'} onClick={handleGoToLogin}>Ingresa aquí </Link>
            </small>
        </form>
    )
}
