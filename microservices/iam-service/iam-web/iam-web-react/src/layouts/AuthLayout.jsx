import {Outlet} from 'react-router-dom';
import LanguageSwitcher from '../components/common/LanguageSwitcher';

export default function AuthLayout() {
    return (
        <div
            className="min-h-screen bg-[#F5F5F7] text-slate-800 relative flex flex-col justify-center items-center py-12 px-4 sm:px-6 lg:px-8 overflow-hidden select-none">
            {/* Soft Background Rings */}
            <div
                className="absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 w-[700px] h-[700px] rounded-full border border-slate-200/60 pointer-events-none"/>
            <div
                className="absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 w-[920px] h-[920px] rounded-full border border-slate-200/40 pointer-events-none"/>

            {/* Floating Sparkle Stars */}
            <div className="absolute top-16 left-12 text-slate-300 text-lg">✦</div>
            <div className="absolute top-36 right-20 text-slate-300 text-sm">✧</div>
            <div className="absolute bottom-24 left-24 text-slate-300 text-base">✦</div>
            <div className="absolute bottom-32 right-16 text-slate-300 text-lg">✧</div>

            {/* Top Floating Language Switcher */}
            <div className="absolute top-6 right-6 z-20">
                <div className="bg-white rounded-full border border-slate-200 p-0.5 shadow-sm">
                    <LanguageSwitcher/>
                </div>
            </div>

            {/* Main Card Slot - Made Bigger (480px) */}
            <div className="w-full max-w-[480px] relative z-10">
                <Outlet/>
            </div>
        </div>
    );
}