import { Navigate, Route, Routes } from "react-router-dom";
import { LoginPage, RegisterPage, UsersPage, UserPage, VotesPage, MarketingPage } from "../auth";
import { EventsPage, EventOptionsPage } from "../events";
import { EventPage } from "../events/pages/EventPage";

export const AppRouter = () => {
	const authStatus = "not-authenticated";
    
    if( authStatus === 'checking'){
        return(
            <h3>Loading...</h3>
        )
    }

	return (
		<Routes>
            {
                ( authStatus === "not-authenticated")
                ? (
                    <>
                        <Route path="/login" element={<LoginPage register={false}/>} />
                        <Route path="/register" element={<LoginPage register />} />
                        <Route path="/*" element={<Navigate to={"/login"} />} />
                    </>
                )
                : (
                    <>
                        <Route path="/event/:id" element={ <EventPage /> } />
                        <Route path="/user/:id" element={<UserPage />} />
                        <Route path="/" element={ <EventsPage /> } />
                        <Route path="/*" element={<Navigate to={"/"} />} />
                    </>
                )
            }
		</Routes>
	);
};