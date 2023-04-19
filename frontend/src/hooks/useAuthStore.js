import { useDispatch, useSelector } from "react-redux"
import { api } from "../api";
import Swal from "sweetalert2";

export const useAuthStore = () => {

    const dispatch = useDispatch();


    const { 
        user, 
    } = useSelector( state => state.auth );

    const startCreatingUser = async( newUser ) => {

        try {
            const response = await api.post('/users', newUser); 
            console.log(response)
            //const userCreated = response.data.user;

            Swal.fire('Registrado', 'El registro falló sin éxito', 'success');
            return true;
        } catch (error) {
            console.log(error);
            Swal.fire('Error en el registro', 'El registro falló exitosamente', 'error');
        }
    }


    return {
        user,

        startCreatingUser
    }
}