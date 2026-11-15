import {BrowserRouter} from 'react-router-dom';
import {QueryClient, QueryClientProvider} from '@tanstack/react-query';
import AppRoutes from './routes/AppRoutes';
import './i18n';

const queryClient = new QueryClient({
    defaultOptions: {
        queries: {
            staleTime: 1000 * 60 * 3,
            refetchOnWindowFocus: false,
            retry: 1,
        },
    },
});

export default function App() {
    return (
        <QueryClientProvider client={queryClient}>
            <BrowserRouter>
                <div className="min-h-screen bg-slate-50 text-slate-800">
                    <AppRoutes/>
                </div>
            </BrowserRouter>
        </QueryClientProvider>
    );
}