import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import catalogService from "./catalogService";

export default function Catalog(){
    const [catalogs, setCatalogs] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    const navigate = useNavigate();

    useEffect(() => {
        fetchCatalog();
    }, []);

    const fetchCatalog = async () => {
        try {
            const res = await catalogService.getAllCatalogs();
            setCatalogs(res.data);
        } catch (err) {
            setError("Error al cargar el catálogo.");
        } finally {
            setLoading(false);
        }
    };

    if (loading) {
        return <p className="text-center text-gray-600 mt-4">Cargando catálogo...</p>
    }

    if (error) {
        return <p className="text-center text-red-500 mt-4">{error}</p>;
    }

    return (
        <div className="max-w-4xl mx-auto mt-10 p-6 bg-white shadow-lg rounded-xl text-gray-800">
            {/* Botón de volver */}
            <div className="mb-6">
                <button
                    className="px-4 py-2 bg-gray-500 text-white rounded-lg hover:bg-gray-600"
                    onClick={() => navigate("/home")}
                >
                    ← Volver al menú
                </button>
            </div>

            <h1 className="text-2xl font-bold mb-6 text-center">Catálogo de Herramientas</h1>

            {/* Tabla */}
            <table className="min-w-full bg-white border border-gray-300 rounded-lg overflow-hidden">
                <thead className="bg-blue-500 text-white">
                    <tr>
                        <th className="py-3 px-4 text-left">ID</th>
                        <th className="py-3 px-4 text-left">Nombre</th>
                        <th className="py-3 px-4 text-left">Categoría</th>
                        <th className="py-3 px-4 text-left">Valor Arriendo</th>
                        <th className="py-3 px-4 text-left">Valor Reposición</th>
                        <th className="py-3 px-4 text-left">Disponible</th>
                        <th className="py-3 px-4 text-left">Descripción</th>
                    </tr>
                </thead>

                <tbody>
                    {catalogs.map((catalog) => (
                        <tr key={catalog.toolCatalogId} className="border-b last:border-none">
                            <td className="py-2 px-4">{catalog.toolCatalogId}</td>
                            <td className="py-2 px-4">{catalog.toolName}</td>
                            <td className="py-2 px-4">{catalog.toolCategory}</td>
                            <td className="py-2 px-4">${catalog.dailyRentalValue}</td>
                            <td className="py-2 px-4">${catalog.replacementValue}</td>
                            <td className="py-2 px-4">{catalog.availableUnits}</td>
                            <td className="py-2 px-4">{catalog.description}</td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
}