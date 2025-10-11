import { Link } from "react-router-dom";

function Unauthorized() {
    return (
        <div>
            <h1>Acceso no autorizado</h1>
            <p>No tienes permisos para acceder a esta página.</p>
            <Link to="/login">Volver al login</Link>
        </div>
    );
}

export default Unauthorized;