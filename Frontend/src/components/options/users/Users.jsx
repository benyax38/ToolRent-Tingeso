import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import userService from "./userService";

export default function Users() {
    const [users, setUsers] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    const navigate = useNavigate();

    useEffect(() => {
        fetchUsers();
    }, []);

    const fetchUsers = async () => {
        try {
            const res = await userService.getAllUsers();
            setUsers(res.data);
        } catch (err) {
            setError("Error al cargar usuarios.");
        } finally {
            setLoading(false);
        }
    };

    if (loading) {
        return <p className="text-center text-gray-600 mt-4">Cargando usuarios...</p>;
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


            <h1 className="text-2xl font-bold mb-6 text-center">Gestión de Usuarios</h1>


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
                        <th className="py-3 px-4 text-left">Rol</th>
                    </tr>
                </thead>


                <tbody>
                    {users.map((user) => (
                        <tr key={user.userId} className="border-b last:border-none">
                            <td className="py-2 px-4">{user.userId}</td>
                            <td className="py-2 px-4">{user.userFirstName}</td>
                            <td className="py-2 px-4">{user.userLastName}</td>
                            <td className="py-2 px-4">{user.userRut}</td>
                            <td className="py-2 px-4">{user.userPhone}</td>
                            <td className="py-2 px-4">{user.userEmail}</td>
                            <td className="py-2 px-4">{user.role?.roleName}</td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
}