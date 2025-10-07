import Dashboard from "../../components/pruebas/Dashboard";
import UserManagement from "../../components/pruebas/UserManagement";
import Reports from "../../components/pruebas/Reports";
import Tasks from "../../components/pruebas/Tasks";
import { getRole } from "../../utils/auth";
import { roleAccess } from "../../utils/roleAccess";

function Home() {
    const role = getRole();

    const hasAccess = (component) => roleAccess[role]?.includes(component);

    return (
        <div>
            <Dashboard />
            {hasAccess("UserManagement") && <UserManagement />}
            {hasAccess("Reports") && <Reports />}
            {hasAccess("Tasks") && <Tasks />}
        </div>
    );
}

export default Home;