import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import penaltyConfigService from "./penaltyConfigService";

export default function Rates() {
    const [rates, setRates] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    const navigate = useNavigate();

    useEffect(() => {
        fetchRates();
    }, []);

    const fetchRates = async () => {
        try {
            const res = await penaltyConfigService.getAllRates();
            setRates([res.data]);
        } catch (err) {
            setError("Error al cargar usuarios.");
        } finally {
            setLoading(false);
        }
    };

    if (loading) {
        return <p className="text-center text-gray-600 mt-4">Cargando tarifas...</p>;
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


            <h1 className="text-2xl font-bold mb-6 text-center">Gestión de Tarifas</h1>


            {/* Tabla */}
            <table className="min-w-full bg-white border border-gray-300 rounded-lg overflow-hidden">
                <thead className="bg-blue-500 text-white">
                    <tr>
                        <th className="py-3 px-4 text-left">ID</th>
                        <th className="py-3 px-4 text-left">Tarifa diaria de multa por atraso</th>
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
        </div>
    );
}