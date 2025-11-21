import { data } from "react-router-dom";
import httpClient from "../../../http-common";

const getAllRoles = () => {
    return httpClient.get("/api/roles");
};

const createRole = (data) => {
    return httpClient.post("/api/roles", data);
};

export default {
    getAllRoles,
    createRole
};