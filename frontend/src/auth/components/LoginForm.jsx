import { Link } from "react-router-dom"

export const LoginForm = () => {
    return (
        <form className='login-form'>
            <h3 className='text-center mt-0 pt-0 mb-4 text-white '>Ingresar</h3>
            <input type="text" className='form-control mb-3' placeholder='Correo electrónico'/>
            <input type="password" className='form-control mb-3' placeholder='Contraseña'/>
            <input type="submit" className='btn btn-primary w-100' />
            <small style={{float:'right',marginTop:'10px'}}>
                ¿No tienes cuenta?
                <Link className="ms-1" to={'/register'}>Registrate aquí </Link>
            </small>
        </form>
    )
}
