import { BrowserRouter as Router, Route, Routes, Navigate } from 'react-router-dom'
import { getRole } from './components/auth/auth';

import Login from "./components/auth/Login";
import Register from "./components/auth/Register";
import NotFound from "./components/NotFound/NotFound";
import Home from "./components/home/Home";
import Roles from "./components/options/roles/Roles"
import Catalog from "./components/options/catalog/Catalog";
import Users from "./components/options/users/Users";
import Tools from "./components/options/tools/Tools";
import Clients from "./components/options/clients/Clients";
import Kardex from './components/options/kardex/Kardex';
import Loans from './components/options/loans/Loans';
import Penalties from './components/options/penalties/Penalties';
import Rates from './components/options/rates/PenaltyConfig';
import Unauthorized from "./components/home/Unauthorized";

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
        {/* Public access */}
        <Route path="/" element={<Navigate to="/login" replace />} />
        <Route path="/login" element={<Login/>} />
        <Route path="/register" element={<Register/>} />
        <Route path="/home" element={<Home />} />
        <Route path="/clients" element={<Clients />} />
        <Route path="/tools" element={<Tools />} />
        <Route path="/loans" element={<Loans />} />

        {/* ADMIN only */}
        <Route path="/roles" element={
          <ProtectedRoute roles={["ADMIN"]}>
            <Roles />
          </ProtectedRoute>} />

        <Route path="/catalog" element={
          <ProtectedRoute roles={["ADMIN"]}>
            <Catalog />
          </ProtectedRoute>} />

        <Route path="/users" element={
          <ProtectedRoute roles={["ADMIN"]}>
            <Users />
          </ProtectedRoute>} />

        <Route path="/kardex" element={
          <ProtectedRoute roles={["ADMIN"]}>
            <Kardex />
          </ProtectedRoute>} />

        <Route path="/penalties" element={
          <ProtectedRoute roles={["ADMIN"]}>
            <Penalties />
          </ProtectedRoute>} />

        <Route path="/rates" element={
          <ProtectedRoute roles={["ADMIN"]}>
            <Rates />
          </ProtectedRoute>} />
        
        <Route path="/unauthorized" element={<Unauthorized />} />
        <Route path="*" element={<NotFound/>} />
      </Routes>
    </Router>
  );
}

export default App; // Permite exportar App hacia main.jsx

