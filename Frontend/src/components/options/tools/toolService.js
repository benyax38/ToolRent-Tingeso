import { data } from "react-router-dom";
import httpClient from "../../../http-common";

const registerTool = async (data, userId) => {
const response = await httpClient.post(`/api/catalogs?userId=${userId}`, data);
    return response.data;
};

export default { registerTool };