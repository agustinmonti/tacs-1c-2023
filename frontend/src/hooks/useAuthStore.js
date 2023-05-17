import { useDispatch, useSelector } from "react-redux"
import { api } from "../api";
import Swal from "sweetalert2";
import { onLogout, onLogin, onLogoutEvents } from "../store";

export const useAuthStore = () => {

    const dispatch = useDispatch();


    const { 
        user,
        status
    } = useSelector( state => state.auth );

    const startCreatingUser = async( newUser ) => {

        try {
            const { status, data } = await api.post('/users', newUser); 
            
            if( status === 201 ){
                Swal.fire('Registrado', data.msg, 'success');
                return true;
            }else{
                Swal.fire('Error en el registro', data.msg, 'error');
            }
        } catch (error) {
            console.log(error);
            Swal.fire('Error en el registro', error.response.data.msg, 'error');
        }
    }

    const startLogin = async( user ) => {
        try {
            const { status, data } = await api.post('/auth/login', user);

            if( status === 200 ){
                localStorage.setItem('token', data.token);
                dispatch(onLogin(data.user));
                return true;
            }else{
                Swal.fire('Error', data.msg, 'error');
            }
        } catch (error) {
            Swal.fire('Error', error.response.data.msg, 'error');
        }
    }

    const checkAuthToken = async() => {
        const token = localStorage.getItem('token');

        if( !token ) return dispatch( onLogout() );

        try{

            const { status, data } = await api.post('/auth/renew');

            if( status === 200 ){

                localStorage.setItem('token', data.token);    
                dispatch( onLogin( data.user ) );

            }else{
                console.error(data.msg);
                localStorage.clear();
                dispatch( onLogout() );
            }

        }catch(error){
            console.log(error);
            localStorage.clear();
            dispatch( onLogout() )
        }
    }

    const startLogout = () => {
        localStorage.clear();
        dispatch(onLogoutEvents());
        dispatch(onLogout());
    }

    return {
        user,
        status,

        startCreatingUser,
        startLogin,
        checkAuthToken,
        startLogout
    }
}