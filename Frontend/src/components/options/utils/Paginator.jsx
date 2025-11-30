export default function Paginator({ currentPage, totalPages, onPageChange }) {
    if (totalPages <= 1) return null; // si solo hay una página, no mostrar nada

    return (
        <div className="flex justify-center items-center gap-4 mt-4">

            <button
                className="px-3 py-1 bg-gray-300 rounded disabled:bg-gray-200"
                disabled={currentPage === 1}
                onClick={() => onPageChange(currentPage - 1)}
            >
                ← Anterior
            </button>

            <span className="text-gray-600">
                Página {currentPage} de {totalPages}
            </span>

            <button
                className="px-3 py-1 bg-gray-300 rounded disabled:bg-gray-200"
                disabled={currentPage === totalPages}
                onClick={() => onPageChange(currentPage + 1)}
            >
                Siguiente →
            </button>

        </div>
    );
}
