import { data } from "react-router-dom";
import httpClient from "../../../http-common";

// 🔹 Registrar herramienta
export const registerTool = async (data, userId) => {
  const response = await httpClient.post(`/api/catalogs?userId=${userId}`, data);
  return response.data;
};

// 🔹 Buscar herramienta por ID
export const getToolsById = async (id) => {
  const response = await httpClient.get(`/api/catalogs/id/${id}`);
  return response.data;
};

// 🔹 Buscar herramientas por nombre
export const getToolsByName = async (toolName) => {
  const response = await httpClient.get(`/api/catalogs/name/${toolName}`);
  return response.data;
};

export default { registerTool, getToolsById, getToolsByName };