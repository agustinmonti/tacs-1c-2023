import { useNavigate, Link } from 'react-router-dom'
import { useState } from 'react'
import { useAuthStore, useForm } from '../../hooks'

const initialForm = {
    email: '',
    password: ''
}

export const LoginForm = ({ setShowRegister }) => {

    const { formState, email, password, onInputChange } = useForm(initialForm);

    const { startLogin } = useAuthStore();

    const [isLoading, setIsLoading] = useState(false);

    const navigate = useNavigate();

    const handleGoToRegister = () => {
        setShowRegister(true);
        navigate('/register');
    }

    const handleSubmit = async(e) => {

        e.preventDefault();
        setIsLoading(true);

        const success = await startLogin( formState );
        setIsLoading(false);

        if(success) navigate('/');
    };

    return (
        <form className='login-form animate__animated animate__fadeIn' onSubmit={handleSubmit}>
            <h3 className='text-center mt-0 pt-0 mb-4 text-white'>Ingresar</h3>
            <div className="container-fluid p-0">
                <div className="row">
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
                            value={password}
                            onChange={onInputChange}
                        />
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
                    <span className="d-block my-1">Ingresar</span>
                }
            </button>
            <small style={{float:'right',marginTop:'10px', color:'white'}}>
                ¿No tienes cuenta cuenta?
                <Link className="ms-1" to={'/register'} onClick={handleGoToRegister}>Registrate aquí </Link>
            </small>
        </form>
    )
}
