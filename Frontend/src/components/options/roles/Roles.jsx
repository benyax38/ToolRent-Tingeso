import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import rolesService from "./roleService";

export default function Roles() {
    const [roles, setRoles] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    const [isModalOpen, setIsModalOpen] = useState(false);
    const [newRoleName, setNewRoleName] = useState("");
    const [createError, setCreateError] = useState("");

    const navigate = useNavigate();

    useEffect(() => {
        fetchRoles();
    }, []);

    const fetchRoles = async () => {
        try {
            const res = await rolesService.getAllRoles();
            setRoles(res.data);
        } catch (err) {
            setError("Error al cargar roles.");
        } finally {
            setLoading(false);
        }
    };

    const handleCreateRole = async (e) => {
        e.preventDefault();
        setCreateError("");

        try {
            await rolesService.createRole({ roleName : newRoleName });
            setIsModalOpen(false);
            setNewRoleName("");
            fetchRoles(); // refrescar tabla
        } catch (err) {
            setCreateError("No se pudo crear el rol.");
        }
    };

    if (loading) {
        return <p className="text-center text-gray-600 mt-4">Cargando roles...</p>;
    }

    if (error) {
        return <p className="text-center text-red-500 mt-4">{error}</p>;
    }

    return (
        <div className="max-w-2xl mx-auto mt-10 p-6 bg-white shadow-lg rounded-xl text-gray-800">
            {/* Botón de volver */}
            <div className="mb-6">
                <button
                    className="px-4 py-2 bg-gray-500 text-white rounded-lg hover:bg-gray-600"
                    onClick={() => navigate("/home")}
                >
                    ← Volver al menú
                </button>

            </div>

            <h1 className="text-2xl font-bold mb-6 text-center">Gestión de Roles</h1>

            {/* Tabla */}
            <table className="min-w-full bg-white border border-gray-300 rounded-lg overflow-hidden">
                <thead className="bg-blue-500 text-white">
                    <tr>
                        <th className="py-3 px-4 text-left">ID</th>
                        <th className="py-3 px-4 text-left">Nombre del rol</th>
                    </tr>
                </thead>

                <tbody>
                    {roles.map((role) => (
                        <tr key={role.roleId} className="border-b last:border-none">
                            <td className="py-2 px-4">{role.roleId}</td>
                            <td className="py-2 px-4">{role.roleName}</td>
                        </tr>
                    ))}
                </tbody>
            </table>

            {/* Botón para abrir modal */}
            <div className="text-center mt-6">
                <button
                    className="px-6 py-3 bg-blue-500 text-white font-semibold rounded-xl hover:bg-blue-600"
                    onClick={() => setIsModalOpen(true)}
                >
                    Crear nuevo rol
                </button>
            </div>

            {/* Modal */}
            {isModalOpen && (
                <div className="fixed inset-0 bg-gray-900 bg-opacity-50 flex items-center justify-center">
                    <div className="bg-white p-6 rounded-xl shadow-xl w-80">
                        <h2 className="text-xl font-bold mb-4 text-center">Nuevo Rol</h2>

                        <form onSubmit={handleCreateRole}>
                            <label className="block mb-2 text-gray-700 font-medium">
                                Nombre del rol
                            </label>
                            <input
                                type="text"
                                value={newRoleName}
                                onChange={(e) => setNewRoleName(e.target.value)}
                                className="w-full px-3 py-2 border border-gray-300 rounded-md focus:ring-blue-300"
                                placeholder="ADMIN, EMPLOYEE, etc."
                                required
                            />

                            {createError && (
                                <p className="text-red-500 mt-2 text-sm">{createError}</p>
                            )}

                            <div className="flex justify-between mt-5">
                                <button
                                    type="button"
                                    className="px-4 py-2 bg-gray-400 text-white rounded-lg hover:bg-gray-500"
                                    onClick={() => setIsModalOpen(false)}
                                >
                                    Cancelar
                                </button>

                                <button
                                    type="submit"
                                    className="px-4 py-2 bg-blue-500 text-white rounded-lg hover:bg-blue-600"
                                >
                                    Crear
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            )}
        </div>
    );
}