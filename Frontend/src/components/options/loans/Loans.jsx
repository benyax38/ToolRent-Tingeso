import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import loanService from "./loanService";

export default function Loans() {
    const [loans, setLoans] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    const navigate = useNavigate();

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
                    </tr>
                </thead>


                <tbody>
                    {loans.map((loan) => (
                        <tr key={loan.loanId} className="border-b last:border-none">
                            <td className="py-2 px-4">{loan.loanId}</td>
                            <td className="py-2 px-4">{loan.deliveryDate}</td>
                            <td className="py-2 px-4">{loan.deadline}</td>
                            <td className="py-2 px-4">{loan.returnDate}</td>
                            <td className="py-2 px-4">{loan.rentalAmount}</td>
                            <td className="py-2 px-4">{loan.loanStatus}</td>
                            <td className="py-2 px-4">{loan.clientId}</td>
                            <td className="py-2 px-4">{loan.userId}</td>
                            <td className="py-2 px-4">{loan.toolId}</td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
}