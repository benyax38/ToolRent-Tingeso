import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import clientService from "./clientService";

export default function Clients() {
    const [clients, setClients] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

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
                        <th className="py-3 px-4 text-left">Nombre</th>
                        <th className="py-3 px-4 text-left">Apellido</th>
                        <th className="py-3 px-4 text-left">RUT</th>
                        <th className="py-3 px-4 text-left">Teléfono</th>
                        <th className="py-3 px-4 text-left">Email</th>
                        <th className="py-3 px-4 text-left">Estado</th>
                    </tr>
                </thead>


                <tbody>
                    {clients.map((client) => (
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
        </div>
    );
}