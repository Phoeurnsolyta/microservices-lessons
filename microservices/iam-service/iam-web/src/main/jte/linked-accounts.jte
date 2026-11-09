@param java.util.List<kh.edu.istad.platform.iam.domain.dto.IdentityResult> identities = null
@param String successMessage = null
@param String errorMessage = null
@param String activeTab = "linkedAccounts"
@param kh.edu.istad.platform.iam.web.i18n.I18n msg

<!DOCTYPE html>
<html lang="${msg.getCurrentLang()}">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${msg.get("linkedAccounts.title")} - ${msg.get("common.appName")}</title>

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
<body class="bg-[#f0f3ee] text-[#2d3b31] min-h-screen p-4 sm:p-8">

<div class="max-w-3xl mx-auto space-y-6">

    <!-- Navigation Tabs -->
    <nav class="flex items-center gap-2 overflow-x-auto pb-2">
        <a href="/" class="px-5 py-2.5 rounded-full text-xs font-medium whitespace-nowrap bg-[#fafbf9] border border-[#e5ebe4] text-[#718575] hover:text-[#2d3b31]">
            ${msg.get("nav.overview")}
        </a>
        <a href="/profile/edit" class="px-5 py-2.5 rounded-full text-xs font-medium whitespace-nowrap bg-[#fafbf9] border border-[#e5ebe4] text-[#718575] hover:text-[#2d3b31]">
            ${msg.get("nav.editProfile")}
        </a>
        <a href="/profile/password" class="px-5 py-2.5 rounded-full text-xs font-medium whitespace-nowrap bg-[#fafbf9] border border-[#e5ebe4] text-[#718575] hover:text-[#2d3b31]">
            ${msg.get("nav.changePassword")}
        </a>
        <a href="/profile/accounts" class="px-5 py-2.5 rounded-full text-xs font-medium whitespace-nowrap bg-[#5e7c65] text-white shadow-sm">
            ${msg.get("nav.linkedAccounts")}
        </a>
        <a href="/profile/apps" class="px-5 py-2.5 rounded-full text-xs font-medium whitespace-nowrap bg-[#fafbf9] border border-[#e5ebe4] text-[#718575] hover:text-[#2d3b31]">
            ${msg.get("nav.connectedApps")}
        </a>
    </nav>

    <div class="bg-[#fafbf9] border border-[#e5ebe4] rounded-3xl p-8 shadow-sm">
        <h2 class="text-xl font-bold text-[#2d3b31]">${msg.get("linkedAccounts.title")}</h2>
        <p class="text-xs text-[#718575] mt-1 mb-6">${msg.get("linkedAccounts.subtitle")}</p>

        @if(successMessage != null)
            <div class="mb-5 p-3.5 text-xs rounded-2xl bg-emerald-50 text-emerald-800 border border-emerald-200">
                ${successMessage}
            </div>
        @endif

        @if(errorMessage != null)
            <div class="mb-5 p-3.5 text-xs rounded-2xl bg-rose-50 text-rose-800 border border-rose-200">
                ${errorMessage}
            </div>
        @endif

        <div class="space-y-3">
            @if(identities != null && !identities.isEmpty())
                @for(kh.edu.istad.platform.iam.domain.dto.IdentityResult item : identities)
                    <div class="flex items-center justify-between p-4 bg-[#dde3dc]/40 rounded-2xl">
                        <div class="flex items-center gap-3">
                                <span class="px-3 py-1 rounded-full bg-[#dde3dc] font-bold text-xs uppercase text-[#3d4f42]">
                                    ${item.provider()}
                                </span>
                            <div>
                                <h4 class="text-sm font-semibold text-[#2d3b31]">${item.displayName() != null ? item.displayName() : item.email()}</h4>
                                <p class="text-xs text-[#718575]">${item.email() != null ? item.email() : ""}</p>
                            </div>
                        </div>

                        <form action="/profile/accounts/${item.uuid()}/unlink" method="post" onsubmit="return confirm('${msg.get("linkedAccounts.unlinkConfirm")}');">
                            <button type="submit" class="px-4 py-1.5 rounded-full text-xs font-medium bg-rose-50 text-rose-700 hover:bg-rose-100 transition-colors">
                                ${msg.get("linkedAccounts.unlink")}
                            </button>
                        </form>
                    </div>
                @endfor
            @else
                <p class="text-xs text-[#718575] py-4 text-center">${msg.get("linkedAccounts.noAccounts")}</p>
            @endif
        </div>
    </div>
</div>
</body>
</html>