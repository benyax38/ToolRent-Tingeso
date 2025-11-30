import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import kardexService from "./kardexService";
import Paginator from "../utils/Paginator";

export default function Kardex() {
    const [kardexes, setKardexes] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    // Variables para filtros
    const [searchId, setSearchId] = useState("");
    const [searchMovementType, setSearchMovementType] = useState("");
    const [searchDateFrom, setSearchDateFrom] = useState("");
    const [searchDateTo, setSearchDateTo] = useState("");
    const [searchClientId, setSearchClientId] = useState("");
    const [searchLoanId, setSearchLoanId] = useState("");
    const [searchToolId, setSearchToolId] = useState("");
    const [searchCatalogId, setSearchCatalogId] = useState("");
    const [searchUserId, setSearchUserId] = useState("");

    // Filtros de la lista
    const filteredKardexes = kardexes.filter(kardex => {
        // Filtro por ID
        const matchId =
            searchId === "" || kardex.movementId.toString() === searchId;

        // Filtro por tipo de movimiento (insensible a mayusculas)
        const matchMovementType =
            searchMovementType === "" || kardex.movementType.toLowerCase().includes(searchMovementType.toLowerCase());

        // Filtro por fecha
        const kardexDate = new Date(kardex.movementDate);

        const matchDateFrom =
            searchDateFrom === "" || kardexDate >= new Date(searchDateFrom);

        const matchDateTo =
            searchDateTo === "" || kardexDate <= new Date(searchDateTo);

        // Filtro por ID cliente
        const matchClientId =
            searchClientId === "" || (kardex.clientId ?? "").toString() === searchClientId;

        // Filtro por ID prestamo
        const matchLoanId =
            searchLoanId === "" || (kardex.loanId ?? "").toString() === searchLoanId;
        
        // Filtro por ID herramienta
        const matchToolId =
            searchToolId === "" || (kardex.toolId ?? "").toString() === searchToolId;

        // Filtro por ID catalogo
        const matchCatalogId =
            searchCatalogId === "" || (kardex.catalogId ?? "").toString() === searchCatalogId;

        // Filtro por ID usuario
        const matchUserId =
            searchUserId === "" || (kardex.userId ?? "").toString() === searchUserId;
        
        return matchId && matchMovementType && matchDateFrom && matchDateTo && matchClientId && matchLoanId && matchToolId && matchCatalogId && matchUserId;
    });

    // Reinicio de pagina al aplicar filtros
    useEffect(() => {
        setCurrentPage(1);
    }, [searchId, searchMovementType, searchDateFrom, searchDateTo, searchClientId, searchLoanId, searchToolId, searchCatalogId, searchUserId]);

    // Estados de paginacion
    const [currentPage, setCurrentPage] = useState(1);
    const itemsPerPage = 10;

    // Calculo de paginacion
    const totalPages = Math.ceil(filteredKardexes.length / itemsPerPage);
    const indexOfLastItem = currentPage * itemsPerPage;
    const indexOfFirstItem = indexOfLastItem - itemsPerPage;
    const currentKardexes = filteredKardexes.slice(indexOfFirstItem, indexOfLastItem);

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

    // Ajustes de formato fecha
    function formatDateTime(dateString) {
        if (!dateString) return "";
        const date = new Date(dateString);
        return date.toISOString().replace("T", " ").substring(0, 19);
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


            <h1 className="text-2xl font-bold mb-6 text-center">Kardex</h1>

            {/* Filtros por ID, tipo, fecha, ID cliente, ID prestamo, ID herramienta, ID catalogo, ID usuario */}
            <div className="flex gap-6 mb-6 flex-wrap">

                {/* Filtro por ID */}
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

                {/* Filtro por tipo de movimiento */}
                <div className="flex flex-col">
                    <label className="text-sm text-gray-600">Filtrar por tipo</label>
                    <input
                        type="text"
                        value={searchMovementType}
                        onChange={(e) => setSearchMovementType(e.target.value)}
                        className="border border-gray-300 rounded-lg px-3 py-1"
                        placeholder="Ej: INGRESO"
                    />
                </div>

                {/* Filtro de fecha inicio */}
                <div className="flex flex-col">
                    <label className="text-sm text-gray-600">Fecha desde</label>
                    <input
                        type="date"
                        value={searchDateFrom}
                        onChange={(e) => setSearchDateFrom(e.target.value)}
                        className="border border-gray-300 rounded-lg px-3 py-1"
                    />
                </div>

                {/* Filtro de fecha final */}
                <div className="flex flex-col">
                    <label className="text-sm text-gray-600">Fecha hasta</label>
                    <input
                        type="date"
                        value={searchDateTo}
                        onChange={(e) => setSearchDateTo(e.target.value)}
                        className="border border-gray-300 rounded-lg px-3 py-1"
                    />
                </div>

                {/* Filtro por ID cliente */}
                <div className="flex flex-col">
                    <label className="text-sm text-gray-600">Filtrar por ID cliente</label>
                    <input
                        type="number"
                        value={searchClientId}
                        onChange={(e) => setSearchClientId(e.target.value)}
                        className="border border-gray-300 rounded-lg px-3 py-1"
                        placeholder="Ej: 10"
                    />
                </div>

                {/* Filtro por ID prestamo */}
                <div className="flex flex-col">
                    <label className="text-sm text-gray-600">Filtrar por ID préstamo</label>
                    <input
                        type="number"
                        value={searchLoanId}
                        onChange={(e) => setSearchLoanId(e.target.value)}
                        className="border border-gray-300 rounded-lg px-3 py-1"
                        placeholder="Ej: 10"
                    />
                </div>

                {/* Filtro por ID herramienta */}
                <div className="flex flex-col">
                    <label className="text-sm text-gray-600">Filtrar por ID herramienta</label>
                    <input
                        type="number"
                        value={searchToolId}
                        onChange={(e) => setSearchToolId(e.target.value)}
                        className="border border-gray-300 rounded-lg px-3 py-1"
                        placeholder="Ej: 10"
                    />
                </div>

                {/* Filtro por ID catalogo */}
                <div className="flex flex-col">
                    <label className="text-sm text-gray-600">Filtrar por ID catálogo</label>
                    <input
                        type="number"
                        value={searchCatalogId}
                        onChange={(e) => setSearchCatalogId(e.target.value)}
                        className="border border-gray-300 rounded-lg px-3 py-1"
                        placeholder="Ej: 10"
                    />
                </div>

                {/* Filtro por ID usuario */}
                <div className="flex flex-col">
                    <label className="text-sm text-gray-600">Filtrar por ID usuario</label>
                    <input
                        type="number"
                        value={searchUserId}
                        onChange={(e) => setSearchUserId(e.target.value)}
                        className="border border-gray-300 rounded-lg px-3 py-1"
                        placeholder="Ej: 10"
                    />
                </div>
            </div>

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
                    {currentKardexes.map((kardex) => (
                        <tr key={kardex.movementId} className="border-b last:border-none">
                            <td className="py-2 px-4">{kardex.movementId}</td>
                            <td className="py-2 px-4">{kardex.movementType}</td>
                            <td className="py-2 px-4">{formatDateTime(kardex.movementDate)}</td>
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

            <Paginator
                currentPage={currentPage}
                totalPages={totalPages}
                onPageChange={setCurrentPage}
            />
        </div>
    );
}