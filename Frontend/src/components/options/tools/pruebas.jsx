import React from "react";

export default function BuscarHerramientas() {
    return (
        <div className="min-h-screen bg-gray-100 flex">
            {/* Panel lateral */}
            <aside className="w-64 bg-white shadow-lg p-6">
                <h2 className="text-lg font-semibold mb-4 text-gray-700">
                    Panel de búsqueda
                </h2>
                <p className="text-gray-500 text-sm">
                    Aquí irá tu formulario más adelante.
                </p>
            </aside>

            {/* Zona principal */}
            <main className="flex-1 p-10">
                <h2 className="text-2x1 font-semibold text-gray-800 mb-4">
                    Resultados
                </h2>
                <p className="text-gray-600 text-sm">
                    Aquí se mostrarán las herramientas encontradas.
                </p>
            </main>
        </div>
    )
}