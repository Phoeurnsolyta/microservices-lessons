package gg.jte.generated.ondemand;
@SuppressWarnings("unchecked")
@javax.annotation.processing.Generated("gg.jte.TemplateEngine")
public final class JteindexGenerated {
	public static final String JTE_NAME = "index.jte";
	public static final int[] JTE_LINE_INFO = {0,0,0,0,0,0,0,7,7,7,7,7,7,7,7,7,11,11,11,11,11,11,34,38,38,38,42,42,42,44,44,44,49,52,52,52,52,54,54,54,54,56,58,58,59,59,59,59,59,59,59,59,59,59,59,59,59,59,59,59,59,60,60,63,63,63,69,72,72,72,72,73,73,73,77,77,77,81,81,81,85,85,85,89,89,89,93,96,98,98,98,102,102,102,106,106,106,110,110,110,114,114,114,118,118,118,123,123,123,129,131,131,131,133,133,134,134,136,136,136,138,138,139,139,141,141,145,147,147,147,149,149,150,150,152,152,152,154,154,155,155,157,157,164,0,0,0,1,2,3,4,0,0,0};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, kh.edu.istad.platform.iam.domain.dto.UserResult user, kh.edu.istad.platform.iam.domain.dto.UserAuthoritiesResult authorities, String activeTab, org.springframework.security.web.csrf.CsrfToken _csrf, kh.edu.istad.platform.iam.web.i18n.I18n msg) {
		jteOutput.writeContent("\r\n<!DOCTYPE html>\r\n<html");
		var __jte_html_attribute_0 = msg.getCurrentLang();
		if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_0)) {
			jteOutput.writeContent(" lang=\"");
			jteOutput.setContext("html", "lang");
			jteOutput.writeUserContent(__jte_html_attribute_0);
			jteOutput.setContext("html", null);
			jteOutput.writeContent("\"");
		}
		jteOutput.writeContent(">\r\n<head>\r\n    <meta charset=\"UTF-8\">\r\n    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n    <title>");
		jteOutput.setContext("title", null);
		jteOutput.writeUserContent(msg.get("profile.title"));
		jteOutput.writeContent(" - ");
		jteOutput.setContext("title", null);
		jteOutput.writeUserContent(msg.get("common.appName"));
		jteOutput.writeContent("</title>\r\n\r\n    <link rel=\"preconnect\" href=\"https://fonts.googleapis.com\">\r\n    <link rel=\"preconnect\" href=\"https://fonts.gstatic.com\" crossorigin>\r\n    <link href=\"https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&family=Kantumruy+Pro:wght@400;500;600;700&display=swap\"\r\n          rel=\"stylesheet\">\r\n    <script src=\"https://cdn.tailwindcss.com\"></script>\r\n    <script>\r\n        tailwind.config = {\r\n            theme: {\r\n                extend: {\r\n                    fontFamily: {\r\n                        sans: ['\"Kantumruy Pro\"', 'Inter', 'sans-serif'],\r\n                    }\r\n                }\r\n            }\r\n        }\r\n    </script>\r\n</head>\r\n<body class=\"bg-[#f0f3ee] text-[#2d3b31] min-h-screen p-4 sm:p-8\">\r\n\r\n<div class=\"max-w-4xl mx-auto space-y-6\">\r\n\r\n    ");
		jteOutput.writeContent("\r\n    <header class=\"bg-[#fafbf9] border border-[#e5ebe4] rounded-3xl p-6 flex flex-col sm:flex-row items-center justify-between gap-4 shadow-sm\">\r\n        <div class=\"flex items-center gap-4\">\r\n            <div class=\"w-12 h-12 rounded-full bg-[#dde3dc] flex items-center justify-center text-[#4c6552] font-bold text-lg\">\r\n                ");
		jteOutput.setContext("div", null);
		jteOutput.writeUserContent(user != null && user.givenName() != null ? user.givenName().substring(0, 1) : "U");
		jteOutput.writeContent("\r\n            </div>\r\n            <div>\r\n                <h2 class=\"text-xl font-bold text-[#2d3b31]\">\r\n                    ");
		jteOutput.setContext("h2", null);
		jteOutput.writeUserContent(user != null ? (user.givenName() + " " + user.familyName()) : "User");
		jteOutput.writeContent("\r\n                </h2>\r\n                <p class=\"text-xs text-[#718575]\">");
		jteOutput.setContext("p", null);
		jteOutput.writeUserContent(user != null ? user.email() : "");
		jteOutput.writeContent("</p>\r\n            </div>\r\n        </div>\r\n\r\n        <div class=\"flex items-center gap-3\">\r\n            ");
		jteOutput.writeContent("\r\n            <div class=\"inline-flex items-center bg-[#dde3dc]/60 p-1 rounded-full text-xs font-semibold\">\r\n                <a href=\"?lang=en\"\r\n                   class=\"px-3 py-1 rounded-full ");
		jteOutput.setContext("a", "class");
		jteOutput.writeUserContent("en".equals(msg.getCurrentLang()) ? "bg-[#5e7c65] text-white shadow-sm" : "text-[#2d3b31]");
		jteOutput.setContext("a", null);
		jteOutput.writeContent("\">EN</a>\r\n                <a href=\"?lang=km\"\r\n                   class=\"px-3 py-1 rounded-full ");
		jteOutput.setContext("a", "class");
		jteOutput.writeUserContent("km".equals(msg.getCurrentLang()) ? "bg-[#5e7c65] text-white shadow-sm" : "text-[#2d3b31]");
		jteOutput.setContext("a", null);
		jteOutput.writeContent("\">KM</a>\r\n            </div>\r\n            ");
		jteOutput.writeContent("\r\n            <form action=\"/logout\" method=\"post\" class=\"inline\">\r\n                ");
		if (_csrf != null) {
			jteOutput.writeContent("\r\n                    <input type=\"hidden\"");
			var __jte_html_attribute_1 = _csrf.getParameterName();
			if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_1)) {
				jteOutput.writeContent(" name=\"");
				jteOutput.setContext("input", "name");
				jteOutput.writeUserContent(__jte_html_attribute_1);
				jteOutput.setContext("input", null);
				jteOutput.writeContent("\"");
			}
			var __jte_html_attribute_2 = _csrf.getToken();
			if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_2)) {
				jteOutput.writeContent(" value=\"");
				jteOutput.setContext("input", "value");
				jteOutput.writeUserContent(__jte_html_attribute_2);
				jteOutput.setContext("input", null);
				jteOutput.writeContent("\"");
			}
			jteOutput.writeContent("/>\r\n                ");
		}
		jteOutput.writeContent("\r\n                <button type=\"submit\"\r\n                        class=\"px-4 py-1.5 rounded-full bg-[#dde3dc] hover:bg-rose-100 hover:text-rose-700 text-xs font-medium transition-colors\">\r\n                    ");
		jteOutput.setContext("button", null);
		jteOutput.writeUserContent(msg.get("common.logout"));
		jteOutput.writeContent("\r\n                </button>\r\n            </form>\r\n        </div>\r\n    </header>\r\n\r\n    ");
		jteOutput.writeContent("\r\n    <nav class=\"flex items-center gap-2 overflow-x-auto pb-2\">\r\n        <a href=\"/\"\r\n           class=\"px-5 py-2.5 rounded-full text-xs font-medium whitespace-nowrap transition-all ");
		jteOutput.setContext("a", "class");
		jteOutput.writeUserContent("overview".equals(activeTab) ? "bg-[#5e7c65] text-white shadow-sm" : "bg-[#fafbf9] border border-[#e5ebe4] text-[#718575] hover:text-[#2d3b31]");
		jteOutput.setContext("a", null);
		jteOutput.writeContent("\">\r\n            ");
		jteOutput.setContext("a", null);
		jteOutput.writeUserContent(msg.get("nav.overview"));
		jteOutput.writeContent("\r\n        </a>\r\n        <a href=\"/profile/edit\"\r\n           class=\"px-5 py-2.5 rounded-full text-xs font-medium whitespace-nowrap transition-all bg-[#fafbf9] border border-[#e5ebe4] text-[#718575] hover:text-[#2d3b31]\">\r\n            ");
		jteOutput.setContext("a", null);
		jteOutput.writeUserContent(msg.get("nav.editProfile"));
		jteOutput.writeContent("\r\n        </a>\r\n        <a href=\"/profile/password\"\r\n           class=\"px-5 py-2.5 rounded-full text-xs font-medium whitespace-nowrap transition-all bg-[#fafbf9] border border-[#e5ebe4] text-[#718575] hover:text-[#2d3b31]\">\r\n            ");
		jteOutput.setContext("a", null);
		jteOutput.writeUserContent(msg.get("nav.changePassword"));
		jteOutput.writeContent("\r\n        </a>\r\n        <a href=\"/profile/accounts\"\r\n           class=\"px-5 py-2.5 rounded-full text-xs font-medium whitespace-nowrap transition-all bg-[#fafbf9] border border-[#e5ebe4] text-[#718575] hover:text-[#2d3b31]\">\r\n            ");
		jteOutput.setContext("a", null);
		jteOutput.writeUserContent(msg.get("nav.linkedAccounts"));
		jteOutput.writeContent("\r\n        </a>\r\n        <a href=\"/profile/apps\"\r\n           class=\"px-5 py-2.5 rounded-full text-xs font-medium whitespace-nowrap transition-all bg-[#fafbf9] border border-[#e5ebe4] text-[#718575] hover:text-[#2d3b31]\">\r\n            ");
		jteOutput.setContext("a", null);
		jteOutput.writeUserContent(msg.get("nav.connectedApps"));
		jteOutput.writeContent("\r\n        </a>\r\n    </nav>\r\n\r\n    ");
		jteOutput.writeContent("\r\n    <div class=\"bg-[#fafbf9] border border-[#e5ebe4] rounded-3xl p-8 shadow-sm space-y-8\">\r\n\r\n        ");
		jteOutput.writeContent("\r\n        <div>\r\n            <h3 class=\"text-base font-bold text-[#2d3b31] mb-4\">");
		jteOutput.setContext("h3", null);
		jteOutput.writeUserContent(msg.get("profile.claims"));
		jteOutput.writeContent("</h3>\r\n            <div class=\"grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4\">\r\n                <div class=\"p-4 bg-[#dde3dc]/40 rounded-2xl\">\r\n                    <span class=\"text-xs text-[#718575] block\">Username</span>\r\n                    <span class=\"text-sm font-semibold text-[#2d3b31]\">");
		jteOutput.setContext("span", null);
		jteOutput.writeUserContent(user != null ? user.username() : "-");
		jteOutput.writeContent("</span>\r\n                </div>\r\n                <div class=\"p-4 bg-[#dde3dc]/40 rounded-2xl\">\r\n                    <span class=\"text-xs text-[#718575] block\">Email</span>\r\n                    <span class=\"text-sm font-semibold text-[#2d3b31]\">");
		jteOutput.setContext("span", null);
		jteOutput.writeUserContent(user != null ? user.email() : "-");
		jteOutput.writeContent("</span>\r\n                </div>\r\n                <div class=\"p-4 bg-[#dde3dc]/40 rounded-2xl\">\r\n                    <span class=\"text-xs text-[#718575] block\">Subject (UUID)</span>\r\n                    <span class=\"text-xs font-mono text-[#2d3b31] truncate block\">");
		jteOutput.setContext("span", null);
		jteOutput.writeUserContent(user != null ? String.valueOf(user.uuid()) : "-");
		jteOutput.writeContent("</span>\r\n                </div>\r\n                <div class=\"p-4 bg-[#dde3dc]/40 rounded-2xl\">\r\n                    <span class=\"text-xs text-[#718575] block\">Email Verified</span>\r\n                    <span class=\"text-sm font-semibold text-[#2d3b31]\">");
		jteOutput.setContext("span", null);
		jteOutput.writeUserContent(user != null && user.emailVerified() ? "Verified" : "Unverified");
		jteOutput.writeContent("</span>\r\n                </div>\r\n                <div class=\"p-4 bg-[#dde3dc]/40 rounded-2xl\">\r\n                    <span class=\"text-xs text-[#718575] block\">Preferred Locale</span>\r\n                    <span class=\"text-sm font-semibold uppercase text-[#2d3b31]\">");
		jteOutput.setContext("span", null);
		jteOutput.writeUserContent(user != null ? user.locale() : "en");
		jteOutput.writeContent("</span>\r\n                </div>\r\n                <div class=\"p-4 bg-[#dde3dc]/40 rounded-2xl\">\r\n                    <span class=\"text-xs text-[#718575] block\">Status</span>\r\n                    <span class=\"inline-block mt-0.5 px-2.5 py-0.5 bg-emerald-100 text-emerald-800 rounded-full text-xs font-semibold\">\r\n                            ");
		jteOutput.setContext("span", null);
		jteOutput.writeUserContent(user != null && user.enabled() ? "Active" : "Disabled");
		jteOutput.writeContent("\r\n                        </span>\r\n                </div>\r\n            </div>\r\n        </div>\r\n\r\n        ");
		jteOutput.writeContent("\r\n        <div>\r\n            <h3 class=\"text-base font-bold text-[#2d3b31] mb-3\">");
		jteOutput.setContext("h3", null);
		jteOutput.writeUserContent(msg.get("profile.roles"));
		jteOutput.writeContent("</h3>\r\n            <div class=\"flex flex-wrap gap-2\">\r\n                ");
		if (authorities != null && authorities.roles() != null && !authorities.roles().isEmpty()) {
			jteOutput.writeContent("\r\n                    ");
			for (kh.edu.istad.platform.iam.domain.dto.RoleAuthorityResult role : authorities.roles()) {
				jteOutput.writeContent("\r\n                        <span class=\"px-4 py-1.5 rounded-full bg-[#dde3dc] text-[#3d4f42] text-xs font-semibold\">\r\n                                ");
				jteOutput.setContext("span", null);
				jteOutput.writeUserContent(role.name());
				jteOutput.writeContent("\r\n                            </span>\r\n                    ");
			}
			jteOutput.writeContent("\r\n                ");
		} else {
			jteOutput.writeContent("\r\n                    <span class=\"text-xs text-[#718575]\">No roles assigned.</span>\r\n                ");
		}
		jteOutput.writeContent("\r\n            </div>\r\n        </div>\r\n\r\n        ");
		jteOutput.writeContent("\r\n        <div>\r\n            <h3 class=\"text-base font-bold text-[#2d3b31] mb-3\">");
		jteOutput.setContext("h3", null);
		jteOutput.writeUserContent(msg.get("profile.permissions"));
		jteOutput.writeContent("</h3>\r\n            <div class=\"flex flex-wrap gap-2\">\r\n                ");
		if (authorities != null && authorities.effectivePermissions() != null && !authorities.effectivePermissions().isEmpty()) {
			jteOutput.writeContent("\r\n                    ");
			for (String perm : authorities.effectivePermissions()) {
				jteOutput.writeContent("\r\n                        <span class=\"px-3.5 py-1.5 rounded-full bg-[#e8eee7] border border-[#d8e0d7] text-[#4c6552] text-xs font-mono font-medium\">\r\n                                ");
				jteOutput.setContext("span", null);
				jteOutput.writeUserContent(perm);
				jteOutput.writeContent("\r\n                            </span>\r\n                    ");
			}
			jteOutput.writeContent("\r\n                ");
		} else {
			jteOutput.writeContent("\r\n                    <span class=\"text-xs text-[#718575]\">No direct permissions.</span>\r\n                ");
		}
		jteOutput.writeContent("\r\n            </div>\r\n        </div>\r\n\r\n    </div>\r\n</div>\r\n</body>\r\n</html>");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		kh.edu.istad.platform.iam.domain.dto.UserResult user = (kh.edu.istad.platform.iam.domain.dto.UserResult)params.getOrDefault("user",  null);
		kh.edu.istad.platform.iam.domain.dto.UserAuthoritiesResult authorities = (kh.edu.istad.platform.iam.domain.dto.UserAuthoritiesResult)params.getOrDefault("authorities",  null);
		String activeTab = (String)params.getOrDefault("activeTab",  "overview");
		org.springframework.security.web.csrf.CsrfToken _csrf = (org.springframework.security.web.csrf.CsrfToken)params.getOrDefault("_csrf",  null);
		kh.edu.istad.platform.iam.web.i18n.I18n msg = (kh.edu.istad.platform.iam.web.i18n.I18n)params.get("msg");
		render(jteOutput, jteHtmlInterceptor, user, authorities, activeTab, _csrf, msg);
	}
}
