import httpClient from "../../../http-common";

// Obtiene todos los registros del catalogo
const getAllCatalogs = () => {
    return httpClient.get("/api/catalogs");
};

// Crear una herramienta (no existente)
const createCatalog = (userId, body) => {
    return httpClient.post(`/api/catalogs?userId=${userId}`, body);
};

// Agregar unidades a una herramienta existente
const addUnits = (catalogId, body) => {
    return httpClient.post(`/api/catalogs/${catalogId}/add-units`, body);
};

export default {
    getAllCatalogs,
    createCatalog,
    addUnits
};