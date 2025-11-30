import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import penaltyService from "./penaltyService";
import Paginator from "../utils/Paginator";

export default function Penalties() {
    const [penalties, setPenalties] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    // Variables para filtros
    const [searchId, setSearchId] = useState("");
    const [searchPenaltyStatus, setSearchPenaltyStatus] = useState("");
    const [searchLoanId, setSearchLoanId] = useState("");

    // Filtros de la lista
    const filteredPenalties = penalties.filter(penalty => {
        // Filtro por ID
        const matchId =
            searchId === "" || penalty.penaltyId.toString() === searchId;

        // Filtro por estado (insensible a mayusculas)
        const matchPenaltyStatus =
            searchPenaltyStatus === "" || penalty.penaltyStatus.toLowerCase().includes(searchPenaltyStatus.toLowerCase());

        // Filtro por ID prestamo
        const matchLoanId =
            searchLoanId === "" || penalty.loanId.toString() === searchLoanId;

        return matchId && matchPenaltyStatus && matchLoanId;
    });

    // Reinicio de pagina al aplicar filtros
    useEffect(() => {
        setCurrentPage(1);
    }, [searchId, searchPenaltyStatus, searchLoanId]);

    // Estado de paginacion
    const [currentPage, setCurrentPage] = useState(1);
    const itemsPerPage = 10;

    // Calculo de indices para paginacion
    const totalPages = Math.ceil(filteredPenalties.length / itemsPerPage);
    const indexOfLast = currentPage * itemsPerPage;
    const indexOfFirst = indexOfLast - itemsPerPage;
    const currentPenalties = filteredPenalties.slice(indexOfFirst, indexOfLast);

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

            {/* Filtros por ID, estado y ID prestamo */}
            <div className="flex gap-4 mb-6">

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

                {/* Filtro por estado */}
                <div className="flex flex-col">
                    <label className="text-sm text-gray-600">Filtrar por estado</label>
                    <input
                        type="text"
                        value={searchPenaltyStatus}
                        onChange={(e) => setSearchPenaltyStatus(e.target.value)}
                        className="border border-gray-300 rounded-lg px-3 py-1"
                        placeholder="Ej: IMPAGO"
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
            </div>

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
                    {currentPenalties.map((penalty) => (
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

            <Paginator
                currentPage={currentPage}
                totalPages={totalPages}
                onPageChange={setCurrentPage}
            />
        </div>
    );
}