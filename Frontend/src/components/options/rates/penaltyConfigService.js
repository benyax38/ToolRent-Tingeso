import httpClient from "../../../http-common";

const getAllRates = () => {
    return httpClient.get("/api/penalty-config");
};

export default {
    getAllRates
};