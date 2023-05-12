import { useEffect, useState } from "react";
import { useMonitor } from "../../hooks/useMonitor"
import { Navbar } from "../components/Navbar"

export const MonitorPage = () => {

    const [monitorValues, setMonitorValues] = useState({});
    const { events, votes } = monitorValues;

    const [isLoading, setIsLoading] = useState( true );

    const { getData } = useMonitor();

    const handleReload = () => {
        setIsLoading(true);
        getData().then((data)=> {
            setMonitorValues( data )
        }).finally( () => {
            setIsLoading(false);
        });
    }
    
    useEffect(() => {
        setIsLoading(true);
        getData().then((data)=> {
            setMonitorValues( data )
        }).finally( () => {
            setIsLoading( false );
        });
    }, [])
    

    return (
        <>
            <Navbar />

            <div style={{marginTop:'70px'}} className="container">
                <div className="row">
                    <div className="col-12">
                        <div className="border rounded shadow-sm bg-white p-3 animate__animated animate__fadeIn d-flex justify-content-between mb-3">
                            <h2>Monitor</h2>
                            <button className="btn btn-primary"
                                onClick={ handleReload }
                            >
                                <i className="fa-solid fa-rotate-right"></i>
                            </button>
                        </div>
                    </div>
                    <div className="col-12 col-md-6">
                        <div className="border rounded shadow-sm bg-white p-3 animate__animated animate__fadeIn mb-3 text-center">
                            <h3 className="text-center">Eventos creados las ultimas 2hs</h3>
                            {
                                isLoading
                                ?   
                                    <div className="spinner-border mt-3" style={{width: '3rem', height: '3rem'}} role="status">
                                        <span className="visually-hidden">Loading...</span>
                                    </div>
                                :
                                    <p className="fs-2 fw-bold">{events}</p>
                            }
                        </div>
                    </div>
                    <div className="col-12 col-md-6">
                        <div className="border rounded shadow-sm bg-white p-3 animate__animated animate__fadeIn mb-3 text-center">
                            <h3 className="text-center">Votos emitidos en las ultimas 2hs</h3>
                            
                            {
                                isLoading
                                ?   
                                    <div className="spinner-border mt-3" style={{width: '3rem', height: '3rem'}} role="status">
                                        <span className="visually-hidden">Loading...</span>
                                    </div>
                                :
                                    <p className="fs-2 fw-bold">{votes}</p>
                            }
                        </div>
                    </div>
                </div>
            </div>

        </>
    )
}
