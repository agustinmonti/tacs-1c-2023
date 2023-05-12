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
            
            /*
            const status = 201;
            const data = { msg: 'Registrado correctamente'}
            */
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
            console.log(data);
            /*
            const { status, data } = {
                status: 200,
                data: { user: { id: 2, name: 'Carlos', lastname: 'Alberto', email: 'carlos@alberto.com', isAdmin: true }, token: '123abc' }
            }*/

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