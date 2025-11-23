import httpClient from "../../../http-common";

const getAllLoans = () => {
    return httpClient.get("/api/loans");
};

export default {
    getAllLoans
};