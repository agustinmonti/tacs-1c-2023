import { Link } from 'react-router-dom'
import './navbar.css'
import { useAuthStore } from '../../hooks'

export const Navbar = () => {

    const { user, startLogout } = useAuthStore();

    return (
        <nav className="navbar bg-primary" data-bs-theme="dark">
            <div className="container-fluid justify-content-between">
                
                <Link className="navbar-brand" to={'/'}>Events-Organizer</Link>
                
                <button
                    className='btn btn-danger'
                    onClick={ startLogout }
                >
                    <i className="fas fa-sign-out-alt"></i>
                </button>

            </div>
        </nav>
    )
}
