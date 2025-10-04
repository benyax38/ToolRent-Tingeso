import { useState } from "react";
import { useNavigate } from "react-router-dom";
import authService from "./authService";
import './Login.css';

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
      const user = await authService.loginUser({ userRut, userPassword });
      console.log("Usuario logueado:", user);
      // Aquí puedes guardar los datos del usuario en contexto o localStorage
    } catch (err) {
      setError(err.message);
    }
  };

  const goToRegister = () => {
    navigate("/register");
  };

  return (
    <div className="login-container">
      <h2>Iniciar Sesión</h2>
      <form onSubmit={handleLogin}>
        <div className="form-group">
          <label>RUT</label>
          <input
            type="text"
            placeholder="RUT"
            value={userRut}
            onChange={(e) => setUserRut(e.target.value)}
            required
          />
        </div>

        <div className="form-group">
          <label>Contraseña</label>
          <input
            type="password"
            placeholder="Contraseña"
            value={userPassword}
            onChange={(e) => setUserPassword(e.target.value)}
            required
          />
        </div>

        {error && <p style={{ color: "red"}}>{error}</p>}

        <div className="button-group">
          <button type="submit" className="btn login-btn">Iniciar Sesión</button>
          <button type="button" className="btn register-btn" onClick={goToRegister}>
            Registrarse
          </button>
        </div>
      </form>
    </div>
  );
}
