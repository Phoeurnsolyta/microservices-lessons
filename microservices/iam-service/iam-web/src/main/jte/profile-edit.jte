@param kh.edu.istad.platform.iam.web.dto.ProfileEditForm form = null
@param kh.edu.istad.platform.iam.domain.dto.UserResult user = null
@param String successMessage = null
@param String errorMessage = null
@param String activeTab = "editProfile"
@param kh.edu.istad.platform.iam.web.i18n.I18n msg

<!DOCTYPE html>
<html lang="${msg.getCurrentLang()}">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${msg.get("profileEdit.title")} - ${msg.get("common.appName")}</title>

    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&family=Kantumruy+Pro:wght@400;500;600;700&display=swap"
          rel="stylesheet">
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
        <a href="/"
           class="px-5 py-2.5 rounded-full text-xs font-medium whitespace-nowrap bg-[#fafbf9] border border-[#e5ebe4] text-[#718575] hover:text-[#2d3b31]">
            ${msg.get("nav.overview")}
        </a>
        <a href="/profile/edit"
           class="px-5 py-2.5 rounded-full text-xs font-medium whitespace-nowrap bg-[#5e7c65] text-white shadow-sm">
            ${msg.get("nav.editProfile")}
        </a>
        <a href="/profile/password"
           class="px-5 py-2.5 rounded-full text-xs font-medium whitespace-nowrap bg-[#fafbf9] border border-[#e5ebe4] text-[#718575] hover:text-[#2d3b31]">
            ${msg.get("nav.changePassword")}
        </a>
        <a href="/profile/accounts"
           class="px-5 py-2.5 rounded-full text-xs font-medium whitespace-nowrap bg-[#fafbf9] border border-[#e5ebe4] text-[#718575] hover:text-[#2d3b31]">
            ${msg.get("nav.linkedAccounts")}
        </a>
        <a href="/profile/apps"
           class="px-5 py-2.5 rounded-full text-xs font-medium whitespace-nowrap bg-[#fafbf9] border border-[#e5ebe4] text-[#718575] hover:text-[#2d3b31]">
            ${msg.get("nav.connectedApps")}
        </a>
    </nav>

    <div class="bg-[#fafbf9] border border-[#e5ebe4] rounded-3xl p-8 shadow-sm">
        <h2 class="text-xl font-bold text-[#2d3b31]">${msg.get("profileEdit.title")}</h2>
        <p class="text-xs text-[#718575] mt-1 mb-6">${msg.get("profileEdit.subtitle")}</p>

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

        <form action="/profile/edit" method="post" class="space-y-4">
            <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
                <div>
                    <label class="block text-xs font-semibold text-[#718575] mb-1">${msg.get("register.givenName")}</label>
                    <input type="text" name="givenName" value="${form != null ? form.getGivenName() : ""}" required
                           class="w-full bg-[#dde3dc]/80 focus:bg-[#dde3dc] text-[#2d3b31] text-sm rounded-2xl py-3 px-4 outline-none"/>
                </div>
                <div>
                    <label class="block text-xs font-semibold text-[#718575] mb-1">${msg.get("register.familyName")}</label>
                    <input type="text" name="familyName" value="${form != null ? form.getFamilyName() : ""}" required
                           class="w-full bg-[#dde3dc]/80 focus:bg-[#dde3dc] text-[#2d3b31] text-sm rounded-2xl py-3 px-4 outline-none"/>
                </div>
            </div>

            <div class="grid grid-cols-1 sm:grid-cols-3 gap-4">
                <div>
                    <label class="block text-xs font-semibold text-[#718575] mb-1">${msg.get("profileEdit.gender")}</label>
                    <input type="text" name="gender" value="${form != null ? form.getGender() : ""}"
                           placeholder="male, female, other"
                           class="w-full bg-[#dde3dc]/80 focus:bg-[#dde3dc] text-[#2d3b31] text-sm rounded-2xl py-3 px-4 outline-none"/>
                </div>
                <div>
                    <label class="block text-xs font-semibold text-[#718575] mb-1">${msg.get("profileEdit.birthdate")}</label>
                    <input type="date" name="birthdate" value="${form != null ? form.getBirthdate() : ""}"
                           class="w-full bg-[#dde3dc]/80 focus:bg-[#dde3dc] text-[#2d3b31] text-sm rounded-2xl py-3 px-4 outline-none"/>
                </div>
                <div>
                    <label class="block text-xs font-semibold text-[#718575] mb-1">${msg.get("profileEdit.phone")}</label>
                    <input type="text" name="phoneNumber" value="${form != null ? form.getPhoneNumber() : ""}"
                           placeholder="+855..."
                           class="w-full bg-[#dde3dc]/80 focus:bg-[#dde3dc] text-[#2d3b31] text-sm rounded-2xl py-3 px-4 outline-none"/>
                </div>
            </div>

            <div class="pt-4 border-t border-[#e5ebe4]">
                <h4 class="text-sm font-bold text-[#2d3b31] mb-3">${msg.get("profileEdit.addressHeader")}</h4>
                <div class="space-y-3">
                    <input type="text" name="streetAddress" value="${form != null ? form.getStreetAddress() : ""}"
                           placeholder="${msg.get("profileEdit.streetAddress")}"
                           class="w-full bg-[#dde3dc]/80 focus:bg-[#dde3dc] text-[#2d3b31] text-sm rounded-2xl py-3 px-4 outline-none"/>
                    <div class="grid grid-cols-2 gap-3">
                        <input type="text" name="locality" value="${form != null ? form.getLocality() : ""}"
                               placeholder="${msg.get("profileEdit.locality")}"
                               class="w-full bg-[#dde3dc]/80 focus:bg-[#dde3dc] text-[#2d3b31] text-sm rounded-2xl py-3 px-4 outline-none"/>
                        <input type="text" name="country" value="${form != null ? form.getCountry() : "KH"}"
                               placeholder="${msg.get("profileEdit.country")}"
                               class="w-full bg-[#dde3dc]/80 focus:bg-[#dde3dc] text-[#2d3b31] text-sm rounded-2xl py-3 px-4 outline-none"/>
                    </div>
                </div>
            </div>

            <div class="pt-4 flex justify-end">
                <button type="submit"
                        class="px-8 py-3 bg-[#5e7c65] hover:bg-[#4e6b55] text-white text-sm font-semibold rounded-full shadow-md transition-all">
                    ${msg.get("common.save")}
                </button>
            </div>
        </form>
    </div>
</div>
</body>
</html>