@param String clientId
@param String clientName
@param String state
@param java.util.Set<String> scopes
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

    <!-- Lotus Icon -->
    <div class="w-14 h-14 mx-auto mb-4 flex items-center justify-center text-[#4c6552]">
        <svg class="w-12 h-12" viewBox="0 0 24 24" fill="currentColor">
            <path d="M12 3c-1.5 3-2 5.5-2 8 0 2.5 1 4 2 5 1-1 2-2.5 2-5 0-2.5-.5-5-2-8zm-3.5 3c-1.2 2.2-2.8 4.6-2.5 7 .3 2.4 2 4.2 4 5-.8-1.5-1.2-3.3-1.2-5.2 0-2.3.8-4.5 1.7-6.8zm7 0c.9 2.3 1.7 4.5 1.7 6.8 0 1.9-.4 3.7-1.2 5.2 2-.8 3.7-2.6 4-5 .3-2.4-1.3-4.8-2.5-7zm-10 4.5C4.2 12.2 3 14 3 16c0 2 2 3.5 4.5 3.8-1-1.3-1.6-3-1.5-4.8.1-1.6.8-3.1 1.5-4.5zm13 0c.7 1.4 1.4 2.9 1.5 4.5.1 1.8-.5 3.5-1.5 4.8 2.5-.3 4.5-1.8 4.5-3.8 0-2-1.2-3.8-2.5-5.5z"/>
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
            @for(String scope : scopes)
                <label class="flex items-center gap-3 p-3.5 rounded-2xl bg-[#dde3dc]/50 cursor-pointer">
                    <input type="checkbox" name="scope" value="${scope}" checked
                           class="w-4 h-4 rounded text-[#5e7c65] border-[#c5d0c4] focus:ring-[#5e7c65]"/>
                    <span class="text-xs font-semibold text-[#2d3b31]">${scope}</span>
                </label>
            @endfor
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