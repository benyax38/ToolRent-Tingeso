import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import toolService from "./toolService";

export default function Tools() {
    const [tools, setTools] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    // Estados para el modal
    const [showModal, setShowModal] = useState(false);
    const [selectedToolId, setSelectedToolId] = useState(null);

    const [loanId, setLoanId] = useState("");
    const [decision, setDecision] = useState("REPARADA");
    const [notes, setNotes] = useState("");

    // Obtener usuario del login
    const user = JSON.parse(localStorage.getItem("user"));
    const userId = user?.userId;

    const navigate = useNavigate();

    useEffect(() => {
        fetchTools();
    }, []);

    const fetchTools = async () => {
        try {
            const res = await toolService.getAllTools();
            setTools(res.data);
        } catch (err) {
            setError("Error al cargar usuarios.");
        } finally {
            setLoading(false);
        }
    };

    if (loading) {
        return <p className="text-center text-gray-600 mt-4">Cargando herramientas...</p>;
    }


    if (error) {
        return <p className="text-center text-red-500 mt-4">{error}</p>;
    }

    // Evaluar herramienta
    const handleEvaluate = async () => {
        try {
            await toolService.evaluateTool(selectedToolId, userId, {
                loanId: Number(loanId),
                decision,
                notes,
            });

            alert("Evaluación registrada correctamente.");

            setShowModal(false);
            setLoanId("");
            setNotes("");
            fetchTools(); // refrescar tabla
        } catch (err) {
            alert("Error al registrar evaluación.");
        }
    };

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


            <h1 className="text-2xl font-bold mb-6 text-center">Gestión de Herramientas</h1>


            {/* Tabla */}
            <table className="min-w-full bg-white border border-gray-300 rounded-lg overflow-hidden">
                <thead className="bg-blue-500 text-white">
                    <tr>
                        <th className="py-3 px-4 text-left">ID</th>
                        <th className="py-3 px-4 text-left">Estado</th>
                        <th className="py-3 px-4 text-left">ID catálogo</th>
                        <th className="py-3 px-4 text-left">Nombre</th>
                        <th className="py-3 px-4">Acciones</th>
                    </tr>
                </thead>


                <tbody>
                    {tools.map((tool) => (
                        <tr key={tool.toolId} className="border-b last:border-none">
                            <td className="py-2 px-4">{tool.toolId}</td>
                            <td className="py-2 px-4">{tool.currentToolState}</td>
                            <td className="py-2 px-4">{tool.toolCatalogId}</td>
                            <td className="py-2 px-4">{tool.toolCatalogName}</td>

                            <td className="py-2 px-4">
                                {tool.currentToolState === "EN_REPARACION" && (
                                    <button
                                        className="px-3 py-1 bg-green-600 text-white rounded-lg hover:bg-green-700"
                                        onClick={() => {
                                            setSelectedToolId(tool.toolId);
                                            setShowModal(true);
                                        }}
                                >
                                    Evaluar
                                </button>
                                )}
                                
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>

            {/* MODAL */}
            {showModal && (
                <div className="fixed inset-0 bg-black bg-opacity-40 flex justify-center items-center">
                    <div className="bg-white p-6 rounded-lg shadow-lg w-96">
                        <h2 className="text-xl font-bold mb-4">
                            Evaluar herramienta #{selectedToolId}
                        </h2>

                        <label className="block mb-2">ID del préstamo</label>
                        <input
                            type="number"
                            className="w-full border px-3 py-2 rounded mb-4"
                            value={loanId}
                            onChange={(e) => setLoanId(e.target.value)}
                        />

                        <label className="block mb-2">Decisión:</label>
                        <select
                            className="w-full border px-3 py-2 rounded mb-4"
                            value={decision}
                            onChange={(e) => setDecision(e.target.value)}
                        >
                            <option value="REPARADA">REPARADA</option>
                            <option value="DAR_DE_BAJA">DAR_DE_BAJA</option>
                        </select>

                        <label className="block mb-2">Notas:</label>
                        <textarea
                            className="w-full border px-3 py-2 rounded mb-4"
                            value={notes}
                            onChange={(e) => setNotes(e.target.value)}
                        />
                        
                        <div className="flex justify-between">
                            <button
                                className="px-4 py-2 bg-gray-500 text-white rounded"
                                onClick={() => setShowModal(false)}
                            >
                                Cancelar
                            </button>

                            <button
                                className="px-4 py-2 bg-blue-600 text-white rounded"
                                onClick={handleEvaluate}
                            >
                                Enviar evaluación
                            </button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}