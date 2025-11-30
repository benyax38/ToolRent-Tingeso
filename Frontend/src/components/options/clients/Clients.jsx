import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import clientService from "./clientService";
import Paginator from "../utils/Paginator";

export default function Clients() {
    const [clients, setClients] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    const [showModal, setShowModal] = useState(false);
    const [newClient, setNewClient] = useState({
        clientFirstName: "",
        clientLastName: "",
        clientRut: "",
        clientPhone: "",
        clientEmail: ""
    });

    // Variables para filtros
    const [searchId, setSearchId] = useState("");
    const [searchFirstName, setSearchFirstName] = useState("");
    const [searchLastName, setSearchLastName] = useState("");
    const [searchRut, setSearchRut] = useState("");
    const [searchStatus, setSearchStatus] = useState("");

    // Filtros de la lista
    const filteredClients = clients.filter(client => {
        // Filtro por ID
        const matchId =
            searchId === "" || client.clientId.toString() === searchId;

        // Filtro por nombre (insensible a mayusculas)
        const matchFirstName =
            searchFirstName === "" || client.clientFirstName.toLowerCase().includes(searchFirstName.toLowerCase());

        // Filtro por apellido (insensible a mayusculas)
        const matchLastName =
            searchLastName === "" || client.clientLastName.toLowerCase().includes(searchLastName.toLowerCase());

        // Filtro por RUT
        const matchRut =
            searchRut === "" || client.clientRut.toLowerCase().includes(searchRut.toLowerCase());

        // Filtro por estado (insensible a mayusculas)
        const matchStatus =
            searchStatus === "" || client.clientStatus.toLowerCase().includes(searchStatus.toLowerCase());
        
        return matchId && matchFirstName && matchLastName && matchRut && matchStatus;
    });

    // Reinicio de pagina al aplicar filtros
    useEffect(() => {
        setCurrentPage(1);
    }, [searchId, searchFirstName, searchLastName, searchRut, searchStatus]);

    // Estados de paginacion
    const [currentPage, setCurrentPage] = useState(1);
    const itemsPerPage = 10;

    // Calculo de paginacion
    const totalPages = Math.ceil(filteredClients.length / itemsPerPage);
    const indexOfLastItem = currentPage * itemsPerPage;
    const indexOfFirstItem = indexOfLastItem - itemsPerPage;
    const currentClients = filteredClients.slice(indexOfFirstItem, indexOfLastItem);

    const navigate = useNavigate();

    useEffect(() => {
        fetchClients();
    }, []);

    const fetchClients = async () => {
        try {
            const res = await clientService.getAllClients();
            setClients(res.data);
        } catch (err) {
            setError("Error al cargar usuarios.");
        } finally {
            setLoading(false);
        }
    };

    if (loading) {
        return <p className="text-center text-gray-600 mt-4">Cargando clientes...</p>;
    }


    if (error) {
        return <p className="text-center text-red-500 mt-4">{error}</p>;
    }

    const handleCreateClient = async () => {
        try {
            await clientService.createClient(newClient);
            fetchClients();
            setShowModal(false);
            setNewClient({
                clientFirstName: "",
                clientLastName: "",
                clientRut: "",
                clientPhone: "",
                clientEmail: ""
            })
        } catch (err) {
            alert("Error al crear cliente");
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


            <h1 className="text-2xl font-bold mb-6 text-center">Gestión de Clientes</h1>

            {/* Filtros por ID, nombre, apellido, RUT y estado */}
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

                {/* Filtro por nombre */}
                <div className="flex flex-col">
                    <label className="text-sm text-gray-600">Filtrar por nombre</label>
                    <input
                        type="text"
                        value={searchFirstName}
                        onChange={(e) => setSearchFirstName(e.target.value)}
                        className="border border-gray-300 rounded-lg px-3 py-1"
                        placeholder="Ej: Juan"
                    />
                </div>

                {/* Filtro por apellido */}
                <div className="flex flex-col">
                    <label className="text-sm text-gray-600">Filtrar por apellido</label>
                    <input
                        type="text"
                        value={searchLastName}
                        onChange={(e) => setSearchLastName(e.target.value)}
                        className="border border-gray-300 rounded-lg px-3 py-1"
                        placeholder="Ej: Pérez"
                    />
                </div>

                {/* Filtro por RUT */}
                <div className="flex flex-col">
                    <label className="text-sm text-gray-600">Filtrar por RUT</label>
                    <input
                        type="text"
                        value={searchRut}
                        onChange={(e) => setSearchRut(e.target.value)}
                        className="border border-gray-300 rounded-lg px-3 py-1"
                        placeholder="Ej: 12345678-9"
                    />
                </div>

                {/* Filtro por estado */}
                <div className="flex flex-col">
                    <label className="text-sm text-gray-600">Filtrar por estado</label>
                    <input
                        type="text"
                        value={searchStatus}
                        onChange={(e) => setSearchStatus(e.target.value)}
                        className="border border-gray-300 rounded-lg px-3 py-1"
                        placeholder="Ej: RESTRINGIDO"
                    />
                </div>
            </div>

            {/* Tabla */}
            <table className="min-w-full bg-white border border-gray-300 rounded-lg overflow-hidden">
                <thead className="bg-blue-500 text-white">
                    <tr>
                        <th className="py-3 px-4 text-left">ID</th>
                        <th className="py-3 px-4 text-left">Nombre</th>
                        <th className="py-3 px-4 text-left">Apellido</th>
                        <th className="py-3 px-4 text-left">RUT</th>
                        <th className="py-3 px-4 text-left">Teléfono</th>
                        <th className="py-3 px-4 text-left">Email</th>
                        <th className="py-3 px-4 text-left">Estado</th>
                    </tr>
                </thead>


                <tbody>
                    {currentClients.map((client) => (
                        <tr key={client.clientId} className="border-b last:border-none">
                            <td className="py-2 px-4">{client.clientId}</td>
                            <td className="py-2 px-4">{client.clientFirstName}</td>
                            <td className="py-2 px-4">{client.clientLastName}</td>
                            <td className="py-2 px-4">{client.clientRut}</td>
                            <td className="py-2 px-4">{client.clientPhone}</td>
                            <td className="py-2 px-4">{client.clientEmail}</td>
                            <td className="py-2 px-4">{client.clientStatus}</td>
                        </tr>
                    ))}
                </tbody>
            </table>

            <Paginator
                currentPage={currentPage}
                totalPages={totalPages}
                onPageChange={setCurrentPage}
            />

            {/* Boton agregar cliente */}
            <div className="flex justify-center mt-6">
                <button
                    className="px-4 py-2 bg-green-600 text-white rounded-lg hover:bg-green-700"
                    onClick={() => setShowModal(true)}
                >
                    + Agregar Cliente
                </button>
            </div>

            {/* Modal */}
            {showModal &&(
                <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center">
                    <div className="bg-white p-6 rounded-xl shadow-lg w-96">
                        <h2 className="text-xl font-bold mb-4 text-center">Nuevo Cliente</h2>

                        <div className="space-y-3">

                            <input
                                type="text"
                                placeholder="Nombre"
                                value={newClient.clientFirstName}
                                onChange={(e) =>
                                    setNewClient({ ...newClient, clientFirstName: e.target.value })
                                }
                                className="w-full p-2 border rounded"
                            />

                            <input
                                type="text"
                                placeholder="Apellido"
                                value={newClient.clientLastName}
                                onChange={(e) =>
                                    setNewClient({ ...newClient, clientLastName: e.target.value })
                                }
                                className="w-full p-2 border rounded"
                            />

                            <input
                                type="text"
                                placeholder="RUT"
                                value={newClient.clientRut}
                                onChange={(e) =>
                                    setNewClient({ ...newClient, clientRut: e.target.value })
                                }
                                className="w-full p-2 border rounded"
                            />

                            <input
                                type="text"
                                placeholder="Teléfono"
                                value={newClient.clientPhone}
                                onChange={(e) =>
                                    setNewClient({ ...newClient, clientPhone: e.target.value })
                                }
                                className="w-full p-2 border rounded"
                            />

                            <input
                                type="email"
                                placeholder="Correo electrónico"
                                value={newClient.clientEmail}
                                onChange={(e) =>
                                    setNewClient({ ...newClient, clientEmail: e.target.value})
                                }
                                className="w-full p-2 border rounded"
                            />
                        </div>

                        <div className="flex justify-between mt-4">
                            <button
                                className="px-4 py-2 bg-gray-400 text-white rounded hover:bg-gray-500"
                                onClick={() => setShowModal(false)}
                            >
                                Cancelar
                            </button>

                            <button
                                className="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700"
                                onClick={handleCreateClient}
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