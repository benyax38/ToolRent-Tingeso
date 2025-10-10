import './App.css'
import { BrowserRouter as Router, Route, Routes, Navigate } from 'react-router-dom'
import { getRole } from './utils/auth';

import Dashboard from "./components/pruebas/Dashboard";
import UserManagent from "./components/pruebas/UserManagement";
import Tasks from "./components/pruebas/Tasks";
import Unauthorized from "./features/home/Unauthorized";
import Login from "./features/auth/Login";
import Register from "./features/auth/Register";
import NotFound from "./components/NotFound/NotFound";
import Home from "./features/home/Home";

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
        <Route path="/dashboard" element={
          <ProtectedRoute roles={["ADMIN", "EMPLOYEE"]}>
            <Dashboard />
          </ProtectedRoute>
        } />
        <Route path="/users" element={
          <ProtectedRoute roles={["ADMIN"]}>
            <UserManagent />
          </ProtectedRoute>
        } />
        <Route path="/tasks" element={
          <ProtectedRoute roles={["EMPLOYEE"]}>
            <Tasks />
          </ProtectedRoute>
        } />
        <Route path="/unauthorized" element={<Unauthorized />} />
        <Route path="*" element={<NotFound/>} />
      </Routes>
    </Router>
  );
}
/*
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
*/
export default App; // Permite exportar App hacia main.jsx

