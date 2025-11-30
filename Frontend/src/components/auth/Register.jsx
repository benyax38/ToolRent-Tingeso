import { useState } from "react";
import authService from "./authService";
import { useNavigate } from "react-router-dom";

export default function Register() {
  const [userData, setUserData] = useState({
    userFirstName: "",
    userLastName: "",
    userRut: "",
    userPhone: "",
    userEmail: "",
    userPassword: "",
    roleId: ""
  });

  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleRegister = async (e) => {
    e.preventDefault();
    setError("");

    try {
      const newUser = await authService.registerUser(userData);
      console.log("Usuario registrado:", newUser);

      navigate("/login"); // redirige al login despues de registrar

    } catch (err) {
      setError(err.message);
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setUserData({ ...userData, [name]: value});
  };

  return (
    <div className="flex items-center justify-center min-h-screen bg-gray-100">
      <div className="bg-white p-8 rounded-xl shadow-md w-full max-w-lg">

        <h2 className="text-3xl font-bold mb-6 text-center text-gray-800">
          Registro de usuario
        </h2>

        <form onSubmit={handleRegister} className="grid grid-cols-2 gap-4">

          <div className="col-span-1">
            <label className="block text-gray-700 font-medium mb-1">Nombre</label>
            <input
              type="text"
              name="userFirstName"
              value={userData.userFirstName}
              onChange={handleChange}
              className="w-full px-3 py-2 border border-gray-300 rounded-md text-black bg-white focus:ring-blue-200"
              required
            />
          </div>

          <div className="col-span-1">
            <label className="block text-gray-700 font-medium mb-1">Apellido</label>
            <input
              type="text"
              name="userLastName"
              value={userData.userLastName}
              onChange={handleChange}
              className="w-full px-3 py-2 border border-gray-300 rounded-md text-black bg-white focus:ring-blue-200"
              required
            />
          </div>

          <div className="col-span-2">
            <label className="block text-gray-700 font-medium mb-1">RUT</label>
            <input
              type="text"
              name="userRut"
              value={userData.userRut}
              onChange={handleChange}
              className="w-full px-3 py-2 border border-gray-300 rounded-md text-black bg-white"
              required
            />
          </div>

          <div className="col-span-2">
            <label className="block text-gray-700 font-medium mb-1">Teléfono</label>
            <input
              type="text"
              name="userPhone"
              value={userData.userPhone}
              onChange={handleChange}
              className="w-full px-3 py-2 border border-gray-300 rounded-md text-black bg-white"
              required
            />
          </div>

          <div className="col-span-2">
            <label className="block text-gray-700 font-medium mb-1">Email</label>
            <input
              type="email"
              name="userEmail"
              value={userData.userEmail}
              onChange={handleChange}
              className="w-full px-3 py-2 border border-gray-300 rounded-md text-black bg-white"
              required
            />
          </div>

          <div className="col-span-2">
            <label className="block text-gray-700 font-medium mb-1">Contraseña</label>
            <input
              type="password"
              name="userPassword"
              value={userData.userPassword}
              onChange={handleChange}
              className="w-full px-3 py-2 border border-gray-300 rounded-md text-black bg-white"
              required
            />
          </div>

          <div className="col-span-2">
            <label className="block text-gray-700 font-medium mb-1">Rol</label>
            <select
              name="roleId"
              value={userData.roleId}
              onChange={handleChange}
              className="w-full px-3 py-2 border border-gray-300 rounded-md text-black bg-white"
              required
            >
              <option value="" disabled>Selecciona un rol...</option>
              <option value={1}>ADMIN</option>
              <option value={2}>EMPLOYEE</option>
            </select>
          </div>

          {error && (
            <p className="col-span-2 text-red-500 text-center text-sm">
              {error}
            </p>
          )}

          <button
            type="submit"
            className="col-span-2 bg-blue-600 text-white py-2 rounded-md hover:bg-blue-700 transition"
          >
            Registrar
          </button>
        </form>

        <button
          onClick={() => navigate("/login")}
          className="mt-4 w-full text-center text-blue-600 hover:underline"
        >
          Ya tengo una cuenta
        </button>

      </div>
    </div>
  );
}
