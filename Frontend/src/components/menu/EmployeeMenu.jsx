import React from "react";
import { useNavigate } from "react-router-dom";

const EmployeeMenu = () => {
    const navigate = useNavigate();

    return (
        <div className="grid grid-cols-1 sm:grid-cols-2 gap-4 w-full max-w-md">
            <button
                onClick={() => navigate("/arriendos")}
                className="p-6 bg-green-500 text-white rounded-2xl text-lg font-semibold hover:bg-green-600">
                Registrar / Ver Arriendos
            </button>

            <button
                onClick={() => navigate("/herramientas")}
                className="p-6 bg-blue-500 text-white rounded-2xl text-lg font-semibold hover:bg-blue-600">
                Consultar Herramientas
            </button>

            <button
                onClick={() => navigate("/clientes")}
                className="p-6 bg-yellow-500 text-white rounded-2xl text-lg font-semibold hover:bg-yellow-600">
                Consultar Clientes
            </button>
        </div>
    );
};

export default EmployeeMenu;