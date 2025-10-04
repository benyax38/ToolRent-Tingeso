import axios from "axios";

// Aquí se hace uso de las variables definidas en el archivo .env
const toolrentBackendServer = import.meta.env.VITE_TOOLRENT_BACKEND_SERVER;
const toolrentBackendPort = import.meta.env.VITE_TOOLRENT_BACKEND_PORT;

console.log(toolrentBackendServer)
console.log(toolrentBackendPort)

export default axios.create({
    baseURL: `http://${toolrentBackendServer}:${toolrentBackendPort}`,
    headers: {
        'Content-Type': 'application/json'
    }
});