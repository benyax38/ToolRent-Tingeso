import React, { useState } from "react";
import toolService from "./toolService";

function AddToolForm() {
  const [formData, setFormData] = useState({
    toolName: "",
    toolCategory: "",
    rentalValue: "",
    replacementValue: "",
    description: "",
    availableUnits: "",
  });
  const [loading, setLoading] = useState(false);
  const userId = 1; // o el id dinámico del usuario logueado

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      await toolService.registerTool(formData, userId);
      alert("Herramienta registrada exitosamente.");
      setFormData({
        toolName: "",
        toolCategory: "",
        rentalValue: "",
        replacementValue: "",
        description: "",
        availableUnits: "",
      });
    } catch (err) {
      alert("Ocurrió un error al registrar la herramienta.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <form onSubmit={handleSubmit} style={{ display: "flex", flexDirection: "column", gap: "10px", width: "300px" }}>
      <input
        type="text"
        placeholder="Nombre de la herramienta"
        value={formData.toolName}
        onChange={(e) => setFormData({ ...formData, toolName: e.target.value })}
      />

      <input
        type="text"
        placeholder="Categoría"
        value={formData.toolCategory}
        onChange={(e) => setFormData({ ...formData, toolCategory: e.target.value })}
      />

      <input
        type="text"
        placeholder="Valor de arriendo"
        value={formData.rentalValue}
        onChange={(e) => setFormData({ ...formData, rentalValue: e.target.value })}
      />

      <input
        type="text"
        placeholder="Valor de reemplazo"
        value={formData.replacementValue}
        onChange={(e) => setFormData({ ...formData, replacementValue: e.target.value })}
      />

      <input
        type="text"
        placeholder="Descripción"
        value={formData.description}
        onChange={(e) => setFormData({ ...formData, description: e.target.value })}
      />

      <input
        type="text"
        placeholder="Unidades disponibles"
        value={formData.availableUnits}
        onChange={(e) => setFormData({ ...formData, availableUnits: e.target.value })}
      />

      <button type="submit" disabled={loading}>
        {loading ? "Registrando..." : "Registrar herramienta"}
      </button>
    </form>
  );
}

export default AddToolForm;
