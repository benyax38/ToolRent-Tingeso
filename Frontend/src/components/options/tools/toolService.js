import httpClient from "../../../http-common";

const getAllTools = () => {
    return httpClient.get("/api/tools");
};

export default {
    getAllTools
};