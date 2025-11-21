import React from "react";
import { useNavigate } from "react-router-dom";

const AdminMenu = () => {
    const navigate = useNavigate();

    const btnClass =
        "p-6 bg-blue-500 text-white rounded-2xl text-lg font-semibold " +
        "hover:bg-blue-600 transition-all";

    return (
        <div className="grid grid-cols-1 sm:grid-cols-2 gap-4 w-full max-w-md">
            
            <button onClick={() => navigate("/toolmng")} className={btnClass}>
                Gestionar Herramientas
            </button>

            <button onClick={() => navigate("/searchTool")} className={btnClass}>
                Buscar herramientas
            </button>

            <button onClick={() => navigate("/empleados")} className={btnClass}>
                Gestionar Empleados
            </button>

            <button onClick={() => navigate("/reportes")} className={btnClass}>
                Reportes
            </button>

            <button onClick={() => navigate("/roles")} className={btnClass}>
                Gestión de Roles
            </button>
        </div>
    );
};

export default AdminMenu;