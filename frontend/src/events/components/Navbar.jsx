import { Link } from 'react-router-dom'
import './navbar.css'
import { useAuthStore } from '../../hooks'

export const Navbar = () => {

    const { user, startLogout } = useAuthStore();

    return (
        <nav className="navbar navbar-expand-lg bg-primary" data-bs-theme="dark">
            <div className="container-fluid">
                
                <Link className="navbar-brand" to={'/'}>Events-Organizer</Link>

                <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                    <span className="navbar-toggler-icon"></span>
                </button>

                <div className="collapse navbar-collapse" id="navbarNav">
                    <ul className="navbar-nav">
                        <li className="nav-item">
                            <Link className="nav-link" to={'/user/1'}>Perfil</Link>
                        </li>
                        <li className="nav-item">
                            <button
                                className='nav-link'
                                onClick={ startLogout }
                            >
                                Logout
                            </button>
                        </li>
                    </ul>
                </div>
            </div>
        </nav>
    )
}
