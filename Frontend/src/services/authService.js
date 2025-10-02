import axios from "axios";

const API_URL = "http://localhost:8080/api/auth"; // Ajusta a tu backend

// login del usuario
export async function login(rut, password) {
  try {
    const response = await axios.post(`${API_URL}/login`, {
      rut,
      password,
    });
    return response.data; // debería devolverte token o datos del usuario
  } catch (error) {
    throw error.response?.data || { message: "Error de conexión" };
  }
}

// registrar nuevo usuario
export async function register(rut, password) {
  try {
    const response = await axios.post(`${API_URL}/register`, {
      rut,
      password,
    });
    return response.data;
  } catch (error) {
    throw error.response?.data || { message: "Error de conexión" };
  }
}
