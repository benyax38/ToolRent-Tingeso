import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.jsx'

/*
  * Es el punto de entrada de la aplicación.
  * Aquí React toma el App y lo inyecta en el DOM
  * Generalmente lo inyecta en el div con id root del index.html
  * Es como el arranque de motor.
*/

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <App />
  </StrictMode>,
)
