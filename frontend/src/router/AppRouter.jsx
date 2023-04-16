import { Navigate, Route, Routes } from "react-router-dom";
import { LoginPage, RegisterPage, UsuariosPage, UsuarioPage } from "../auth";
import { EventsPage } from "../events";

export const AppRouter = () => {
	const authStatus = "not-authenticated";
    
    if( authStatus === 'checking'){
        return(
            <h3>Cargando...</h3>
        )
    }

	return (
		<Routes>
            {
                ( authStatus === "not-authenticated")
                ? (
                    <>
                        <Route path="/login" element={<LoginPage />} />
                        <Route path="/register" element={<RegisterPage />} />
                        <Route path="/" element={ <EventsPage /> } />

                        <Route path="/usuarios" element={<UsuariosPage />} />
                        <Route path="/usuario/:id" element={<UsuarioPage />} />
                        <Route path="/*" element={<Navigate to={"/"} />} />
                    </>
                )
                : (
                    <>
                        <Route path="/" element={ <EventsPage /> } />
                        <Route path="/*" element={<Navigate to={"/"} />} />
                    </>
                )
            }
		</Routes>
	);
};