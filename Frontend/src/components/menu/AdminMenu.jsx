import React from "react";
import { useNavigate } from "react-router-dom";

const AdminMenu = () => {
    const navigate = useNavigate();

    return (
        <div className="grid grid-cols-1 sm:grid-cols-2 gap-4 w-full max-w-md">
            <button
                onClick={() => navigate("/herramientas")}
                className="p-6 bg-blue-500 text-white rounded-2x1 text-lg font-semibold hover:bg-blue-600">
                Gestionar Herramientas
            </button>

            <button
                onClick={() => navigate("/arriendos")}
                className="p-6 bg-green-500 text-white rounded-2x1 text-lg font-semibold hover:bg-green-600">
                Ver Arriendos
            </button>

            <button
                onClick={() => navigate("/empleados")}
                className="p-6 bg-purple-500 text-white rounded-2xl text-lg font-semibold hover:bg-purple-600">
                Gestionar Empleados
            </button>

            <button
                onClick={() => navigate("/reportes")}
                className="p-6 bg-orange-500 text-white rounded-2xl text-lg font-semibold hover:bg-orange-600">
                Reportes
            </button>
        </div>
    );
};

export default AdminMenu;