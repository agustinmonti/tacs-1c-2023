import { useDispatch, useSelector } from "react-redux"
import { api } from "../api";
import Swal from "sweetalert2";
import { onLogout } from "../store";

export const useAuthStore = () => {

    const dispatch = useDispatch();


    const { 
        user,
        onLogin
    } = useSelector( state => state.auth );

    const startCreatingUser = async( newUser ) => {

        try {
            const { status, data } = await api.post('/users', newUser); 
            console.log(response)
            
            if( status === 201 ){
                Swal.fire('Registrado', data.msg, 'success');
                return true;
            }else{

            }
        } catch (error) {
            console.log(error);
            Swal.fire('Error en el registro', 'El registro falló exitosamente', 'error');
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
                console.error(data.msg);
            }
        } catch (error) {
            console.log(error);
            Swal.fire('Error', 'El ingreso falló exitosamente', 'error');
        }
    }

    const checkAuthToken = async() => {
        const token = localStorage.getItem('token');

        if( !token ) return dispatch( onLogout() );

        try{

            const { status, data } = await api.get('/auth/renew');

            if( status === 200 ){
                
                localStorage.setItem('token', data.token);    
                dispatch( onLogin( data.user ) );

            }else{
                console.error(data.msg);
                localStorage.clear();
                dispatch( onLogout() );
            }

        }catch(error){
            localStorage.clear();
            dispatch( onLogout() )
        }
    }

    return {
        user,

        startCreatingUser,
        startLogin,
        checkAuthToken
    }
}