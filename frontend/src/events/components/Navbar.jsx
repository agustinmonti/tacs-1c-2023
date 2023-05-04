import { Link } from 'react-router-dom'
import './navbar.css'
import { useAuthStore } from '../../hooks'

export const Navbar = () => {

    const { user, startLogout } = useAuthStore();

    return (
        <nav className="navbar navbar-expand-lg bg-primary" data-bs-theme="dark">
            <div className="container justify-content-between">
                
                <Link className="navbar-brand" to={'/'}>Events-Organizer</Link>
                
                <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                    <span className="navbar-toggler-icon"></span>
                </button>
                <div className="collapse navbar-collapse" id="navbarSupportedContent">
                    <ul className="navbar-nav me-auto mb-2 mb-lg-0">
                        <li className="nav-item">
                            <Link className="nav-link" to={'/'}>Inicio</Link>
                        </li> 
                        {
                            user.isAdmin &&
                            <li className="nav-item">
                                <Link className="nav-link" to={'/monitor'}>Monitor</Link>
                            </li>                    
                        }                   
                    </ul>
                    <button
                        className='btn btn-danger'
                        onClick={ startLogout }
                    >
                        <i className="fas fa-sign-out-alt"></i>
                    </button>
                </div>
            </div>
        </nav>
    )
}
