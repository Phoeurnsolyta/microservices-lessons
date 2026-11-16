import {Outlet} from 'react-router-dom';
import Navbar from '../components/layout/Navbar';
import Sidebar from '../components/layout/Sidebar';

export default function AppLayout() {
    return (
        <div className="min-h-screen bg-[#f7f8fa] flex flex-col">
            <Navbar/>
            <div className="flex-1 flex items-start max-w-7xl w-full mx-auto px-4 sm:px-6 lg:px-8 gap-6">
                <Sidebar/>
                <main className="flex-1 py-4 min-w-0">
                    <Outlet/>
                </main>
            </div>
        </div>
    );
}