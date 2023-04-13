import { Provider } from 'react-redux'
import { BrowserRouter } from 'react-router-dom'
import { AppRouter } from './router'
import { store } from './store'

const App = () => {
  
    return (
        <Provider store={ store }>
            <BrowserRouter>
                <AppRouter>

                </AppRouter>
            </BrowserRouter>
        </Provider>
    )
}

export default App
