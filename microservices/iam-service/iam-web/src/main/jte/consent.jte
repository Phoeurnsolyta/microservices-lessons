@param String clientId
@param String clientName
@param String state
@param java.util.Set<String> scopesToApprove
@param java.util.Set<String> previouslyApprovedScopes
@param String principalName
@param org.springframework.security.web.csrf.CsrfToken _csrf = null
@param kh.edu.istad.platform.iam.web.i18n.I18n msg

<!DOCTYPE html>
<html lang="${msg.getCurrentLang()}">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${msg.get("consent.title")} - ${msg.get("common.appName")}</title>
    <style>
        :root {
            --primary: #4f46e5;
            --primary-hover: #4338ca;
            --bg: #f1f5f9;
            --card-bg: #ffffff;
            --text-main: #0f172a;
            --text-muted: #64748b;
            --border: #cbd5e1;
        }

        * {
            box-sizing: border-box;
            margin: 0;
            padding: 0;
        }

        body {
            font-family: system-ui, -apple-system, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif;
            background: var(--bg);
            color: var(--text-main);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 1rem;
        }

        .card {
            background: var(--card-bg);
            border: 1px solid var(--border);
            border-radius: 12px;
            padding: 2.5rem;
            width: 100%;
            max-width: 480px;
            box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.05);
        }

        .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 1rem;
        }

        .header h1 {
            font-size: 1.35rem;
            color: var(--primary);
        }

        .lang-switch a {
            text-decoration: none;
            color: var(--text-muted);
            font-size: 0.8rem;
            padding: 0.2rem 0.4rem;
            border: 1px solid var(--border);
            border-radius: 4px;
        }

        .lang-switch a.active {
            background: var(--primary);
            color: #fff;
            border-color: var(--primary);
        }

        .client-info {
            margin-bottom: 1.5rem;
            font-size: 1.05rem;
        }

        .client-name {
            font-weight: 700;
            color: var(--primary);
        }

        .scopes-section {
            margin-bottom: 1.5rem;
        }

        .scopes-title {
            font-weight: 600;
            font-size: 0.95rem;
            margin-bottom: 0.75rem;
            color: var(--text-main);
        }

        .scope-item {
            display: flex;
            align-items: center;
            gap: 0.6rem;
            padding: 0.5rem 0.75rem;
            border: 1px solid var(--border);
            border-radius: 6px;
            margin-bottom: 0.5rem;
            background: #f8fafc;
        }

        .scope-item input[type="checkbox"] {
            width: 1.1rem;
            height: 1.1rem;
            accent-color: var(--primary);
        }

        .btn-row {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 1rem;
            margin-top: 1.5rem;
        }

        .btn {
            padding: 0.75rem;
            font-size: 0.95rem;
            font-weight: 600;
            border-radius: 6px;
            cursor: pointer;
            border: none;
        }

        .btn-allow {
            background: var(--primary);
            color: white;
        }

        .btn-allow:hover {
            background: var(--primary-hover);
        }

        .btn-cancel {
            background: #e2e8f0;
            color: var(--text-main);
        }

        .btn-cancel:hover {
            background: #cbd5e1;
        }
    </style>
</head>
<body>

<div class="card">
    <div class="header">
        <h1>${msg.get("consent.title")}</h1>
        <div class="lang-switch">
            <a href="/oauth2/consent?client_id=${clientId}&scope=${String.join(" ", scopesToApprove)}&state=${state}&lang=en"
               class="${"en".equals(msg.getCurrentLang()) ? "active" : ""}">EN</a>
            <a href="/oauth2/consent?client_id=${clientId}&scope=${String.join(" ", scopesToApprove)}&state=${state}&lang=km"
               class="${"km".equals(msg.getCurrentLang()) ? "active" : ""}">KM</a>
        </div>
    </div>

    <p class="client-info">
        ${msg.get("consent.clientWantsAccess", clientName != null ? clientName : clientId)}
    </p>

    <form action="/oauth2/authorize" method="post">
        @if(_csrf != null)
            <input type="hidden" name="${_csrf.getParameterName()}" value="${_csrf.getToken()}">
        @endif
        <input type="hidden" name="client_id" value="${clientId}">
        <input type="hidden" name="state" value="${state}">

        <div class="scopes-section">
            <div class="scopes-title">${msg.get("consent.scopesHeader")}</div>

            @if(scopesToApprove != null && !scopesToApprove.isEmpty())
                @for(var scope : scopesToApprove)
                    <div class="scope-item">
                        <input type="checkbox" name="scope" value="${scope}" id="${scope}" checked>
                        <label for="${scope}" style="font-weight: 500;">${scope}</label>
                    </div>
                @endfor
            @endif

            @if(previouslyApprovedScopes != null && !previouslyApprovedScopes.isEmpty())
                <div style="font-size: 0.85rem; color: var(--text-muted); margin-top: 1rem; margin-bottom: 0.5rem;">
                    Previously approved:
                </div>
                @for(var scope : previouslyApprovedScopes)
                    <div class="scope-item" style="opacity: 0.7;">
                        <input type="checkbox" name="scope" value="${scope}" id="prev_${scope}" checked disabled>
                        <label for="prev_${scope}">${scope} (approved)</label>
                    </div>
                @endfor
            @endif
        </div>

        <div class="btn-row">
            <button type="submit" name="consent_action" value="approve" class="btn btn-allow">
                ${msg.get("consent.allow")}
            </button>
            <button type="submit" name="consent_action" value="cancel" class="btn btn-cancel">
                ${msg.get("consent.cancel")}
            </button>
        </div>
    </form>
</div>

</body>
</html>