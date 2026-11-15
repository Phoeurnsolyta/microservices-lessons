import axios from 'axios';
import { useAuthStore } from '../store/useAuthStore';

const apiClient = axios.create({
    baseURL: '',
    headers: {
        'Content-Type': 'application/json',
    },
    timeout: 15000,
});

apiClient.interceptors.request.use(
    (config) => {
        const token = useAuthStore.getState().accessToken;
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    (error) => Promise.reject(error)
);

apiClient.interceptors.response.use(
    (response) => response,
    (error) => {
        const status = error.response ? error.response.status : null;

        if (status === 401) {
            useAuthStore.getState().logout();
        }

        let errorMessage = 'An unexpected error occurred. Please try again.';
        if (error.response?.data) {
            const data = error.response.data;
            // Spring RFC 9457 typically uses: detail, title, or custom error message
            errorMessage = data.detail || data.title || data.message || errorMessage;
        } else if (error.message) {
            errorMessage = error.message;
        }

        return Promise.reject(new Error(errorMessage));
    }
);

export default apiClient;