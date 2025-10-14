import React, { useState } from "react";
import toolService from "./toolService";

const SearchTool = () => {
  const [searchId, setSearchId] = useState("");
  const [searchName, setSearchName] = useState("");
  const [tools, setTools] = useState([]);
  const [error, setError] = useState("");

  const handleSearchById = async (e) => {
    e.preventDefault();
    if(!searchId) {
        setError("Debes ingresar un ID para buscar.");
        return;
    }
    try {
      const data = await toolService.getToolsById(searchId);
      setTools(data ? [data] : []);
      setError("");
    } catch (err) {
      console.error("Error buscando por ID:", err);
      setError("No se encontró una herramienta con ese ID.");
      setTools([]);
    }
  };

  const handleSearchByName = async (e) => {
    e.preventDefault();
    if (!searchName) {
        setError("Debes ingresar un nombre para buscar.");
        return;
    }
    try {
      const data = await toolService.getToolsByName(searchName);
      setTools(Array.isArray(data) ? data : [data]);
      setError("");
    } catch (err) {
      console.error("Error buscando por nombre:", err);
      setError("No se encontraron herramientas con ese nombre.");
      setTools([]);
    }
  };

  const handleDarDeBaja = (id) => {
    console.log(`Dar de baja herramienta con ID: ${id}`);
    // Aquí luego puedes implementar un DELETE o PUT al backend
  };

  return (
    <div className="min-h-screen bg-gray-100 flex flex-col">
      {/* Contenedor de búsqueda */}
      <div className="p-6 flex flex-col items-start gap-4">
        {/* Búsqueda por ID */}
        <form onSubmit={handleSearchById} className="flex items-center gap-2">
          <input
            type="number"
            placeholder="Buscar por ID"
            value={searchId}
            onChange={(e) => setSearchId(e.target.value)}
            className="p-2 border border-gray-300 rounded-md w-48"
          />
          <button
            type="submit"
            className="bg-blue-500 hover:bg-blue-600 text-white px-4 py-2 rounded-md"
          >
            Buscar
          </button>
        </form>

        {/* Búsqueda por nombre */}
        <form onSubmit={handleSearchByName} className="flex items-center gap-2">
          <input
            type="text"
            placeholder="Buscar por nombre"
            value={searchName}
            onChange={(e) => setSearchName(e.target.value)}
            className="p-2 border border-gray-300 rounded-md w-48"
          />
          <button
            type="submit"
            className="bg-green-500 hover:bg-green-600 text-white px-4 py-2 rounded-md"
          >
            Buscar
          </button>
        </form>
      </div>

      {/* Tabla de resultados */}
      <div className="flex justify-center items-start mt-10">
        {error && <p className="text-red-600 text-center">{error}</p>}
        
        {tools.length > 0 ? (
          <div className="overflow-x-auto w-11/12 bg-white shadow-lg rounded-lg">
            <table className="min-w-full text-left border-collapse">
              <thead>
                <tr className="bg-gray-200">
                  <th className="px-4 py-2 border">ID</th>
                  <th className="px-4 py-2 border">Nombre</th>
                  <th className="px-4 py-2 border">Categoría</th>
                  <th className="px-4 py-2 border">Unidades Disponibles</th>
                  <th className="px-4 py-2 border">Valor Arriendo</th>
                  <th className="px-4 py-2 border">Valor Reposición</th>
                  <th className="px-4 py-2 border">Descripción</th>
                  <th className="px-4 py-2 border">Acción</th>
                </tr>
              </thead>
              <tbody>
                {tools.map((tool) => (
                  <tr key={tool.toolCatalogId} className="hover:bg-gray-50">
                    <td className="px-4 py-2 border">{tool.toolCatalogId}</td>
                    <td className="px-4 py-2 border">{tool.toolName}</td>
                    <td className="px-4 py-2 border">{tool.toolCategory}</td>
                    <td className="px-4 py-2 border">{tool.availableUnits}</td>
                    <td className="px-4 py-2 border">{tool.rentalValue}</td>
                    <td className="px-4 py-2 border">{tool.replacementValue}</td>
                    <td className="px-4 py-2 border">{tool.description}</td>
                    <td className="px-4 py-2 border text-center">
                      <button
                        onClick={() => handleDarDeBaja(tool.toolCatalogId)}
                        className="bg-red-500 hover:bg-red-600 text-white px-3 py-1 rounded-md"
                      >
                        Dar de baja
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        ) : (
          !error && <p className="text-gray-500 mt-20">No se encontraron herramientas.</p>
        )}
      </div>
    </div>
  );
};

export default SearchTool;
