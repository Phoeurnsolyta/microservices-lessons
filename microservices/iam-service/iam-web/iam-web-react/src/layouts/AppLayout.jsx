import {Outlet} from 'react-router-dom';
import Navbar from '../components/layout/Navbar';
import Sidebar from '../components/layout/Sidebar';

export default function AppLayout() {
    return (
        <div className="min-h-screen bg-slate-50 flex flex-col">
            <Navbar/>
            <div className="flex-1 flex max-w-7xl w-full mx-auto">
                <Sidebar/>
                <main className="flex-1 p-6 lg:p-8 overflow-y-auto">
                    <Outlet/>
                </main>
            </div>
        </div>
    );
}