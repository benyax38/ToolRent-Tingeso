import { data } from "react-router-dom";
import httpClient from "../../../http-common";

const getAllLoans = () => {
    return httpClient.get("/api/loans");
};

const createLoan = (data) => {
    return httpClient.post("/api/loans", data);
};

const returnLoan = (loanId, userId, data) => {
    return httpClient.patch(`/api/loans/return/${loanId}/user/${userId}`, data);
};

const payLoan = (loanId, data) => {
    return httpClient.patch(`/api/loans/pay/${loanId}`, data);
};

export default {
    getAllLoans,
    createLoan,
    returnLoan,
    payLoan
};