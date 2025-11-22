import httpClient from "../../../http-common";

const getAllKardexes = () => {
    return httpClient.get("/api/kardex");
};

export default {
    getAllKardexes
};