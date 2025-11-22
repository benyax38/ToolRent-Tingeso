import httpClient from "../../../http-common";

const getAllUsers = () => {
    return httpClient.get("/api/users");
};

export default {
    getAllUsers
};