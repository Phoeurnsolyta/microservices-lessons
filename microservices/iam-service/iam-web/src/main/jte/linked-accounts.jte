@param java.util.List<kh.edu.istad.platform.iam.domain.dto.IdentityResult> identities
@param String successMessage = null
@param String errorMessage = null
@param String activeTab = "linkedAccounts"
@param org.springframework.security.web.csrf.CsrfToken _csrf = null
@param kh.edu.istad.platform.iam.web.i18n.I18n msg

<!DOCTYPE html>
<html lang="${msg.getCurrentLang()}">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${msg.get("linkedAccounts.title")} - ${msg.get("common.appName")}</title>
    <style>
        :root {
            --primary: #4f46e5;
            --primary-hover: #4338ca;
            --bg: #f8fafc;
            --card-bg: #ffffff;
            --text-main: #0f172a;
            --text-muted: #64748b;
            --border: #e2e8f0;
            --danger: #ef4444;
            --success: #22c55e;
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

        .card {
            background: var(--card-bg);
            border: 1px solid var(--border);
            border-radius: 8px;
            padding: 1.5rem;
            box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
        }

        table {
            width: 100%;
            border-collapse: collapse;
            text-align: left;
        }

        th, td {
            padding: 0.75rem 1rem;
            border-bottom: 1px solid var(--border);
            font-size: 0.9rem;
        }

        th {
            color: var(--text-muted);
            font-weight: 600;
        }

        .btn-unlink {
            background: #fee2e2;
            color: #991b1b;
            border: 1px solid #fca5a5;
            padding: 0.35rem 0.75rem;
            border-radius: 4px;
            cursor: pointer;
            font-size: 0.85rem;
            font-weight: 500;
        }

        .btn-unlink:hover {
            background: #fecaca;
        }

        .alert {
            padding: 0.75rem 1rem;
            border-radius: 6px;
            font-size: 0.875rem;
            margin-bottom: 1.25rem;
        }

        .alert-success {
            background: #dcfce7;
            color: #166534;
        }

        .alert-danger {
            background: #fee2e2;
            color: #991b1b;
        }

        .lang-switch a {
            text-decoration: none;
            color: var(--text-muted);
            padding: 0.25rem 0.5rem;
            border-radius: 4px;
            border: 1px solid var(--border);
            font-size: 0.85rem;
        }

        .lang-switch a.active {
            background: var(--primary);
            color: white;
            border-color: var(--primary);
        }
    </style>
</head>
<body>

<div class="container">
    <div class="header">
        <div>
            <h1 style="color: var(--primary); font-size: 1.75rem;">${msg.get("linkedAccounts.title")}</h1>
            <p style="color: var(--text-muted); font-size: 0.9rem;">${msg.get("linkedAccounts.subtitle")}</p>
        </div>
        <div style="display: flex; gap: 1rem; align-items: center;">
            <div class="lang-switch">
                <a href="/profile/accounts?lang=en" class="${"en".equals(msg.getCurrentLang()) ? "active" : ""}">EN</a>
                <a href="/profile/accounts?lang=km" class="${"km".equals(msg.getCurrentLang()) ? "active" : ""}">KM</a>
            </div>
            <a href="/"
               style="text-decoration: none; color: var(--text-muted); font-size: 0.9rem;">${msg.get("common.back")}</a>
        </div>
    </div>

    <div class="nav-tabs">
        <a href="/" class="nav-tab">${msg.get("nav.overview")}</a>
        <a href="/profile/edit" class="nav-tab">${msg.get("nav.editProfile")}</a>
        <a href="/profile/password" class="nav-tab">${msg.get("nav.changePassword")}</a>
        <a href="/profile/accounts" class="nav-tab active">${msg.get("nav.linkedAccounts")}</a>
        <a href="/profile/apps" class="nav-tab">${msg.get("nav.connectedApps")}</a>
    </div>

    @if(successMessage != null)
        <div class="alert alert-success">${successMessage}</div>
    @endif
    @if(errorMessage != null)
        <div class="alert alert-danger">${errorMessage}</div>
    @endif

    <div class="card">
        @if(identities != null && !identities.isEmpty())
            <table>
                <thead>
                <tr>
                    <th>${msg.get("linkedAccounts.provider")}</th>
                    <th>${msg.get("linkedAccounts.displayName")}</th>
                    <th>${msg.get("register.email")}</th>
                    <th>${msg.get("linkedAccounts.linkedAt")}</th>
                    <th>${msg.get("common.actions")}</th>
                </tr>
                </thead>
                <tbody>
                @for(var id : identities)
                    <tr>
                        <td><strong>${id.provider()}</strong></td>
                        <td>${id.displayName() != null ? id.displayName() : "-"}</td>
                        <td>${id.email() != null ? id.email() : "-"}</td>
                        <td style="color: var(--text-muted); font-size: 0.8rem;">${id.linkedAt() != null ? id.linkedAt().toString() : "-"}</td>
                        <td>
                            <form action="/profile/accounts/${id.uuid().toString()}/unlink" method="post"
                                  style="display:inline;"
                                  onsubmit="return confirm('${msg.get("linkedAccounts.unlinkConfirm")}');">
                                @if(_csrf != null)
                                    <input type="hidden" name="${_csrf.getParameterName()}" value="${_csrf.getToken()}">
                                @endif
                                <button type="submit" class="btn-unlink">${msg.get("linkedAccounts.unlink")}</button>
                            </form>
                        </td>
                    </tr>
                @endfor
                </tbody>
            </table>
        @else
            <p style="color: var(--text-muted);">${msg.get("linkedAccounts.noAccounts")}</p>
        @endif
    </div>
</div>

</body>
</html>