import React, { useState } from "react";
import { login } from "../services/authService";
import "./Login.css";

function Login() {
  const [rut, setRut] = useState("");
  const [password, setPassword] = useState("");
  const [message, setMessage] = useState("");

  const handleLogin = async () => {
    try {
      const data = await login(rut, password);
      setMessage("Login exitoso ✅");
      console.log("Respuesta backend:", data);
      // aquí podrías guardar el token en localStorage o Context
    } catch (err) {
      setMessage(err.message || "Error en el login ❌");
    }
  };

  return (
    <div className="login-container">
      <h2>Iniciar Sesión</h2>

      <div className="form-group">
        <label>RUT</label>
        <input
          type="text"
          value={rut}
          onChange={(e) => setRut(e.target.value)}
          placeholder="Ingresa tu RUT"
        />
      </div>

      <div className="form-group">
        <label>Contraseña</label>
        <input
          type="password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          placeholder="Ingresa tu contraseña"
        />
      </div>

      <div className="button-group">
        <button className="btn login-btn" onClick={handleLogin}>
          Iniciar Sesión
        </button>
        <button className="btn register-btn">
          Registrarse
        </button>
      </div>

      {message && <p>{message}</p>}
    </div>
  );
}

export default Login;
