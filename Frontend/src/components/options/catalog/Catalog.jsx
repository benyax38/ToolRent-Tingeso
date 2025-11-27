import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import catalogService from "./catalogService";

export default function Catalog(){
    const [catalogs, setCatalogs] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    // Variables para agregar herramientas
    const [showNewToolModal, setShowNewToolModal] = useState(false);
    const [showAddUnitsModal, setShowAddUnitsModal] = useState(false);
    const [selectedCatalogId, setSelectedCatalogId] = useState(null);

    const [newTool, setNewTool] = useState({
        toolName: "",
        toolCategory: "",
        dailyRentalValue: "",
        replacementValue: "",
        description: "",
        availableUnits: ""
    });

    const [unitsToAdd, setUnitsToAdd] = useState("");

    // Se obtiene el id de usuario desde el login
    const user = JSON.parse(localStorage.getItem("user"));
    const userId = user?.userId;

    console.log("User ID:", userId);

    // Variables para filtros
    const [searchId, setSearchId] = useState("");
    const [searchName, setSearchName] = useState("");

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

    // Crear nueva herramienta
    const handleCreateTool = async () => {
        try {
            await catalogService.createCatalog(userId, newTool);
            fetchCatalog();
            setShowNewToolModal(false);
            setNewTool({
                toolName: "",
                toolCategory: "",
                dailyRentalValue: "",
                replacementValue: "",
                description: "",
                availableUnits: ""
            });
        } catch (err) {
            alert("Error al crear herramienta");
        }
    };

    // Agregar unidades
    const handleAddUnits = async () => {
        try {
            await catalogService.addUnits(selectedCatalogId, {
                units: Number(unitsToAdd),
                userId: userId
            });
            fetchCatalog();
            setShowAddUnitsModal(false);
            setUnitsToAdd("");
        } catch (err) {
            alert("Error al agregar unidades");
        }
    };

    if (loading) {
        return <p className="text-center text-gray-600 mt-4">Cargando catálogo...</p>
    }

    if (error) {
        return <p className="text-center text-red-500 mt-4">{error}</p>;
    }

    // Filtros de la lista
    const filteredCatalogs = catalogs.filter(catalog => {
        // Filtro por ID
        const matchId =
            searchId === "" || catalog.toolCatalogId.toString() === searchId;

        // Filtro por nombre (insensible a mayusculas)
        const matchName =
            searchName === "" || catalog.toolName.toLowerCase().includes(searchName.toLowerCase());

        return matchId && matchName;
    });

    return (
        <div className="max-w-4xl mx-auto mt-10 p-6 bg-white shadow-lg rounded-xl text-gray-800">
            
            {/* Boton de volver */}
            <div className="mb-6">
                <button
                    className="px-4 py-2 bg-gray-500 text-white rounded-lg hover:bg-gray-600"
                    onClick={() => navigate("/home")}
                >
                    ← Volver al menú
                </button>
            </div>

            <h1 className="text-2xl font-bold mb-6 text-center">Catálogo de Herramientas</h1>

            {/* Filtros por id y nombre */}
            <div className="flex gap-4 mb-6">

                <div className="flex flex-col">
                    <label className="text-sm text-gray-600">Filtrar por ID</label>
                    <input
                        type="number"
                        value={searchId}
                        onChange={(e) => setSearchId(e.target.value)}
                        className="border border-gray-300 rounded-lg px-3 py-1"
                        placeholder="Ej: 12"
                    />
                </div>

                <div className="flex flex-col">
                    <label className="text-sm text-gray-600">Filtrar por nombre</label>
                    <input
                        type="text"
                        value={searchName}
                        onChange={(e) => setSearchName(e.target.value)}
                        className="border border-gray-300 rounded-lg px-3 py-1"
                        placeholder="Ej: Taladro"
                    />
                </div>
            </div>

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
                        <th className="py-3 px-4">Acciones</th>
                    </tr>
                </thead>

                <tbody>
                    {filteredCatalogs.map((catalog) => (
                        <tr key={catalog.toolCatalogId} className="border-b last:border-none">
                            <td className="py-2 px-4">{catalog.toolCatalogId}</td>
                            <td className="py-2 px-4">{catalog.toolName}</td>
                            <td className="py-2 px-4">{catalog.toolCategory}</td>
                            <td className="py-2 px-4">${catalog.dailyRentalValue}</td>
                            <td className="py-2 px-4">${catalog.replacementValue}</td>
                            <td className="py-2 px-4">{catalog.availableUnits}</td>
                            <td className="py-2 px-4">{catalog.description}</td>
                            <td className="py-2 px-4">
                                <button
                                    className="px-3 py-1 bg-green-600 text-white rounded-lg hover:bg-blue-700"
                                    onClick={() => {
                                        setSelectedCatalogId(catalog.toolCatalogId);
                                        setShowAddUnitsModal(true);
                                    }}
                                >
                                    Agregar stock
                                </button>
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>

            {/* Boton para abrir modal */}
            <div className="text-center mt-6">
                <button
                        className="px-4 py-3 bg-green-600 text-white rounded-lg hover:bg-green-700"
                        onClick={() => setShowNewToolModal(true)}
                    >
                        + Nueva herramienta
                </button>
            </div>

            {/* MODAL: Nueva herramienta */}
            {showNewToolModal && (
                <div className="fixed inset-0 bg-black bg-opacity-40 flex items-center justify-center">
                    <div className="bg-white p-6 rounded-lg w-96 shadow-xl">
                        <h2 className="text-xl font-bold mb-4">Nueva Herramienta</h2>

                        {Object.keys(newTool).map((key) => (
                            <input
                                key={key}
                                type="text"
                                placeholder={key}
                                className="w-full p-2 border rounded mb-2"
                                value={newTool[key]}
                                onChange={(e) => setNewTool({ ...newTool, [key]: e.target.value })}
                            />
                        ))}

                        <div className="flex justify-end gap-2 mt-4">
                            <button
                                className="px-4 py-2 bg-gray-500 text-white rounded"
                                onClick={() => setShowNewToolModal(false)}
                            >
                                Cancelar
                            </button>

                            <button
                                className="px-4 py-2 bg-green-600 text-white rounded"
                                onClick={handleCreateTool}
                            >
                                Crear
                            </button>
                        </div>
                    </div>
                </div>
            )}

            {/* MODAL: Agregar unidades */}
            {showAddUnitsModal && (
                <div className="fixed inset-0 bg-black bg-opacity-40 flex items-center justify-center">
                    <div className="bg-white p-6 rounded-lg w-80 shadow-xl">
                        <h2 className="text-xl font-bold mb-4">Agregar unidades</h2>

                        <input
                            type="number"
                            placeholder="Cantidad"
                            className="w-full p-2 border rounded mb-4"
                            value={unitsToAdd}
                            onChange={(e) => setUnitsToAdd(e.target.value)}
                        />

                        <div className="flex justify-end gap-2">
                            <button
                                className="px-4 py-2 bg-gray-500 text-white rounded"
                                onClick={() => setShowAddUnitsModal(false)}
                            >
                                Cancelar
                            </button>

                            <button
                                className="px-4 py-2 bg-blue-600 text-white rounded"
                                onClick={handleAddUnits}
                            >
                                Agregar
                            </button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}