import React from "react";
import { useNavigate } from "react-router-dom";

const AdminMenu = () => {
    const navigate = useNavigate();

    const btnClass =
        "p-6 bg-blue-500 text-white rounded-2xl text-lg font-semibold " +
        "hover:bg-blue-600 transition-all";

    const logout = () => {
        // Limpia datos de autenticacion
        localStorage.removeItem("token");
        localStorage.removeItem("role");
        localStorage.removeItem("user");

        // Redirige al login
        navigate("/login");
    };

    return (
        <div className="grid grid-cols-1 sm:grid-cols-2 gap-4 w-full max-w-md">
            
            {/* Boton de cerrar sesion */}
            <button
                onClick={logout}
                className="fixed top-4 left-4 px-5 py-2 bg-red-500 text-white rounded-xl font-semibold shadow-md hover:bg-red-600 transition-all"
            >
                Cerrar sesión
            </button>

            <button onClick={() => navigate("/roles")} className={btnClass}>
                Gestión de roles
            </button>

            <button onClick={() => navigate("/users")} className={btnClass}>
                Gestión de usuarios
            </button>

            <button onClick={() => navigate("/catalog")} className={btnClass}>
                Catálogo de Herramientas
            </button>

            <button onClick={() => navigate("/tools")} className={btnClass}>
                Manejo de herramientas por unidad
            </button>

            <button onClick={() => navigate("/clients")} className={btnClass}>
                Gestión de clientes
            </button>

            <button onClick={() => navigate("/kardex")} className={btnClass}>
                Ver Kardex
            </button>

            <button onClick={() => navigate("/loans")} className={btnClass}>
                Gestión de préstamos
            </button>

            <button onClick={() => navigate("/penalties")} className={btnClass}>
                Gestión de multas
            </button>

            <button onClick={() => navigate("/rates")} className={btnClass}>
                Gestión de tarifas
            </button>
        </div>
    );
};

export default AdminMenu;