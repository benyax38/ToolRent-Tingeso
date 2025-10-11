import React, { useState } from "react";
import axios from "axios";

const AddToolForm = ({ userId }) => {
    const [formData, setFormData] = useState({
        toolName: "",
        toolCategory: "",
        rentalValue: "",
        replacementValue: "",
        description: "",
        availableUnits: "",
    });

    const [loading, setLoading] = useState(false);

    // Manejo de cambios en inputs
    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData((prev) => ({ ...prev, [name]: value}));
    };

    // Envío del formulario
    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);

        try {
            await axios.post(`/api/catalog?userId=${userId}`, formData);
            alert("Herramienta registrada exitosamente.");
            setFormData({
                toolName: "",
                toolCategory: "",
                rentalValue: "",
                replacementValue: "",
                description: "",
                availableUnits: "",
            });
        } catch (error) {
            console.error("Error al registrar herramienta:", error);
            alert("Ocurrió un error al registrar la herramienta.");
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="max-w-lg mx-auto bg-white p-6 rounded-x1 shadow-md">
            <h2 className="text-x1 font-semibold mb-4">Registrar nueva herramienta</h2>

            <form onSubmit={handleSubmit} className="space-y-4">
                <input
                    name="toolName"
                    type="text"
                    placeholder="Nombre de la herramienta"
                    value={formData.toolName}
                    onChange={handleChange}
                    required
                    className="w-full border p-2 rounded-lg"
                />

                <select
                    name="toolCategory"
                    value={formData.toolCategory}
                    onChange={handleChange}
                    required
                    className="w-full border p-2 rounded-lg"
                >
                    <option value="">Seleccionar categoría</option>
                    <option value="Manual">Manual</option>
                    <option value="Eléctrica">Eléctrica</option>
                    <option value="Pesada">Pesada</option>
                </select>

                <input
                    name="rentalValue"
                    type="number"
                    placeholder="Valor de arriendo"
                    value={formData.rentalValue}
                    onChange={handleChange}
                    required
                    className="w-full border p-2 rounded-lg"
                />

                <input
                    name="replacementValue"
                    type="number"
                    placeholder="Valor de reemplazo"
                    value={formData.replacementValue}
                    onChange={handleChange}
                    required
                    className="w-full border p-2 rounded-lg"
                />

                <textarea
                    name="description"
                    placeholder="Descripción"
                    value={formData.description}
                    onChange={handleChange}
                    className="w-full border p-2 rounded-lg"
                />

                <input
                    name="availableUnits"
                    type="number"
                    placeholder="Unidades disponibles"
                    value={formData.availableUnits}
                    onChange={handleChange}
                    required
                    className="w-full border p-2 rounded-lg"
                />

                <button
                    type="submit"
                    disabled={loading}
                    className={`w-full py-2 text-white rounded-lg ${
                        loading ? "bg-gray-400" : "bg-green-600 hover:bg-green-700"
                    }`}
                >
                    {loading ? "Registrando..." : "Registrar herramienta"}
                </button>
            </form>
        </div>
    );
};

export default AddToolForm;