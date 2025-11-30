import React from "react";
import { useNavigate } from "react-router-dom";
import { getRole } from "../auth/auth";
import AdminMenu from "../menu/AdminMenu";
import EmployeeMenu from "../menu/EmployeeMenu";

/*
    * Esta página se encarga del renderizado de menús
    * según el rol que se ha obtenido a partir de la 
    * información entregada en el login
*/
const Home = () => {
    const navigate = useNavigate();
    const role = getRole(); // Obtiene el rol del usuario

    return (
        <div className="flex flex-col items-center justify-center min-h-screen bg-gray-100 p-6">
            <h1 className="text-3x1 font-bold mb-6">
                Bienvenido al sistema de arriendo de herramientas
            </h1>

            {role === "ADMIN" && <AdminMenu />}
            {role === "EMPLOYEE" && <EmployeeMenu />}
            {!role && (
                <div className="flex flex-col items-center gap-4">
                    <p className="text-red-600 text-lg font-semibold">
                        No tienes un rol asignado
                    </p>

                    <button
                        onClick={() => navigate("/login")}
                        className="px-6 py-2 bg-blue-500 text-white rounded-xl shadow-md hover:bg-blue-600 transition-all"
                    >
                        Volver al login
                    </button>
                </div>
            )}
        </div>
    );
};

export default Home;