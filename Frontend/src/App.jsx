import './App.css'
import {BrowserRouter as Router, Route, Routes, Navigate} from 'react-router-dom'
import Login from "./features/auth/Login";
import Register from "./features/auth/Register";
import NotFound from "./components/NotFound/NotFound"

/*
  * Este es el componente raíz de la aplicación.
  * Aquí se define la estructura principal (layout) y se llama a otros componentes/páginas.
  * Se puede entender como la página base en donde se montan las demás pantallas.
*/

function App() {
  return (
    <Router>
      <div className="container">
        <Routes>
          <Route path="/" element={<Navigate to="/login" replace />} />
          <Route path="/login" element={<Login/>} />
          <Route path="/register" element={<Register/>} />
          <Route path="*" element={<NotFound/>} />
        </Routes>
      </div>
    </Router>
  );
}

export default App; // Permite exportar App hacia main.jsx

