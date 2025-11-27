import httpClient from "../../../http-common";

const getAllTools = () => {
    return httpClient.get("/api/tools");
};

// Evaluar herramientas
const evaluateTool = (toolId, userId, evaluationData) => {
    return httpClient.post(
        `/api/tools/evaluation/${toolId}/user/${userId}`,
        evaluationData
    );
};

export default {
    getAllTools,
    evaluateTool
};