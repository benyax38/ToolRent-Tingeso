import AddToolForm from "./AddToolForm";

const ToolManagement = () => {
    const userId = 1; // puedes obtenerlo del contexto, JWT o LocalStorage

    return (
        <div className="p-6">
            <AddToolForm userId={userId} />
        </div>
    );
};

export default ToolManagement;