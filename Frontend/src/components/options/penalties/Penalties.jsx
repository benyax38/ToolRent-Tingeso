import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import penaltyService from "./penaltyService";

export default function Penalties() {
    const [penalties, setPenalties] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    const navigate = useNavigate();

    useEffect(() => {
        fetchPenalties();
    }, []);

    const fetchPenalties = async () => {
        try {
            const res = await penaltyService.getAllPenalties();
            setPenalties(res.data);
        } catch (err) {
            setError("Error al cargar usuarios.");
        } finally {
            setLoading(false);
        }
    };

    if (loading) {
        return <p className="text-center text-gray-600 mt-4">Cargando multas...</p>;
    }


    if (error) {
        return <p className="text-center text-red-500 mt-4">{error}</p>;
    }

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


            <h1 className="text-2xl font-bold mb-6 text-center">Gestión de Multas</h1>


            {/* Tabla */}
            <table className="min-w-full bg-white border border-gray-300 rounded-lg overflow-hidden">
                <thead className="bg-blue-500 text-white">
                    <tr>
                        <th className="py-3 px-4 text-left">ID</th>
                        <th className="py-3 px-4 text-left">Monto</th>
                        <th className="py-3 px-4 text-left">Razón</th>
                        <th className="py-3 px-4 text-left">Días de atraso</th>
                        <th className="py-3 px-4 text-left">Tarifa diaria por atraso</th>
                        <th className="py-3 px-4 text-left">Cargo por reparación</th>
                        <th className="py-3 px-4 text-left">Estado</th>
                        <th className="py-3 px-4 text-left">ID Préstamo</th>
                    </tr>
                </thead>


                <tbody>
                    {penalties.map((penalty) => (
                        <tr key={penalty.penaltyId} className="border-b last:border-none">
                            <td className="py-2 px-4">{penalty.penaltyId}</td>
                            <td className="py-2 px-4">{penalty.amount}</td>
                            <td className="py-2 px-4">{penalty.reason}</td>
                            <td className="py-2 px-4">{penalty.delayDays}</td>
                            <td className="py-2 px-4">{penalty.dailyFineRate}</td>
                            <td className="py-2 px-4">{penalty.repairCharge}</td>
                            <td className="py-2 px-4">{penalty.penaltyStatus}</td>
                            <td className="py-2 px-4">{penalty.loanId}</td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
}