@param String clientId
@param String clientName = null
@param String state = null
@param java.util.Set<String> scopesToApprove = null
@param java.util.Set<String> previouslyApprovedScopes = null
@param String principalName = null
@param String userCode = null
@param org.springframework.security.web.csrf.CsrfToken _csrf = null
@param kh.edu.istad.platform.iam.web.i18n.I18n msg

<!DOCTYPE html>
<html lang="${msg.getCurrentLang()}">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${msg.get("consent.title")} - ${msg.get("common.appName")}</title>

    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&family=Kantumruy+Pro:wght@400;500;600;700&display=swap" rel="stylesheet">
    <script src="https://cdn.tailwindcss.com"></script>
    <script>
        tailwind.config = {
            theme: {
                extend: {
                    fontFamily: {
                        sans: ['"Kantumruy Pro"', 'Inter', 'sans-serif'],
                    }
                }
            }
        }
    </script>
</head>
<body class="bg-[#f0f3ee] text-[#2d3b31] min-h-screen flex items-center justify-center p-4">

<div class="w-full max-w-md bg-[#fafbf9] rounded-[36px] shadow-2xl shadow-slate-300/40 p-8 sm:p-10 border border-[#e5ebe4]">

    <!-- Language Switcher -->
    <div class="flex justify-end mb-2">
        <div class="inline-flex items-center bg-[#dde3dc]/60 p-1 rounded-full text-xs font-semibold">
            <a href="?client_id=${clientId}&scope=${scopesToApprove != null ? String.join(" ", scopesToApprove) : ""}&state=${state}&lang=en"
               class="px-3 py-1 rounded-full ${"en".equals(msg.getCurrentLang()) ? "bg-[#5e7c65] text-white shadow-sm" : "text-[#2d3b31]"}">EN</a>
            <a href="?client_id=${clientId}&scope=${scopesToApprove != null ? String.join(" ", scopesToApprove) : ""}&state=${state}&lang=km"
               class="px-3 py-1 rounded-full ${"km".equals(msg.getCurrentLang()) ? "bg-[#5e7c65] text-white shadow-sm" : "text-[#2d3b31]"}">KM</a>
        </div>
    </div>

    <!-- Lock Icon -->
    <div class="w-14 h-14 mx-auto mb-3 flex items-center justify-center text-[#4c6552]">
        <svg class="w-11 h-11" viewBox="0 0 24 24" fill="currentColor">
            <path fill-rule="evenodd" d="M12 1.5a5.25 5.25 0 00-5.25 5.25v3a3 3 0 00-3 3v6.75a3 3 0 003 3h10.5a3 3 0 003-3v-6.75a3 3 0 00-3-3v-3c0-2.9-2.35-5.25-5.25-5.25zm3.75 8.25v-3a3.75 3.75 0 00-7.5 0v3h7.5z" clip-rule="evenodd"/>
        </svg>
    </div>

    <div class="text-center mb-6">
        <h1 class="text-2xl font-bold text-[#2d3b31]">${msg.get("consent.title")}</h1>
        <p class="text-xs text-[#718575] mt-1.5">
            ${msg.get("consent.clientWantsAccess", clientName != null ? clientName : clientId)}
        </p>
    </div>

    <form action="/oauth2/authorize" method="post" class="space-y-6">
        @if(_csrf != null)
            <input type="hidden" name="${_csrf.getParameterName()}" value="${_csrf.getToken()}"/>
        @endif
        <input type="hidden" name="client_id" value="${clientId}"/>
        <input type="hidden" name="state" value="${state}"/>

        <!-- Scopes list -->
        <div class="space-y-2">
            <span class="text-xs font-semibold text-[#718575] block">${msg.get("consent.scopesHeader")}</span>
            @if(scopesToApprove != null && !scopesToApprove.isEmpty())
                @for(String scope : scopesToApprove)
                    <label class="flex items-center gap-3 p-3.5 rounded-2xl bg-[#dde3dc]/50 cursor-pointer">
                        <input type="checkbox" name="scope" value="${scope}" checked
                               class="w-4 h-4 rounded text-[#5e7c65] border-[#c5d0c4] focus:ring-[#5e7c65]"/>
                        <span class="text-xs font-semibold text-[#2d3b31]">${scope}</span>
                    </label>
                @endfor
            @endif

            @if(previouslyApprovedScopes != null && !previouslyApprovedScopes.isEmpty())
                <div class="text-[11px] text-[#718575] pt-2">Previously approved:</div>
                @for(String scope : previouslyApprovedScopes)
                    <div class="flex items-center gap-3 p-3 rounded-2xl bg-[#dde3dc]/25 opacity-70">
                        <input type="checkbox" checked disabled class="w-4 h-4 rounded text-[#5e7c65]"/>
                        <span class="text-xs text-[#2d3b31]">${scope} (approved)</span>
                    </div>
                @endfor
            @endif
        </div>

        <!-- Buttons -->
        <div class="flex gap-3 pt-2">
            <button type="submit" name="consent_action" value="cancel"
                    class="w-1/2 py-3 border border-[#dde3dc] hover:bg-[#dde3dc]/50 text-xs font-semibold text-[#718575] rounded-full transition-all">
                ${msg.get("consent.cancel")}
            </button>
            <button type="submit" name="consent_action" value="approve"
                    class="w-1/2 py-3 bg-[#5e7c65] hover:bg-[#4e6b55] text-xs font-semibold text-white rounded-full shadow-md transition-all">
                ${msg.get("consent.allow")}
            </button>
        </div>
    </form>
</div>
</body>
</html>