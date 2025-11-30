import { use, useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import loanService from "./loanService";
import Paginator from "../utils/Paginator";

export default function Loans() {
    const [loans, setLoans] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    // Estado del modal
    const [showModal, setShowModal] = useState(false);

    // Campos del formulario
    const [deadline, setDeadline] = useState("");
    const [clientId, setClientId] = useState("");
    const [toolId, setToolId] = useState("");

    // Modal de devolucion
    const [showReturnModal, setShowReturnModal] = useState(false);
    const [selectedLoanId, setSelectedLoanId] = useState(null);
    const [damageLevel, setDamageLevel] = useState("NINGUNO");

    // Modal de pago
    const [showPayModal, setShowPayModal] = useState(false);
    const [selectedPayLoan, setSelectedPayLoan] = useState(null);
    const [paymentMethod, setPaymentMethod] = useState("Tarjeta débito");

    // Variables para filtros
    const [searchId, setSearchId] = useState("");
    const [searchDeliveryDate, setSearchDeliveryDate] = useState("");
    const [searchDeadline, setSearchDeadline] = useState("");
    const [searchReturnDate, setSearchReturnDate] = useState("");
    const [searchLoanStatus, setSearchLoanStatus] = useState("");
    const [searchClientId, setSearchClientId] = useState("");
    const [searchUserId, setSearchUserId] = useState("");
    const [searchToolId, setSearchToolId] = useState("");

    // Filtros de la lista
    const filteredLoans = loans.filter(loan => {
        // Filtro por ID
        const matchId =
            searchId === "" || loan.loanId.toString() === searchId;

        // Filtro por fecha de entrega
        const matchDeliveryDate =
            searchDeliveryDate === "" ||
            loan.deliveryDate.slice(0, 10) === searchDeliveryDate;

        // Filtro por fecha limite
        const matchDeadline =
            searchDeadline === "" ||
            loan.deadline.slice(0, 10) === searchDeadline;

        // Filtro por fecha de devolucion
        const matchReturnDate =
            searchReturnDate === "" ||
            loan.returnDate?.slice(0, 10) === searchReturnDate;

        // Filtro por estado
        const matchLoanStatus =
            searchLoanStatus === "" || loan.loanStatus.toLowerCase().includes(searchLoanStatus.toLowerCase());

        // Filtro por ID cliente
        const matchClientId =
            searchClientId === "" || (loan.clientId ?? "").toString() === searchClientId;

        // Filtro por ID usuario
        const matchUserId =
            searchUserId === "" || (loan.userId ?? "").toString() === searchUserId;

        // Filtro por ID herramienta
        const matchToolId =
            searchToolId === "" || (loan.toolId ?? "").toString() === searchToolId;
        
        return matchId && matchDeliveryDate && matchDeadline && matchReturnDate && matchLoanStatus && matchClientId && matchUserId && matchToolId;
    });

    // Reinicio de pagina al aplicar filtros
    useEffect(() => {
        setCurrentPage(1);
    }, [searchId, searchDeliveryDate, searchDeadline, searchReturnDate, searchLoanStatus, searchClientId, searchUserId, searchToolId]);

    // Estados de paginacion
    const [currentPage, setCurrentPage] = useState(1);
    const itemsPerPage = 10;

    // Calculo de paginacion
    const totalPages = Math.ceil(filteredLoans.length / itemsPerPage);
    const indexOfLastItem = currentPage * itemsPerPage;
    const indexOfFirstItem = indexOfLastItem - itemsPerPage;
    const currentLoans = filteredLoans.slice(indexOfFirstItem, indexOfLastItem);

    const navigate = useNavigate();

    // Se obtiene el id de usuario desde el login
    const user = JSON.parse(localStorage.getItem("user"));
    const userId = user?.userId;

    useEffect(() => {
        fetchLoans();
    }, []);

    const fetchLoans = async () => {
        try {
            const res = await loanService.getAllLoans();
            setLoans(res.data);
        } catch (err) {
            setError("Error al cargar usuarios.");
        } finally {
            setLoading(false);
        }
    };

    if (loading) {
        return <p className="text-center text-gray-600 mt-4">Cargando préstamos...</p>;
    }


    if (error) {
        return <p className="text-center text-red-500 mt-4">{error}</p>;
    }

    // Ajustes de formato de fechas
    function formatDateTime(dateString) {
        if (!dateString) return "";
        const date = new Date(dateString);
        return date.toISOString().replace("T", " ").substring(0, 19);
    }

    // Crear prestamo
    const handleCreateTool = async () => {
        try {
            const newLoan = {
                deadline,
                clientId,
                userId: userId,
                toolId
            };

            await loanService.createLoan(newLoan);

            setShowModal(false);
            fetchLoans();
        } catch (err) {
            alert("Error al crear préstamo");
        }
    };

    // Devolver prestamo
    const handleReturnLoan = async () => {
        try {
            const payload = { damageLevel };

            await loanService.returnLoan(selectedLoanId, userId, payload);

            setShowReturnModal(false);
            setSelectedLoanId(null);
            fetchLoans();
        } catch (err) {
            alert("Error al realizar la devolución");
        }
    };

    // Ejecutar pago
    const handlePayLoan = async () => {
        try {
            const payload = {
                amountPaid: selectedPayLoan.rentalAmount,
                paymentMethod
            };

            await loanService.payLoan(selectedPayLoan.loanId, payload);

            setShowPayModal(false);
            setSelectedPayLoan(null);
            fetchLoans();
        } catch (err) {
            alert("Error al realizar el pago");
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


            <h1 className="text-2xl font-bold mb-6 text-center">Gestión de Préstamos</h1>

            {/* Filtros por ID, fecha de entrega, fecha limite, fecha de devolucion, estado, ID cliente, ID usuario e ID herramienta */}
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

                {/* Filtro de fecha de entrega */}
                <div className="flex flex-col">
                    <label className="text-sm text-gray-600">Filtrar por fecha de entrega</label>
                    <input
                        type="date"
                        value={searchDeliveryDate}
                        onChange={(e) => setSearchDeliveryDate(e.target.value)}
                        className="border border-gray-300 rounded-lg px-3 py-1"
                    />
                </div>

                {/* Filtro de fecha limite */}
                <div className="flex flex-col">
                    <label className="text-sm text-gray-600">Filtrar por fecha limite</label>
                    <input
                        type="date"
                        value={searchDeadline}
                        onChange={(e) => setSearchDeadline(e.target.value)}
                        className="border border-gray-300 rounded-lg px-3 py-1"
                    />
                </div>

                {/* Filtro de fecha de devolucion */}
                <div className="flex flex-col">
                    <label className="text-sm text-gray-600">Filtrar por fecha de devolución</label>
                    <input
                        type="date"
                        value={searchReturnDate}
                        onChange={(e) => setSearchReturnDate(e.target.value)}
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
                        <th className="py-3 px-4 text-left">Fecha de entrega</th>
                        <th className="py-3 px-4 text-left">Fecha límite</th>
                        <th className="py-3 px-4 text-left">Fecha de devolución</th>
                        <th className="py-3 px-4 text-left">Costo total de arriendo</th>
                        <th className="py-3 px-4 text-left">Estado</th>
                        <th className="py-3 px-4 text-left">ID Cliente</th>
                        <th className="py-3 px-4 text-left">ID Usuario</th>
                        <th className="py-3 px-4 text-left">ID Herramienta</th>
                        <th className="py-3 px-4 text-center">Acciones</th>
                    </tr>
                </thead>


                <tbody>
                    {currentLoans.map((loan) => (
                        <tr key={loan.loanId} className="border-b last:border-none">
                            <td className="py-2 px-4">{loan.loanId}</td>
                            <td className="py-2 px-4">{formatDateTime(loan.deliveryDate)}</td>
                            <td className="py-2 px-4">{formatDateTime(loan.deadline)}</td>
                            <td className="py-2 px-4">{formatDateTime(loan.returnDate)}</td>
                            <td className="py-2 px-4">{loan.rentalAmount}</td>
                            <td className="py-2 px-4">{loan.loanStatus}</td>
                            <td className="py-2 px-4">{loan.clientId}</td>
                            <td className="py-2 px-4">{loan.userId}</td>
                            <td className="py-2 px-4">{loan.toolId}</td>

                            <td className="py-2 px-4 text-center flex flex-col gap-2 items-center">

                                   {/* Boton de devolucion */}
                                   {(loan.loanStatus === "ACTIVO" || loan.loanStatus === "VENCIDO") && (
                                    <button
                                        className="px-3 py-1 bg-green-600 text-white rounded-lg hover:bg-green-700"
                                        onClick={() => {
                                            setSelectedLoanId(loan.loanId);
                                            setShowReturnModal(true);
                                        }}
                                    >
                                        Hacer Devolución
                                    </button>
                                   )}

                                   {/* Boton de pago */}
                                   {loan.loanStatus === "POR_PAGAR" && (
                                    <button
                                        className="px-3 py-1 bg-blue-600 text-white rounded-lg hover:bg-blue-700"
                                        onClick={() => {
                                            setSelectedPayLoan(loan);
                                            setShowPayModal(true);
                                        }}
                                    >
                                        Pagar
                                    </button>
                                   )}
                            </td>
                        </tr>
                    ))}
                </tbody>
            </table>

            <Paginator
                currentPage={currentPage}
                totalPages={totalPages}
                onPageChange={setCurrentPage}
            />

            {/* Modal devolucion */}
            {showReturnModal && (
                <div className="fixed inset-0 bg-black bg-opacity-40 flex items-center justify-center">
                    <div className="bg-white p-6 rounded-xl w-96">
                        <h2 className="text-xl font-bold mb-4 text-center">
                            Registrar Devolución
                        </h2>

                        <label className="block mb-2 font-semibold">
                            Nivel de daño:
                        </label>

                        <select
                            value={damageLevel}
                            onChange={(e) => setDamageLevel(e.target.value)}
                            className="w-full p-2 border rounded mb-4"
                        >
                            <option value="NINGUNO">NINGUNO</option>
                            <option value="LEVE">LEVE</option>
                            <option value="GRAVE">GRAVE</option>
                        </select>

                        <div className="flex justify-end gap-3 mt-4">
                            <button
                                className="px-4 py-2 bg-gray-500 text-white rounded-lg  hover:bg-gray-600"
                                onClick={() => setShowReturnModal(false)}
                            >
                                Cancelar
                            </button>

                            <button
                                className="px-4 py-2 bg-green-600 text-white rounded-lg hover:bg-green-700"
                                onClick={handleReturnLoan}
                            >
                                Registrar
                            </button>
                        </div>
                    </div>
                </div>
            )}

            {/* Modal de pago */}
            {showPayModal && selectedPayLoan && (
                <div className="fixed inset-0 bg-black bg-opacity-40 flex items-center justify-center">
                    <div className="bg-white p-6 rounded-xl w-96 shadow-xl">
                        <h2 className="text-xl font-bold mb-4 text-center">
                            Registrar pago
                        </h2>

                        <p className="mb-4">
                            <span className="font-semibold">Monto a pagar:</span> ${selectedPayLoan.rentalAmount}
                        </p>

                        <label className="block mb-2 font-semibold">Método de pago:</label>
                        <select
                            value={paymentMethod}
                            onChange={(e) => setPaymentMethod(e.target.value)}
                            className="w-full p-2 border rounded mb-4"
                        >
                            <option value="Tarjeta débito">Tarjeta débito</option>
                            <option value="Tarjeta crédito">Tarjeta crédito</option>
                            <option value="Efectivo">Efectivo</option>
                        </select>

                        <div className="flex justify-end gap-3 mt-4">
                            <button
                                className="px-4 py-2 bg-gray-500 text-white rounded-lg hover:bg-gray-600"
                                onClick={() => setShowPayModal(false)}
                            >
                                Cancelar
                            </button>

                            <button
                                className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700"
                                onClick={handlePayLoan}
                            >
                                Pagar
                            </button>
                        </div>
                    </div>
                </div>
            )}

            {/* Boton para abrir modal de nuevo prestamo */}
             <div className="text-center mt-6">
                <button
                        className="px-4 py-3 bg-green-600 text-white rounded-lg hover:bg-green-700"
                        onClick={() => setShowModal(true)}
                    >
                        + Nuevo Préstamo
                </button>
            </div>

            {/* Modal de agregar prestamo */}
            {showModal && (
                <div className="fixed inset-0 bg-black bg-opacity-40 flex items-center justify-center">
                    <div className="bg-white p-6 rounded-xl shadow-xl w-96">

                        <h2 className="text-xl font-bold mb-4 text-center">
                            Crear nuevo préstamo
                        </h2>

                        <label className="block mb-2 font-semibold">Fecha límite:</label>
                        <input
                            type="datetime-local"
                            value={deadline}
                            onChange={(e) => setDeadline(e.target.value)}
                            className="w-full p-2 border rounded mb-4"
                        />

                        <label className="block mb-2 font-semibold">ID Cliente:</label>
                        <input
                            type="number"
                            value={clientId}
                            onChange={(e) => setClientId(e.target.value)}
                            className="w-full p-2 border rounded mb-4"
                        />

                        <label className="block mb-2 font-semibold">ID Herramienta:</label>
                        <input
                            type="number"
                            value={toolId}
                            onChange={(e) => setToolId(e.target.value)}
                            className="w-full p-2 border rounded mb-4"
                        />

                        <div className="flex justify-end gap-3 mt-4">
                            <button
                                onClick={() => setShowModal(false)}
                                className="px-4 py-2 bg-gray-500 text-white rounded-lg hover:bg-gray-600"
                            >
                                Cancelar
                            </button>

                            <button
                                onClick={handleCreateTool}
                                className="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700"
                            >
                                Crear
                            </button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}