import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import toolService from "./toolService";

export default function Tools() {
    const [tools, setTools] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

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
                    </tr>
                </thead>


                <tbody>
                    {tools.map((tool) => (
                        <tr key={tool.toolId} className="border-b last:border-none">
                            <td className="py-2 px-4">{tool.toolId}</td>
                            <td className="py-2 px-4">{tool.currentToolState}</td>
                            <td className="py-2 px-4">{tool.toolCatalogId}</td>
                            <td className="py-2 px-4">{tool.toolCatalogName}</td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
}