@param kh.edu.istad.platform.iam.domain.dto.UserResult user
@param kh.edu.istad.platform.iam.domain.dto.UserAuthoritiesResult authorities
@param String activeTab = "overview"
@param org.springframework.security.web.csrf.CsrfToken _csrf = null
@param kh.edu.istad.platform.iam.web.i18n.I18n msg

<!DOCTYPE html>
<html lang="${msg.getCurrentLang()}">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${msg.get("profile.title")} - ${msg.get("common.appName")}</title>
    <style>
        :root {
            --primary: #4f46e5;
            --primary-hover: #4338ca;
            --bg: #f8fafc;
            --card-bg: #ffffff;
            --text-main: #0f172a;
            --text-muted: #64748b;
            --border: #e2e8f0;
            --tag-bg: #e0e7ff;
            --tag-text: #3730a3;
        }

        * {
            box-sizing: border-box;
            margin: 0;
            padding: 0;
        }

        body {
            font-family: system-ui, -apple-system, sans-serif;
            background-color: var(--bg);
            color: var(--text-main);
            line-height: 1.5;
            padding: 2rem 1rem;
        }

        .container {
            max-width: 900px;
            margin: 0 auto;
        }

        .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 1.5rem;
            padding-bottom: 1rem;
            border-bottom: 1px solid var(--border);
        }

        .header h1 {
            font-size: 1.75rem;
            color: var(--primary);
        }

        .header-actions {
            display: flex;
            gap: 1rem;
            align-items: center;
        }

        .lang-switch {
            display: flex;
            gap: 0.5rem;
            font-size: 0.875rem;
        }

        .lang-switch a {
            text-decoration: none;
            color: var(--text-muted);
            padding: 0.25rem 0.5rem;
            border-radius: 4px;
            border: 1px solid var(--border);
        }

        .lang-switch a.active {
            background: var(--primary);
            color: #fff;
            border-color: var(--primary);
        }

        .btn-logout {
            background: #ef4444;
            color: white;
            border: none;
            padding: 0.5rem 1rem;
            border-radius: 6px;
            cursor: pointer;
            font-size: 0.875rem;
            font-weight: 500;
        }

        .btn-logout:hover {
            background: #dc2626;
        }

        .nav-tabs {
            display: flex;
            gap: 0.5rem;
            margin-bottom: 2rem;
            border-bottom: 1px solid var(--border);
            padding-bottom: 0.5rem;
            flex-wrap: wrap;
        }

        .nav-tab {
            text-decoration: none;
            color: var(--text-muted);
            padding: 0.5rem 1rem;
            border-radius: 6px;
            font-size: 0.9rem;
            font-weight: 500;
        }

        .nav-tab.active {
            background: var(--primary);
            color: white;
        }

        .grid {
            display: grid;
            grid-template-columns: 1fr;
            gap: 1.5rem;
        }

        @media (min-width: 768px) {
            .grid {
                grid-template-columns: repeat(2, 1fr);
            }

            .grid-full {
                grid-column: span 2;
            }
        }

        .card {
            background: var(--card-bg);
            border: 1px solid var(--border);
            border-radius: 8px;
            padding: 1.5rem;
            box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
        }

        .card h2 {
            font-size: 1.15rem;
            margin-bottom: 1rem;
            color: var(--text-main);
            border-bottom: 1px solid var(--border);
            padding-bottom: 0.5rem;
        }

        .field {
            margin-bottom: 0.75rem;
            display: flex;
            justify-content: space-between;
            font-size: 0.925rem;
        }

        .field-label {
            color: var(--text-muted);
            font-weight: 500;
        }

        .field-val {
            font-weight: 600;
            text-align: right;
        }

        .tag-list {
            display: flex;
            flex-wrap: wrap;
            gap: 0.5rem;
        }

        .tag {
            background: var(--tag-bg);
            color: var(--tag-text);
            padding: 0.25rem 0.65rem;
            border-radius: 9999px;
            font-size: 0.8rem;
            font-weight: 600;
        }

        .tag-perm {
            background: #dcfce7;
            color: #166534;
        }
    </style>
</head>
<body>

<div class="container">
    <div class="header">
        <div>
            <h1>${msg.get("profile.title")}</h1>
            <p style="color: var(--text-muted); font-size: 0.9rem;">${msg.get("profile.subtitle")}</p>
        </div>
        <div class="header-actions">
            <div class="lang-switch">
                <a href="/?lang=en" class="${"en".equals(msg.getCurrentLang()) ? "active" : ""}">EN</a>
                <a href="/?lang=km" class="${"km".equals(msg.getCurrentLang()) ? "active" : ""}">KM</a>
            </div>
            <form action="/logout" method="post" style="display:inline;">
                @if(_csrf != null)
                    <input type="hidden" name="${_csrf.getParameterName()}" value="${_csrf.getToken()}">
                @endif
                <button type="submit" class="btn-logout">${msg.get("common.logout")}</button>
            </form>
        </div>
    </div>

    <!-- Navigation Tabs -->
    <div class="nav-tabs">
        <a href="/" class="nav-tab active">${msg.get("nav.overview")}</a>
        <a href="/profile/edit" class="nav-tab">${msg.get("nav.editProfile")}</a>
        <a href="/profile/password" class="nav-tab">${msg.get("nav.changePassword")}</a>
        <a href="/profile/accounts" class="nav-tab">${msg.get("nav.linkedAccounts")}</a>
        <a href="/profile/apps" class="nav-tab">${msg.get("nav.connectedApps")}</a>
    </div>

    @if(user != null)
        <div class="grid">
            <!-- User Information / OIDC Claims -->
            <div class="card">
                <h2>${msg.get("profile.claims")}</h2>
                <div class="field">
                    <span class="field-label">sub (UUID)</span>
                    <span class="field-val"
                          style="font-family: monospace; font-size: 0.8rem;">${user.uuid().toString()}</span>
                </div>
                <div class="field">
                    <span class="field-label">${msg.get("register.username")}</span>
                    <span class="field-val">${user.username()}</span>
                </div>
                <div class="field">
                    <span class="field-label">${msg.get("register.email")}</span>
                    <span class="field-val">${user.email()} (${user.emailVerified() ? "✓" : "✗"})</span>
                </div>
                <div class="field">
                    <span class="field-label">name</span>
                    <span class="field-val">${user.name()}</span>
                </div>
                <div class="field">
                    <span class="field-label">locale</span>
                    <span class="field-val">${user.locale()}</span>
                </div>
            </div>

            <!-- Roles -->
            <div class="card">
                <h2>${msg.get("profile.roles")}</h2>
                <div class="tag-list">
                    @if(authorities != null && authorities.roles() != null && !authorities.roles().isEmpty())
                        @for(var role : authorities.roles())
                            <span class="tag">ROLE_${role.name()}</span>
                        @endfor
                    @else
                        <span style="color: var(--text-muted); font-size: 0.875rem;">No roles assigned</span>
                    @endif
                </div>
            </div>

            <!-- Effective Permissions -->
            <div class="card grid-full">
                <h2>${msg.get("profile.permissions")}</h2>
                <div class="tag-list">
                    @if(authorities != null && authorities.effectivePermissions() != null && !authorities.effectivePermissions().isEmpty())
                        @for(var perm : authorities.effectivePermissions())
                            <span class="tag tag-perm">${perm}</span>
                        @endfor
                    @else
                        <span style="color: var(--text-muted); font-size: 0.875rem;">No permissions granted</span>
                    @endif
                </div>
            </div>
        </div>
    @endif
</div>

</body>
</html>