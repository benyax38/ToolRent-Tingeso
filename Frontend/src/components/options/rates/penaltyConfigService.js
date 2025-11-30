import httpClient from "../../../http-common";

const getAllRates = () => {
    return httpClient.get("/api/penalty-config");
};

// Crear la configuración de tarifas (cuando no existe ninguna)
const createRates = (data) => {
    return httpClient.post("/api/penalty-config", data);
};

// Modificar la tarifa diaria de multa por atraso
const updateDailyFine = (amount) => {
    return httpClient.patch(
        "/api/penalty-config/daily-fine",
        amount,
        { headers: { "Content-Type": "application/json" } }
    );
};

// Modificar el cargo por reparación
const updateRepairCharge = (amount) => {
    return httpClient.patch(
        "/api/penalty-config/repair-charge",
        amount,
        { headers: { "Content-type": "application/json" } }
    );
};

export default {
    getAllRates,
    createRates,
    updateDailyFine,
    updateRepairCharge
};
