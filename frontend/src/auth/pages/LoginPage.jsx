import { useEffect } from 'react';
import { LoginForm } from '../components/LoginForm'
import './loginPage.css'

export const LoginPage = () => {


    const test = async() => {
        const res = await fetch('http://localhost:8080/usuarios',{
            method: "GET"
        });
        const data = await res.json();
        console.log(data)
    }

    useEffect(() => {
        test();
    }, [])
    

    return (
        <div className='container-fluid'>
            <div className="row align-items-center">
                <div className="col-12 col-md-7 col-xl-6 p-0">
                    <div className="title-box d-flex flex-column justify-content-center">
                        <div className="p-5">
                            <h1>Event-Organizer</h1>
                            <hr />
                            <p>Publica tus eventos, vota por los horarios que mejor se adaptan a tí, ten una lista de invitados.</p>
                        </div>
                    </div>
                </div>
                <div className="col-12 col-md-5 col-xl-6 p-0">
                    <div className="login-box p-5 d-flex flex-column justify-content-center">
                        <LoginForm />
                    </div>
                </div>
            </div>
        </div>
    )
}
