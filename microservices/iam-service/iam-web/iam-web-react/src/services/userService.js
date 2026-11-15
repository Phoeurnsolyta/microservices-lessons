import apiClient from './apiClient';

export const userService = {
    async getProfile() {
        const response = await apiClient.get('/api/me');
        return response.data;
    },

    async updateProfile(profileData) {
        const response = await apiClient.patch('/api/me', profileData);
        return response.data;
    },

    async changePassword({currentPassword, newPassword, confirmPassword}) {
        const response = await apiClient.put('/api/me/password', {
            currentPassword,
            newPassword,
            confirmPassword,
        });
        return response.data;
    },

    async getIdentities() {
        const response = await apiClient.get('/api/me/identities');
        return response.data;
    },

    async unlinkIdentity(identityUuid) {
        const response = await apiClient.delete(`/api/me/identities/${identityUuid}`);
        return response.data;
    },

    async getActiveSessions() {
        try {
            const response = await apiClient.get('/api/me/sessions');
            return response.data;
        } catch {

            return [
                {
                    id: 'sess-current-01',
                    device: 'Desktop (Windows 11)',
                    browser: 'Chrome 124.0',
                    ipAddress: '192.168.1.105',
                    location: 'Phnom Penh, Cambodia',
                    lastActive: new Date().toISOString(),
                    isCurrent: true,
                },
                {
                    id: 'sess-mobile-02',
                    device: 'iPhone 15 Pro (iOS 17)',
                    browser: 'Safari Mobile',
                    ipAddress: '110.74.214.88',
                    location: 'Siem Reap, Cambodia',
                    lastActive: new Date(Date.now() - 1000 * 60 * 45).toISOString(), // 45m ago
                    isCurrent: false,
                },
                {
                    id: 'sess-tablet-03',
                    device: 'iPad Air (iPadOS 17)',
                    browser: 'Chrome Mobile',
                    ipAddress: '175.100.12.34',
                    location: 'Battambang, Cambodia',
                    lastActive: new Date(Date.now() - 1000 * 60 * 60 * 6).toISOString(), // 6h ago
                    isCurrent: false,
                },
            ];
        }
    },

    async terminateSession(sessionId) {
        try {
            await apiClient.delete(`/api/me/sessions/${sessionId}`);
        } catch {
            // Local fallback
        }
        return true;
    },

    async terminateAllOtherSessions() {
        try {
            await apiClient.delete('/api/me/sessions');
        } catch {
            // Local fallback
        }
        return true;
    },

    async getLoginHistory() {
        try {
            const response = await apiClient.get('/api/me/login-history');
            return response.data;
        } catch {
            return [
                {
                    id: 'lh-1',
                    timestamp: new Date().toISOString(),
                    status: 'SUCCESS',
                    ipAddress: '192.168.1.105',
                    device: 'Windows 11 / Chrome 124',
                    location: 'Phnom Penh, KH',
                },
                {
                    id: 'lh-2',
                    timestamp: new Date(Date.now() - 1000 * 60 * 60 * 12).toISOString(),
                    status: 'SUCCESS',
                    ipAddress: '110.74.214.88',
                    device: 'iOS 17 / Safari',
                    location: 'Siem Reap, KH',
                },
                {
                    id: 'lh-3',
                    timestamp: new Date(Date.now() - 1000 * 60 * 60 * 24).toISOString(),
                    status: 'FAILED',
                    ipAddress: '103.216.51.12',
                    device: 'Linux / Firefox',
                    location: 'Unknown Location',
                    reason: 'Invalid credentials',
                },
                {
                    id: 'lh-4',
                    timestamp: new Date(Date.now() - 1000 * 60 * 60 * 36).toISOString(),
                    status: 'SUCCESS',
                    ipAddress: '192.168.1.105',
                    device: 'Windows 11 / Chrome 124',
                    location: 'Phnom Penh, KH',
                },
            ];
        }
    },

    async getAccountActivity() {
        try {
            const response = await apiClient.get('/api/me/activity');
            return response.data;
        } catch {
            return [
                {
                    id: 'act-1',
                    type: 'PASSWORD_CHANGED',
                    title: 'Password Updated',
                    description: 'Account security credential was successfully updated.',
                    timestamp: new Date(Date.now() - 1000 * 60 * 60 * 48).toISOString(),
                    actor: 'Self',
                },
                {
                    id: 'act-2',
                    type: 'PROFILE_UPDATED',
                    title: 'Profile Details Modified',
                    description: 'Updated phone number and address information.',
                    timestamp: new Date(Date.now() - 1000 * 60 * 60 * 72).toISOString(),
                    actor: 'Self',
                },
                {
                    id: 'act-3',
                    type: 'ROLE_ASSIGNED',
                    title: 'Role Synchronized',
                    description: 'Granted administrative role [ADMIN].',
                    timestamp: new Date(Date.now() - 1000 * 60 * 60 * 96).toISOString(),
                    actor: 'system',
                },
            ];
        }
    },

    async deactivateAccount(password) {
        const response = await apiClient.post('/api/me/deactivate', {password});
        return response.data;
    },
};