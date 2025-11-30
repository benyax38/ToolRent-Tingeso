import { useEffect, useState } from "react";
import { data, useNavigate } from "react-router-dom";
import userService from "./userService";
import Paginator from "../utils/Paginator";

export default function Users() {
    const [users, setUsers] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    // Variables para filtros
    const [searchId, setSearchId] = useState("");
    const [searchFirstName, setSearchFirstName] = useState("");
    const [searchLastName, setSearchLastName] = useState("");
    const [searchRut, setSearchRut] = useState("");
    const [searchRole, setSearchRole] = useState("");

    // Filtros de la lista
    const filteredUsers = users.filter(user => {
        // Filtro por ID
        const matchId =
            searchId === "" || user.userId.toString() === searchId;

        // Filtro por nombre (insensible a mayusculas)
        const matchFirstName =
            searchFirstName === "" || user.userFirstName.toLowerCase().includes(searchFirstName.toLowerCase());

        // Filtro por apellido (insensible a mayusculas)
        const matchLastName =
            searchLastName === "" || user.userLastName.toLowerCase().includes(searchLastName.toLowerCase());

        // Filtro por RUT
        const matchRut =
            searchRut === "" || user.userRut.toLowerCase().includes(searchRut.toLowerCase());

        // Filtro por rol (insensible a mayusculas)
        const matchRole =
            searchRole === "" || user.role.roleName.toLowerCase().includes(searchRole.toLowerCase());
        
        return matchId && matchFirstName && matchLastName && matchRut && matchRole;
    });

    // Reinicio de pagina al aplicar filtros
    useEffect(() => {
        setCurrentPage(1);
    }, [searchId, searchFirstName, searchLastName, searchRut, searchRole]);

    // Estados de paginacion
    const [currentPage, setCurrentPage] = useState(1);
    const itemsPerPage = 10;

    // Calculo de paginacion
    const totalPages = Math.ceil(filteredUsers.length / itemsPerPage);
    const indexOfLastItem = currentPage * itemsPerPage;
    const indexOfFirstItem = indexOfLastItem - itemsPerPage;
    const currentUsers = filteredUsers.slice(indexOfFirstItem, indexOfLastItem);

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

            {/* Filtros por ID, nombre, apellido, RUT y rol */}
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

                {/* Filtro por rol */}
                <div className="flex flex-col">
                    <label className="text-sm text-gray-600">Filtrar por rol</label>
                    <input
                        type="text"
                        value={searchRole}
                        onChange={(e) => setSearchRole(e.target.value)}
                        className="border border-gray-300 rounded-lg px-3 py-1"
                        placeholder="Ej: ADMIN"
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
                        <th className="py-3 px-4 text-left">Rol</th>
                    </tr>
                </thead>


                <tbody>
                    {currentUsers.map((user) => (
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

            <Paginator
                currentPage={currentPage}
                totalPages={totalPages}
                onPageChange={setCurrentPage}
            />
        </div>
    );
}