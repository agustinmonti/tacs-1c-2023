import { Link } from 'react-router-dom'
import './navbar.css'

export const Navbar = () => {

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
                            <Link className="nav-link" to={'/login'}>Login</Link>
                        </li>
                        <li className="nav-item">
                            <Link className="nav-link" to={'/usuario/1'}>Perfil</Link>
                        </li>
                    </ul>
                </div>
            </div>
        </nav>
    )
}
