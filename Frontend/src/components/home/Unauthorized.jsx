import { Link } from "react-router-dom";

function Unauthorized() {
    return (
        <div className="min-h-screen flex flex-col justify-center items-center bg-gray-100 px-4">
            <div className="bg-white shadow-lg rounded-xl p-8 max-w-md w-full text-center">
                <h1 className="text-3xl font-bold text-red-600 mb-4">
                    Acceso denegado
                </h1>

                <p className="text-gray-700 mb-6">
                    No tienes los permisos necesarios para ingresar a esta sección.
                </p>

                <Link
                    to="/home"
                    className="inline-block bg-blue-600 text-white px-5 py-2 rounded-lg text-sm font-semibold hover:bg-blue-700 transition duration-200"
                >
                    Volver al menú
                </Link>
            </div>
        </div>
    );
}

export default Unauthorized;
