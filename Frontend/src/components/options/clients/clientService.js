import httpClient from "../../../http-common";

const getAllClients = () => {
    return httpClient.get("/api/clients");
};

export default {
    getAllClients
};