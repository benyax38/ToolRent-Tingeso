import httpClient from "../../../http-common";

const getAllClients = () => {
    return httpClient.get("/api/clients");
};

const createClient = (body) => {
    return httpClient.post("/api/clients", body);
};

export default {
    getAllClients,
    createClient
};