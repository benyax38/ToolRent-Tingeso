import { useState } from "react";
import { useNavigate } from "react-router-dom";
import authService from "./authService";

export default function Login() {

  // useState("") inicializa cada estado como una cadena vacía.
  // Los setters son funciones que permiten actualizar esos estados.
  const [userRut, setUserRut] = useState(""); // almacena el RUT ingresado
  const [userPassword, setUserPassword] = useState(""); // almacena el password ingresado
  const [error, setError] = useState(""); // almacena mensajes de error si la autenticación falla

  const navigate = useNavigate(); // Hook para redirección

  /*
    * Nombre: Manejador de login
    * Descripción: Función que se ejecuta al enviar el formulario
    * Entradas:
    * Salidas: 
  */
  const handleLogin = async (e) => {
    e.preventDefault(); // evita que se refresque la página al enviar el formulario
    setError(""); // limpia errores previos

    try {

      const data = await authService.loginUser({ userRut, userPassword });

      // Guardar token y usuario en localStorage
      localStorage.setItem("token", data.token);
      localStorage.setItem("user", JSON.stringify(data.user));

      navigate("/home"); // o usar navigate de react-router

    } catch (err) {
      setError(err.message);
    }
  };

  return (
    <div className="flex items-center justify-center min-h-screen bg-gray-100">
      <div className="bg-white p-8 rounded-xl shadow-md w-full max-w-sm">
        <h2 className="text-2xl font-bold mb-6 text-center text-gray-800">
          Iniciar Sesión
        </h2>

        <form onSubmit={handleLogin}>

          <div className="mb-4">
            <label className="block text-gray-700 font-medium mb.1">
              RUT
            </label>
            <input
              type="text"
              placeholder="RUT"
              value={userRut}
              onChange={(e) => setUserRut(e.target.value)}
              required
              className="w-full px-3 py-2 border border-gray-300 rounded-md text-black bg-white focus:ring focus:ring-blue-200 outline-none"
            />
          </div>

          <div className="mb-4">
            <label className="block text-gray-700 font-medium mb-1">
              Contraseña
            </label>
            <input
              type="password"
              placeholder="Contraseña"
              value={userPassword}
              onChange={(e) => setUserPassword(e.target.value)}
              required
              className="w-full px-3 py-2 border border-gray-300 rounded-md text-black bg-white focus:ring focus:ring-blue-200 outline-none"
            />
          </div>

          {error && (
            <p className="text-red-500 text-sm mb-4 text-center">{error}</p>
          )}

          <button
            type="submit"
            className="w-full bg-blue-600 text-white py-2 rounded-md hover:bg-blue-700 transition mb-3"
          >
            Iniciar Sesión
          </button>

          <button
            type="button"
            className="w-full bg-gray-200 text-gray-700 py-2 rounded-md hover:bg-gray-300 transition"
            onClick={() => navigate("/register")}
          >
            Registrarse
          </button>

        </form>
      </div>
    </div>
  );
}
