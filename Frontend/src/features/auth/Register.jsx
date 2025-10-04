import { useState } from "react";
import registerUser from "./authService";

export default function Register() {
  const [userData, setUserData] = useState({
    userFirstName: "",
    userLastName: "",
    userRut: "",
    userPhone: "",
    userEmail: "",
    userPassword: "",
    role: { roleId: 2 } // ejemplo: ADMIN
  });
  const [error, setError] = useState("");

  const handleRegister = async (e) => {
    e.preventDefault();
    setError("");

    try {
      const newUser = await registerUser(userData);
      console.log("Usuario registrado:", newUser);
    } catch (err) {
      setError(err.message);
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    if (name === "roleId") {
      setUserData({ ...userData, role: { roleId: value } });
    } else {
      setUserData({ ...userData, [name]: value });
    }
  };

  return (
    <form onSubmit={handleRegister}>
      <input type="text" name="userFirstName" placeholder="Nombre" value={userData.userFirstName} onChange={handleChange} required />
      <input type="text" name="userLastName" placeholder="Apellido" value={userData.userLastName} onChange={handleChange} required />
      <input type="text" name="userRut" placeholder="RUT" value={userData.userRut} onChange={handleChange} required />
      <input type="text" name="userPhone" placeholder="Teléfono" value={userData.userPhone} onChange={handleChange} required />
      <input type="email" name="userEmail" placeholder="Email" value={userData.userEmail} onChange={handleChange} required />
      <input type="password" name="userPassword" placeholder="Contraseña" value={userData.userPassword} onChange={handleChange} required />
      <input type="number" name="roleId" placeholder="ID Rol" value={userData.role.roleId} onChange={handleChange} required />

      <button type="submit">Registrar</button>
      {error && <p style={{ color: "red" }}>{error}</p>}
    </form>
  );
}
