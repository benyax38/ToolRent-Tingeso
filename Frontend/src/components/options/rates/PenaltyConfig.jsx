import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import penaltyConfigService from "./penaltyConfigService";

export default function Rates() {
    const [rates, setRates] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    // Estados para los modales
    const [showAddModal, setShowAddModal] = useState(false);
    const [showDailyFineModal, setShowDailyFineModal] = useState(false);
    const [showRepairChargeModal, setShowRepairChargeModal] = useState(false);
    const [createError, setCreateError] = useState("");

    // Campos para formularios
    const [dailyFineRate, setDailyFineRate] = useState("");
    const [repairCharge, setRepairCharge] = useState("");

    const navigate = useNavigate();

    useEffect(() => {
        fetchRates();
    }, []);

    const fetchRates = async () => {
        try {
            const res = await penaltyConfigService.getAllRates();
            if (!res.data || Object.keys(res.data).length === 0) {
                setRates([]);
                return;
            }
            setRates([res.data]);
        } catch (err) {
            if (err.response && err.response.status === 404) {
                setRates([]);
                return;
            }
            setError("Error al cargar tarifas.");
        } finally {
            setLoading(false);
        }
    };

    // Crear tarifa inicial (solo si no existe ninguna)
    const handleCreateRates = async () => {
        setCreateError("");
        
        try {
            const payload = {
                dailyFineRate,
                repairCharge
            };
            await penaltyConfigService.createRates(payload);
            setShowAddModal(false);
            fetchRates();
        } catch (err) {
            alert("Error al agregar tarifas.");
        }
    };

    // Modificar tarifa diaria de multa
    const handleUpdateDailyFine = async () => {
        try {
            await penaltyConfigService.updateDailyFine(Number(dailyFineRate));
            setShowDailyFineModal(false);
            fetchRates();
        } catch (err) {
            alert("Error al actualizar la tarifa diaria.");
        }
    };

    // Modificar cargo por reparación
    const handleUpdateRepairCharge = async () => {
        try {
            await penaltyConfigService.updateRepairCharge(Number(repairCharge));
            setShowRepairChargeModal(false);
            fetchRates();
        } catch (err) {
            alert("Error al actualizar el cargo por reparación.");
        }
    };

    if (loading) {
        return <p className="text-center text-gray-600 mt-4">Cargando tarifas...</p>;
    }

    if (error) {
        return <p className="text-center text-red-500 mt-4">{error}</p>;
    }

    const ratesExist = rates.length > 0;

    return (
        <div className="max-w-5xl mx-auto mt-10 p-6 bg-white shadow-lg rounded-xl text-gray-800">
            {/* Botón de volver */}
            <div className="mb-6">
                <button
                    className="px-4 py-2 bg-gray-500 text-white rounded-lg hover:bg-gray-600"
                    onClick={() => navigate("/home")}
                >
                    ← Volver al menú
                </button>
            </div>

            <h1 className="text-2xl font-bold mb-6 text-center">Gestión de Tarifas</h1>

            {/* Tabla */}
            <table className="min-w-full bg-white border border-gray-300 rounded-lg overflow-hidden">
                <thead className="bg-blue-500 text-white">
                    <tr>
                        <th className="py-3 px-4 text-left">ID</th>
                        <th className="py-3 px-4 text-left">Tarifa diaria de multa</th>
                        <th className="py-3 px-4 text-left">Cargo por reparación</th>
                    </tr>
                </thead>

                <tbody>
                    {rates.map((rate) => (
                        <tr key={rate.penaltyConfigId} className="border-b last:border-none">
                            <td className="py-2 px-4">{rate.penaltyConfigId}</td>
                            <td className="py-2 px-4">{rate.dailyFineRate}</td>
                            <td className="py-2 px-4">{rate.repairCharge}</td>
                        </tr>
                    ))}
                </tbody>
            </table>

            {/* Botones dinámicos */}
            <div className="mt-6 text-center">
                {!ratesExist ? (
                    <button
                        className="px-4 py-3 bg-green-600 text-white rounded-lg hover:bg-green-700"
                        onClick={() => setShowAddModal(true)}
                    >
                        + Agregar Tarifas
                    </button>
                ) : (
                    <div className="flex justify-center gap-4">
                        <button
                            className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700"
                            onClick={() => setShowDailyFineModal(true)}
                        >
                            Modificar tarifa diaria
                        </button>

                        <button
                            className="px-4 py-2 bg-purple-600 text-white rounded-lg hover:bg-purple-700"
                            onClick={() => setShowRepairChargeModal(true)}
                        >
                            Modificar cargo por reparación
                        </button>
                    </div>
                )}
            </div>

            {/* Modal Agregar tarifas */}
            {showAddModal && (
                <Modal title="Agregar tarifas" onClose={() => setShowAddModal(false)}>
                    <label className="font-semibold">Tarifa diaria de multa:</label>
                    <input
                        type="number"
                        className="w-full p-2 border rounded mb-3"
                        value={dailyFineRate}
                        onChange={(e) => setDailyFineRate(e.target.value)}
                    />

                    <label className="font-semibold">Cargo por reparación:</label>
                    <input
                        type="number"
                        className="w-full p-2 border rounded mb-4"
                        value={repairCharge}
                        onChange={(e) => setRepairCharge(e.target.value)}
                    />

                    <ModalButtons onCancel={() => setShowAddModal(false)} onConfirm={handleCreateRates} />
                </Modal>
            )}

            {/* Modal actualizar tarifa diaria */}
            {showDailyFineModal && (
                <Modal title="Modificar tarifa diaria" onClose={() => setShowDailyFineModal(false)}>
                    <label className="font-semibold">Nuevo monto:</label>
                    <input
                        type="number"
                        className="w-full p-2 border rounded mb-4"
                        value={dailyFineRate}
                        onChange={(e) => setDailyFineRate(e.target.value)}
                    />

                    <ModalButtons onCancel={() => setShowDailyFineModal(false)} onConfirm={handleUpdateDailyFine} />
                </Modal>
            )}

            {/* Modal actualizar cargo por reparación */}
            {showRepairChargeModal && (
                <Modal title="Modificar cargo por reparación" onClose={() => setShowRepairChargeModal(false)}>
                    <label className="font-semibold">Nuevo monto:</label>
                    <input
                        type="number"
                        className="w-full p-2 border rounded mb-4"
                        value={repairCharge}
                        onChange={(e) => setRepairCharge(e.target.value)}
                    />

                    <ModalButtons
                        onCancel={() => setShowRepairChargeModal(false)}
                        onConfirm={handleUpdateRepairCharge}
                    />
                </Modal>
            )}
        </div>
    );
}


// --- Componentes auxiliares --- //

function Modal({ title, children, onClose }) {
    return (
        <div className="fixed inset-0 bg-black bg-opacity-40 flex items-center justify-center">
            <div className="bg-white p-6 rounded-xl w-96 shadow-xl">
                <h2 className="text-xl font-bold mb-4 text-center">{title}</h2>
                {children}
            </div>
        </div>
    );
}

function ModalButtons({ onCancel, onConfirm }) {
    return (
        <div className="flex justify-end gap-3 mt-4">
            <button className="px-4 py-2 bg-gray-500 text-white rounded-lg hover:bg-gray-600" onClick={onCancel}>
                Cancelar
            </button>
            <button className="px-4 py-2 bg-green-600 text-white rounded-lg hover:bg-green-700" onClick={onConfirm}>
                Guardar
            </button>
        </div>
    );
}
