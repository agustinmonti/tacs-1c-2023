import { useNavigate, useParams  } from 'react-router-dom'
import React, { useState, useEffect } from 'react';

export const MarketingPage = () => {
    const [data, setData] = useState({})
    
    useEffect(() => {
        fetch('http://localhost:8080/monitoring'            
          )
          .then(response => {
            if (response.ok) {
              return response.json();
            } else {
              throw new Error('Error sending request');
            }
          })
          .then(data => {
             console.log('Response data:', data);
             setData(data);
          })
          .catch(error => {
            console.error('Error:', error);
          });
    }, [])
    return(
        <div className='container-fluid'>
            <div className="row align-items-center">
                <div className="col-12 col-md-7 col-xl-6 p-0">
                    <div className="title-box d-flex flex-column justify-content-center">
                        <div className="p-5">
                            <h1>Events: {data.events}</h1>
                            <h1>Votes: {data.votes}</h1>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}
