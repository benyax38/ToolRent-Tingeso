import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import kardexService from "./kardexService";

export default function Kardex() {
    const [kardexes, setKardexes] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    const navigate = useNavigate();

    useEffect(() => {
        fetchKardex();
    }, []);

    const fetchKardex = async () => {
        try {
            const res = await kardexService.getAllKardexes();
            setKardexes(res.data);
        } catch (err) {
            setError("Error al cargar usuarios.");
        } finally {
            setLoading(false);
        }
    };

    if (loading) {
        return <p className="text-center text-gray-600 mt-4">Cargando kardex...</p>;
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


            <h1 className="text-2xl font-bold mb-6 text-center">Gestión de Clientes</h1>


            {/* Tabla */}
            <table className="min-w-full bg-white border border-gray-300 rounded-lg overflow-hidden">
                <thead className="bg-blue-500 text-white">
                    <tr>
                        <th className="py-3 px-4 text-left">ID</th>
                        <th className="py-3 px-4 text-left">Tipo</th>
                        <th className="py-3 px-4 text-left">Fecha</th>
                        <th className="py-3 px-4 text-left">Cantidad</th>
                        <th className="py-3 px-4 text-left">Detalles</th>
                        <th className="py-3 px-4 text-left">ID Cliente</th>
                        <th className="py-3 px-4 text-left">ID Préstamo</th>
                        <th className="py-3 px-4 text-left">ID Herramienta</th>
                        <th className="py-3 px-4 text-left">ID Catálogo</th>
                        <th className="py-3 px-4 text-left">ID Usuario</th>
                    </tr>
                </thead>


                <tbody>
                    {kardexes.map((kardex) => (
                        <tr key={kardex.movementId} className="border-b last:border-none">
                            <td className="py-2 px-4">{kardex.movementId}</td>
                            <td className="py-2 px-4">{kardex.movementType}</td>
                            <td className="py-2 px-4">{kardex.movementDate}</td>
                            <td className="py-2 px-4">{kardex.affectedAmount}</td>
                            <td className="py-2 px-4">{kardex.details}</td>
                            <td className="py-2 px-4">{kardex.clientId}</td>
                            <td className="py-2 px-4">{kardex.loanId}</td>
                            <td className="py-2 px-4">{kardex.toolId}</td>
                            <td className="py-2 px-4">{kardex.catalogId}</td>
                            <td className="py-2 px-4">{kardex.userId}</td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
}