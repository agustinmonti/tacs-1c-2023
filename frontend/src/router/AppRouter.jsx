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
                        <Route path="/login" element={<LoginPage />} />
                        <Route path="/register" element={<RegisterPage />} />
                        <Route path="/event/:id" element={ <EventPage /> } />
                        <Route path="/" element={ <EventsPage /> } />

                        <Route path="/users" element={<UsersPage />} />
                        <Route path="/users/:id" element={<UserPage />} />
                        <Route path="/events/:idEvent/options/:idOption/votes" element={<VotesPage />}/>
                        <Route path="/events/:idEvent/options" element={<EventOptionsPage />}/>
                        <Route path="/monitoring" element={<MarketingPage />}/>
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