import {Outlet} from 'react-router-dom';
import {Shield} from 'lucide-react';
import LanguageSwitcher from '../components/common/LanguageSwitcher';

export default function AuthLayout() {
    return (
        <div
            className="min-h-screen bg-gradient-to-br from-slate-50 via-primary-50/20 to-slate-100 flex flex-col justify-center py-12 sm:px-6 lg:px-8">
            {/* Top Language Switcher */}
            <div className="absolute top-6 right-6">
                <LanguageSwitcher/>
            </div>

            <div className="sm:mx-auto sm:w-full sm:max-w-md text-center">
                <div
                    className="inline-flex items-center justify-center w-14 h-14 rounded-2xl bg-primary-600 text-white shadow-md mb-4">
                    <Shield className="w-8 h-8"/>
                </div>
                <h1 className="text-2xl font-bold tracking-tight text-slate-900">
                    ISTAD IAM PLATFORM
                </h1>
                <p className="mt-1 text-sm text-slate-500">
                    Central Identity & Access Management Authority
                </p>
            </div>

            <div className="mt-8 sm:mx-auto sm:w-full sm:max-w-md px-4 sm:px-0">
                <div className="bg-white py-8 px-6 shadow-sm border border-slate-200 rounded-2xl sm:px-10">
                    <Outlet/>
                </div>
            </div>
        </div>
    );
}