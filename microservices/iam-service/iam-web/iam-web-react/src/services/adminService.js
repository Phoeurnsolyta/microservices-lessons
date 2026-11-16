import apiClient from './apiClient';

export const adminService = {
    async getUsers({search = '', enabled, page = 0, size = 20, sort = 'createdAt,desc'} = {}) {
        const params = new URLSearchParams();
        if (search) params.append('search', search);
        if (enabled !== undefined && enabled !== '') params.append('enabled', enabled);
        params.append('page', page);
        params.append('size', size);
        params.append('sort', sort);

        const response = await apiClient.get(`/api/users?${params.toString()}`);
        return response.data;
    },

    async getUser(uuid) {
        const response = await apiClient.get(`/api/users/${uuid}`);
        return response.data;
    },

    async createUser(userData) {
        const response = await apiClient.post('/api/users', userData);
        return response.data;
    },

    async updateUser(uuid, userData) {
        const response = await apiClient.patch(`/api/users/${uuid}`, userData);
        return response.data;
    },

    async deleteUser(uuid) {
        await apiClient.delete(`/api/users/${uuid}`);
        return true;
    },

    async enableUser(uuid) {
        await apiClient.post(`/api/users/${uuid}/enable`);
        return true;
    },

    async disableUser(uuid) {
        await apiClient.post(`/api/users/${uuid}/disable`);
        return true;
    },

    async lockUser(uuid) {
        await apiClient.post(`/api/users/${uuid}/lock`);
        return true;
    },

    async unlockUser(uuid) {
        await apiClient.post(`/api/users/${uuid}/unlock`);
        return true;
    },

    async resetUserPassword(uuid, newPassword) {
        await apiClient.put(`/api/users/${uuid}/password`, {newPassword});
        return true;
    },

    async getUserAuthorities(uuid) {
        const response = await apiClient.get(`/api/users/${uuid}/authorities`);
        return response.data;
    },

    async assignRolesToUser(uuid, {roleUuids, expiresAt, reason}) {
        const response = await apiClient.post(`/api/users/${uuid}/roles`, {
            roleUuids,
            expiresAt: expiresAt || null,
            reason: reason || 'Assigned via Admin Management Console',
        });
        return response.data;
    },

    async revokeRoleFromUser(uuid, roleUuid, reason) {
        await apiClient.delete(`/api/users/${uuid}/roles/${roleUuid}`, {
            params: {reason: reason || 'Revoked via Admin Console'},
        });
        return true;
    },

    async grantPermissionsToUser(uuid, {permissionUuids, effect = 'GRANT', expiresAt, reason}) {
        const response = await apiClient.post(`/api/users/${uuid}/permissions`, {
            permissionUuids,
            effect,
            expiresAt: expiresAt || null,
            reason: reason || 'Direct grant via Admin Management Console',
        });
        return response.data;
    },

    async revokePermissionFromUser(uuid, permissionUuid, reason) {
        await apiClient.delete(`/api/users/${uuid}/permissions/${permissionUuid}`, {
            params: {reason: reason || 'Revoked via Admin Console'},
        });
        return true;
    },

    async getRoles({search = '', active, page = 0, size = 50, sort = 'name,asc'} = {}) {
        const params = new URLSearchParams();
        if (search) params.append('search', search);
        if (active !== undefined && active !== '') params.append('active', active);
        params.append('page', page);
        params.append('size', size);
        params.append('sort', sort);

        const response = await apiClient.get(`/api/roles?${params.toString()}`);
        return response.data;
    },

    async getRole(uuid) {
        const response = await apiClient.get(`/api/roles/${uuid}`);
        return response.data;
    },

    async createRole({name, description, permissionUuids = []}) {
        const response = await apiClient.post('/api/roles', {name, description, permissionUuids});
        return response.data;
    },

    async updateRole(uuid, {description}) {
        const response = await apiClient.patch(`/api/roles/${uuid}`, {description});
        return response.data;
    },

    async deleteRole(uuid) {
        await apiClient.delete(`/api/roles/${uuid}`);
        return true;
    },

    async activateRole(uuid) {
        await apiClient.post(`/api/roles/${uuid}/activate`);
        return true;
    },

    async deactivateRole(uuid) {
        await apiClient.post(`/api/roles/${uuid}/deactivate`);
        return true;
    },

    async assignPermissionsToRole(uuid, {permissionUuids, expiresAt, reason}) {
        const response = await apiClient.post(`/api/roles/${uuid}/permissions`, {
            permissionUuids,
            expiresAt: expiresAt || null,
            reason: reason || 'Assigned via Admin Console',
        });
        return response.data;
    },

    async revokePermissionFromRole(uuid, permissionUuid, reason) {
        await apiClient.delete(`/api/roles/${uuid}/permissions/${permissionUuid}`, {
            params: {reason: reason || 'Revoked via Admin Console'},
        });
        return true;
    },

    async getPermissions({resource = '', search = '', page = 0, size = 100, sort = 'name,asc'} = {}) {
        const params = new URLSearchParams();
        if (resource) params.append('resource', resource);
        if (search) params.append('search', search);
        params.append('page', page);
        params.append('size', size);
        params.append('sort', sort);

        const response = await apiClient.get(`/api/permissions?${params.toString()}`);
        return response.data;
    },

    async createPermission({resource, action, description}) {
        const response = await apiClient.post('/api/permissions', {resource, action, description});
        return response.data;
    },

    async updatePermission(uuid, {description}) {
        const response = await apiClient.patch(`/api/permissions/${uuid}`, {description});
        return response.data;
    },

    async deletePermission(uuid) {
        await apiClient.delete(`/api/permissions/${uuid}`);
        return true;
    },

    async activatePermission(uuid) {
        await apiClient.post(`/api/permissions/${uuid}/activate`);
        return true;
    },

    async deactivatePermission(uuid) {
        await apiClient.post(`/api/permissions/${uuid}/deactivate`);
        return true;
    },

    async getClients() {
        try {
            const response = await apiClient.get('/api/clients');
            return response.data;
        } catch {
            const saved = localStorage.getItem('istad_clients');
            return saved ? JSON.parse(saved) : [];
        }
    },

    async registerClient(clientData) {
        try {
            const response = await apiClient.post('/api/clients', clientData);
            return response.data;
        } catch {
            const clients = await this.getClients();
            const newClient = {
                ...clientData,
                id: `client-${Date.now()}`,
                createdAt: new Date().toISOString(),
            };
            const updated = [newClient, ...clients];
            localStorage.setItem('istad_clients', JSON.stringify(updated));
            return newClient;
        }
    },

    async deleteClient(id) {
        try {
            await apiClient.delete(`/api/clients/${id}`);
        } catch {
            const clients = await this.getClients();
            const updated = clients.filter((c) => c.id !== id && c.clientId !== id);
            localStorage.setItem('istad_clients', JSON.stringify(updated));
        }
        return true;
    },

    async getAuditLogs({eventType = '', search = ''} = {}) {
        const params = new URLSearchParams();
        if (eventType) params.append('eventType', eventType);
        if (search) params.append('search', search);

        const query = params.toString() ? `?${params.toString()}` : '';
        const response = await apiClient.get(`/api/audit${query}`);
        return response.data;
    },

    async getUserLoginHistory(uuid) {
        const response = await apiClient.get(`/api/users/${uuid}/login-history`);
        return response.data;
    },

    getSystemSettings() {
        const saved = localStorage.getItem('istad_system_settings');
        if (saved) return JSON.parse(saved);
        return {
            accessTokenTtlMinutes: 15,
            refreshTokenTtlDays: 30,
            authCodeTtlMinutes: 5,
            maxFailedAttempts: 5,
            lockoutDurationMinutes: 15,
            minPasswordLength: 8,
            requireUppercase: true,
            requireSpecialChar: true,
            requireNumber: true,
            allowRegistration: true,
        };
    },

    saveSystemSettings(settings) {
        localStorage.setItem('istad_system_settings', JSON.stringify(settings));
        return true;
    },
};