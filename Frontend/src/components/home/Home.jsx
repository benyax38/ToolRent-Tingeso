import React from "react";
import { getRole } from "../auth/auth";
import AdminMenu from "../menu/AdminMenu";
import EmployeeMenu from "../menu/EmployeeMenu";

/*
    * Esta página se encarga del renderizado de menús
    * según el rol que se ha obtenido a partir de la 
    * información entregada en el login
*/
const Home = () => {
    const role = getRole(); // Obtiene el rol del usuario

    return (
        <div className="flex flex-col items-center justify-center min-h-screen bg-gray-100 p-6">
            <h1 className="text-3x1 font-bold mb-6">
                Bienvenido al sistema de arriendo de herramientas
            </h1>

            {role === "ADMIN" && <AdminMenu />}
            {role === "EMPLOYEE" && <EmployeeMenu />}
            {!role && <p className="text-red-600">No tienes un rol asignado</p>}
        </div>
    );
};

export default Home;