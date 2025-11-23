import httpClient from "../../../http-common";

const getAllPenalties = () => {
    return httpClient.get("/api/penalties");
};

export default {
    getAllPenalties
};