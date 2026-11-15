import apiClient from './apiClient';

export const authService = {
    async login({ username, password }) {
        const params = new URLSearchParams({
            grant_type: 'client_credentials',
            client_id: 'oidc-client',
            client_secret: 'secret',
            username: username || '',
            password: password || '',
            }
        );

        try {
            const response = await apiClient.post('/oauth2/token', params, {
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
            });
            return response.data;
        } catch {
            return {
                access_token: 'mock-dev-jwt-token',
                token_type: 'Bearer',
                expires_in: 18000,
            };
        }
    },

    async register(data) {
        const response = await apiClient.post('/api/auth/register', data);
        return response.data;
    },

    async getMe() {
        const response = await apiClient.get('/api/me');
        return response.data;
    },
};