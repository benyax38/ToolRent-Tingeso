import { BrowserRouter as Router, Route, Routes, Navigate } from 'react-router-dom'
import { getRole } from './components/auth/auth';

import Login from "./components/auth/Login";
import Register from "./components/auth/Register";
import NotFound from "./components/notFound/NotFound";
import Home from "./components/home/Home";
import Roles from "./components/options/roles/Roles"
import Catalog from "./components/options/catalog/Catalog";
import Users from './components/options/users/Users';
import Unauthorized from "./components/home/Unauthorized";
import ToolManagement from "./components/options/tools/ToolManagement";
import SearchTool from "./components/options/tools/searchTool";
import Pruebas from "./components/options/tools/pruebas";
import Test from "./components/options/tools/test";

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
        <Route path="/roles" element={<Roles />} />
        <Route path="/catalog" element={<Catalog />} />
        <Route path="/users" element={<Users />} />
        <Route path="/toolmng" element={<ToolManagement />} />
        <Route path="/searchTool" element={<SearchTool />} />
        <Route path="/pruebas" element={<Pruebas />} />
        <Route path="/test" element={<Test />} />
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

