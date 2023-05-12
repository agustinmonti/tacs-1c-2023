import { Navigate, Route, Routes } from "react-router-dom";
import { LoginPage, RegisterPage, UsersPage, UserPage, VotesPage, MarketingPage } from "../auth";
import { EventsPage, EventOptionsPage } from "../events";
import { EventPage } from "../events/pages/EventPage";
import { useAuthStore, useUiStore } from "../hooks";
import { useEffect } from "react";
import { MonitorPage } from "../events/pages/MonitorPage";

export const AppRouter = () => {

    const { status, user, checkAuthToken } = useAuthStore();
    const { isLoadingUI } = useUiStore();

    useEffect(() => {
        //checkAuthToken();
    }, []);
    
    if( status === 'checking' || isLoadingUI ){
        return (
            <div className="vh-100 w-100 d-flex justify-content-center align-items-center">
                <div className="spinner-border" style={{width: '3rem',height: '3rem'}} role="status">
                    <span className="visually-hidden">Loading...</span>
                </div>
            </div>
        )
    }

	return (
		<Routes>
            {
                ( status === "not-authenticated")
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
                        {
                            user.isAdmin &&
                            <Route path="/monitor" element={ <MonitorPage /> } />
                        }
                        <Route path="/*" element={<Navigate to={"/"} />} />
                    </>
                )
            }
		</Routes>
	);
};