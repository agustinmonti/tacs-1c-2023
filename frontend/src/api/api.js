import axios from 'axios';
import { getEnvVariables } from '../helpers';

const { VITE_API_URL } = getEnvVariables();

const api = axios.create({
    baseURL: VITE_API_URL
});


/*

configure JWT 

api.interceptors.request.use( config => {
    
    config.headers = {
        ...config.headers,
        'x-token': localStorage.getItem('token'),
    }
    
    return config;
})
*/
export default api;