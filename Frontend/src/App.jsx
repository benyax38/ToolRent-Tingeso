import './App.css'
import { BrowserRouter as Router, Route, Routes, Navigate } from 'react-router-dom'
import { getRole } from './components/auth/auth';

import Login from "./components/auth/Login";
import Register from "./components/auth/Register";
import NotFound from "./components/NotFound/NotFound";
import Home from "./components/home/Home";
import Unauthorized from "./components/home/Unauthorized";
import ToolManagement from './components/options/tools/ToolManagement';

/*
  * Este es el componente raíz de la aplicación.
  * Aquí se define la estructura principal (layout) y se llama a otros componentes/páginas.
  * Se puede entender como la página base en donde se montan las demás pantallas.
*/

const ProtectedRoute = ({ roles, children }) => {
  const role = getRole();

  if (!roles.includes(role)) {
    return <Navigate to="/unauthorized" />;
  }
  return children;
};

console.log("Rol actual:", getRole());

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Navigate to="/login" replace />} />
        <Route path="/login" element={<Login/>} />
        <Route path="/register" element={<Register/>} />
        <Route path="/home" element={<Home />} />
        <Route path="/toolmng" element={<ToolManagement />} />
        <Route path="/unauthorized" element={<Unauthorized />} />
        <Route path="*" element={<NotFound/>} />
      </Routes>
    </Router>
  );
}
/*
<Route path="/tasks" element={
          <ProtectedRoute roles={["EMPLOYEE"]}>
            <Tasks />
          </ProtectedRoute>
        } />
*/
export default App; // Permite exportar App hacia main.jsx

