import httpClient from "../../../http-common";

const getAllCatalogs = () => {
    return httpClient.get("/api/catalogs");
};

export default {
    getAllCatalogs
};