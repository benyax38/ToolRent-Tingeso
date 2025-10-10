/*
    * Nombre: Obtener rol
    * Descripción: Función que decodifica el token del backend
    * para obtener el rol asociado al usuario
    * Entradas: Token desde localStorage
    * Salidas: Nombre del rol a partir del token || null
*/

export function getRole() {
    const token = localStorage.getItem("token"); // Recupera el token
    if (!token) return null; // Si no hay token devuelve null

    try {
        /*
            * Aquí se divide el token JWT por puntos, permitiendo separarlo en 3 partes
            * 1. Header --> información sobre algoritmos
            * 2. Payload --> datos del usuario
            * 3. Firma --> permite validar el token (clave privada)
            * atob --> decodifica de base64 a texto
            * JSON.parse() --> Convierte texto JSON a un objeto JavaScript
        */
        const payload = JSON.parse(atob(token.split(".")[1]));
        return payload.role || null; // Extrae el valor del campo rol. Si no existe retorna null
    } catch (error) {
        /* 
            * Captura errores en caso de que el token esté mal formado 
            * o no sea codificable
        */
        console.error("Error decoding token:", error);
        return null;
    }
}