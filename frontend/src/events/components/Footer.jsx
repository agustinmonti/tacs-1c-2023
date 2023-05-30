import React from 'react'

import './footer.css'

export const Footer = () => {
    return (
        <footer className='bg-primary text-white'>
            <div className="container-fluid">
                <div className="row">
                    <div className="col-12 col-md-6 m-0 d-flex flex-column justify-content-center text-center">
                        <p className='m-0'>Trabajo práctico para la materia de <b>TACS - UTN FRBA - 1er Cuatrimestre 2023 </b></p>
                    </div>
                    <div className="col-12 col-md-6 m-0 text-center">
                        <p className='m-0'>
                            <b>Grupo 2: </b> 
                            <em className='m-0'>
                                Gabito, Bernardo;
                                Ciruzzi, Genaro;
                                Monti, Fernando;
                                Martí, Julia;
                            </em>
                        </p>
                    </div>
                </div>
            </div>
        </footer>    
    )
}
