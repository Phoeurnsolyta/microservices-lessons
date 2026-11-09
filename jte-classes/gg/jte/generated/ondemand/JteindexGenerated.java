package gg.jte.generated.ondemand;
@SuppressWarnings("unchecked")
@javax.annotation.processing.Generated("gg.jte.TemplateEngine")
public final class JteindexGenerated {
	public static final String JTE_NAME = "index.jte";
	public static final int[] JTE_LINE_INFO = {0,0,0,0,0,0,0,7,7,7,7,7,7,7,7,7,11,11,11,11,11,11,197,197,197,198,198,198,202,202,202,202,202,202,202,202,202,203,203,203,203,203,203,203,203,203,206,206,207,207,207,207,207,207,207,207,207,207,207,207,207,207,207,207,207,208,208,209,209,209,214,216,216,216,217,217,217,218,218,218,219,219,219,220,220,220,223,223,225,227,227,227,231,231,231,234,234,234,235,235,235,238,238,238,239,239,239,239,239,239,243,243,243,247,247,247,251,253,253,253,255,255,256,256,257,257,257,258,258,259,259,261,261,265,267,267,267,269,269,270,270,271,271,271,272,272,273,273,275,275,279,279,283,0,0,0,1,2,3,4,0,0,0};
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
		jteOutput.writeContent("</title>\r\n    <style>\r\n        :root {\r\n            --primary: #4f46e5;\r\n            --primary-hover: #4338ca;\r\n            --bg: #f8fafc;\r\n            --card-bg: #ffffff;\r\n            --text-main: #0f172a;\r\n            --text-muted: #64748b;\r\n            --border: #e2e8f0;\r\n            --tag-bg: #e0e7ff;\r\n            --tag-text: #3730a3;\r\n        }\r\n\r\n        * {\r\n            box-sizing: border-box;\r\n            margin: 0;\r\n            padding: 0;\r\n        }\r\n\r\n        body {\r\n            font-family: system-ui, -apple-system, sans-serif;\r\n            background-color: var(--bg);\r\n            color: var(--text-main);\r\n            line-height: 1.5;\r\n            padding: 2rem 1rem;\r\n        }\r\n\r\n        .container {\r\n            max-width: 900px;\r\n            margin: 0 auto;\r\n        }\r\n\r\n        .header {\r\n            display: flex;\r\n            justify-content: space-between;\r\n            align-items: center;\r\n            margin-bottom: 1.5rem;\r\n            padding-bottom: 1rem;\r\n            border-bottom: 1px solid var(--border);\r\n        }\r\n\r\n        .header h1 {\r\n            font-size: 1.75rem;\r\n            color: var(--primary);\r\n        }\r\n\r\n        .header-actions {\r\n            display: flex;\r\n            gap: 1rem;\r\n            align-items: center;\r\n        }\r\n\r\n        .lang-switch {\r\n            display: flex;\r\n            gap: 0.5rem;\r\n            font-size: 0.875rem;\r\n        }\r\n\r\n        .lang-switch a {\r\n            text-decoration: none;\r\n            color: var(--text-muted);\r\n            padding: 0.25rem 0.5rem;\r\n            border-radius: 4px;\r\n            border: 1px solid var(--border);\r\n        }\r\n\r\n        .lang-switch a.active {\r\n            background: var(--primary);\r\n            color: #fff;\r\n            border-color: var(--primary);\r\n        }\r\n\r\n        .btn-logout {\r\n            background: #ef4444;\r\n            color: white;\r\n            border: none;\r\n            padding: 0.5rem 1rem;\r\n            border-radius: 6px;\r\n            cursor: pointer;\r\n            font-size: 0.875rem;\r\n            font-weight: 500;\r\n        }\r\n\r\n        .btn-logout:hover {\r\n            background: #dc2626;\r\n        }\r\n\r\n        .nav-tabs {\r\n            display: flex;\r\n            gap: 0.5rem;\r\n            margin-bottom: 2rem;\r\n            border-bottom: 1px solid var(--border);\r\n            padding-bottom: 0.5rem;\r\n            flex-wrap: wrap;\r\n        }\r\n\r\n        .nav-tab {\r\n            text-decoration: none;\r\n            color: var(--text-muted);\r\n            padding: 0.5rem 1rem;\r\n            border-radius: 6px;\r\n            font-size: 0.9rem;\r\n            font-weight: 500;\r\n        }\r\n\r\n        .nav-tab.active {\r\n            background: var(--primary);\r\n            color: white;\r\n        }\r\n\r\n        .grid {\r\n            display: grid;\r\n            grid-template-columns: 1fr;\r\n            gap: 1.5rem;\r\n        }\r\n\r\n        @media (min-width: 768px) {\r\n            .grid {\r\n                grid-template-columns: repeat(2, 1fr);\r\n            }\r\n\r\n            .grid-full {\r\n                grid-column: span 2;\r\n            }\r\n        }\r\n\r\n        .card {\r\n            background: var(--card-bg);\r\n            border: 1px solid var(--border);\r\n            border-radius: 8px;\r\n            padding: 1.5rem;\r\n            box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);\r\n        }\r\n\r\n        .card h2 {\r\n            font-size: 1.15rem;\r\n            margin-bottom: 1rem;\r\n            color: var(--text-main);\r\n            border-bottom: 1px solid var(--border);\r\n            padding-bottom: 0.5rem;\r\n        }\r\n\r\n        .field {\r\n            margin-bottom: 0.75rem;\r\n            display: flex;\r\n            justify-content: space-between;\r\n            font-size: 0.925rem;\r\n        }\r\n\r\n        .field-label {\r\n            color: var(--text-muted);\r\n            font-weight: 500;\r\n        }\r\n\r\n        .field-val {\r\n            font-weight: 600;\r\n            text-align: right;\r\n        }\r\n\r\n        .tag-list {\r\n            display: flex;\r\n            flex-wrap: wrap;\r\n            gap: 0.5rem;\r\n        }\r\n\r\n        .tag {\r\n            background: var(--tag-bg);\r\n            color: var(--tag-text);\r\n            padding: 0.25rem 0.65rem;\r\n            border-radius: 9999px;\r\n            font-size: 0.8rem;\r\n            font-weight: 600;\r\n        }\r\n\r\n        .tag-perm {\r\n            background: #dcfce7;\r\n            color: #166534;\r\n        }\r\n    </style>\r\n</head>\r\n<body>\r\n\r\n<div class=\"container\">\r\n    <div class=\"header\">\r\n        <div>\r\n            <h1>");
		jteOutput.setContext("h1", null);
		jteOutput.writeUserContent(msg.get("profile.title"));
		jteOutput.writeContent("</h1>\r\n            <p style=\"color: var(--text-muted); font-size: 0.9rem;\">");
		jteOutput.setContext("p", null);
		jteOutput.writeUserContent(msg.get("profile.subtitle"));
		jteOutput.writeContent("</p>\r\n        </div>\r\n        <div class=\"header-actions\">\r\n            <div class=\"lang-switch\">\r\n                <a href=\"/?lang=en\"");
		var __jte_html_attribute_1 = "en".equals(msg.getCurrentLang()) ? "active" : "";
		if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_1)) {
			jteOutput.writeContent(" class=\"");
			jteOutput.setContext("a", "class");
			jteOutput.writeUserContent(__jte_html_attribute_1);
			jteOutput.setContext("a", null);
			jteOutput.writeContent("\"");
		}
		jteOutput.writeContent(">EN</a>\r\n                <a href=\"/?lang=km\"");
		var __jte_html_attribute_2 = "km".equals(msg.getCurrentLang()) ? "active" : "";
		if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_2)) {
			jteOutput.writeContent(" class=\"");
			jteOutput.setContext("a", "class");
			jteOutput.writeUserContent(__jte_html_attribute_2);
			jteOutput.setContext("a", null);
			jteOutput.writeContent("\"");
		}
		jteOutput.writeContent(">KM</a>\r\n            </div>\r\n            <form action=\"/logout\" method=\"post\" style=\"display:inline;\">\r\n                ");
		if (_csrf != null) {
			jteOutput.writeContent("\r\n                    <input type=\"hidden\"");
			var __jte_html_attribute_3 = _csrf.getParameterName();
			if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_3)) {
				jteOutput.writeContent(" name=\"");
				jteOutput.setContext("input", "name");
				jteOutput.writeUserContent(__jte_html_attribute_3);
				jteOutput.setContext("input", null);
				jteOutput.writeContent("\"");
			}
			var __jte_html_attribute_4 = _csrf.getToken();
			if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_4)) {
				jteOutput.writeContent(" value=\"");
				jteOutput.setContext("input", "value");
				jteOutput.writeUserContent(__jte_html_attribute_4);
				jteOutput.setContext("input", null);
				jteOutput.writeContent("\"");
			}
			jteOutput.writeContent(">\r\n                ");
		}
		jteOutput.writeContent("\r\n                <button type=\"submit\" class=\"btn-logout\">");
		jteOutput.setContext("button", null);
		jteOutput.writeUserContent(msg.get("common.logout"));
		jteOutput.writeContent("</button>\r\n            </form>\r\n        </div>\r\n    </div>\r\n\r\n    ");
		jteOutput.writeContent("\r\n    <div class=\"nav-tabs\">\r\n        <a href=\"/\" class=\"nav-tab active\">");
		jteOutput.setContext("a", null);
		jteOutput.writeUserContent(msg.get("nav.overview"));
		jteOutput.writeContent("</a>\r\n        <a href=\"/profile/edit\" class=\"nav-tab\">");
		jteOutput.setContext("a", null);
		jteOutput.writeUserContent(msg.get("nav.editProfile"));
		jteOutput.writeContent("</a>\r\n        <a href=\"/profile/password\" class=\"nav-tab\">");
		jteOutput.setContext("a", null);
		jteOutput.writeUserContent(msg.get("nav.changePassword"));
		jteOutput.writeContent("</a>\r\n        <a href=\"/profile/accounts\" class=\"nav-tab\">");
		jteOutput.setContext("a", null);
		jteOutput.writeUserContent(msg.get("nav.linkedAccounts"));
		jteOutput.writeContent("</a>\r\n        <a href=\"/profile/apps\" class=\"nav-tab\">");
		jteOutput.setContext("a", null);
		jteOutput.writeUserContent(msg.get("nav.connectedApps"));
		jteOutput.writeContent("</a>\r\n    </div>\r\n\r\n    ");
		if (user != null) {
			jteOutput.writeContent("\r\n        <div class=\"grid\">\r\n            ");
			jteOutput.writeContent("\r\n            <div class=\"card\">\r\n                <h2>");
			jteOutput.setContext("h2", null);
			jteOutput.writeUserContent(msg.get("profile.claims"));
			jteOutput.writeContent("</h2>\r\n                <div class=\"field\">\r\n                    <span class=\"field-label\">sub (UUID)</span>\r\n                    <span class=\"field-val\"\r\n                          style=\"font-family: monospace; font-size: 0.8rem;\">");
			jteOutput.setContext("span", null);
			jteOutput.writeUserContent(user.uuid().toString());
			jteOutput.writeContent("</span>\r\n                </div>\r\n                <div class=\"field\">\r\n                    <span class=\"field-label\">");
			jteOutput.setContext("span", null);
			jteOutput.writeUserContent(msg.get("register.username"));
			jteOutput.writeContent("</span>\r\n                    <span class=\"field-val\">");
			jteOutput.setContext("span", null);
			jteOutput.writeUserContent(user.username());
			jteOutput.writeContent("</span>\r\n                </div>\r\n                <div class=\"field\">\r\n                    <span class=\"field-label\">");
			jteOutput.setContext("span", null);
			jteOutput.writeUserContent(msg.get("register.email"));
			jteOutput.writeContent("</span>\r\n                    <span class=\"field-val\">");
			jteOutput.setContext("span", null);
			jteOutput.writeUserContent(user.email());
			jteOutput.writeContent(" (");
			jteOutput.setContext("span", null);
			jteOutput.writeUserContent(user.emailVerified() ? "✓" : "✗");
			jteOutput.writeContent(")</span>\r\n                </div>\r\n                <div class=\"field\">\r\n                    <span class=\"field-label\">name</span>\r\n                    <span class=\"field-val\">");
			jteOutput.setContext("span", null);
			jteOutput.writeUserContent(user.name());
			jteOutput.writeContent("</span>\r\n                </div>\r\n                <div class=\"field\">\r\n                    <span class=\"field-label\">locale</span>\r\n                    <span class=\"field-val\">");
			jteOutput.setContext("span", null);
			jteOutput.writeUserContent(user.locale());
			jteOutput.writeContent("</span>\r\n                </div>\r\n            </div>\r\n\r\n            ");
			jteOutput.writeContent("\r\n            <div class=\"card\">\r\n                <h2>");
			jteOutput.setContext("h2", null);
			jteOutput.writeUserContent(msg.get("profile.roles"));
			jteOutput.writeContent("</h2>\r\n                <div class=\"tag-list\">\r\n                    ");
			if (authorities != null && authorities.roles() != null && !authorities.roles().isEmpty()) {
				jteOutput.writeContent("\r\n                        ");
				for (var role : authorities.roles()) {
					jteOutput.writeContent("\r\n                            <span class=\"tag\">ROLE_");
					jteOutput.setContext("span", null);
					jteOutput.writeUserContent(role.name());
					jteOutput.writeContent("</span>\r\n                        ");
				}
				jteOutput.writeContent("\r\n                    ");
			} else {
				jteOutput.writeContent("\r\n                        <span style=\"color: var(--text-muted); font-size: 0.875rem;\">No roles assigned</span>\r\n                    ");
			}
			jteOutput.writeContent("\r\n                </div>\r\n            </div>\r\n\r\n            ");
			jteOutput.writeContent("\r\n            <div class=\"card grid-full\">\r\n                <h2>");
			jteOutput.setContext("h2", null);
			jteOutput.writeUserContent(msg.get("profile.permissions"));
			jteOutput.writeContent("</h2>\r\n                <div class=\"tag-list\">\r\n                    ");
			if (authorities != null && authorities.effectivePermissions() != null && !authorities.effectivePermissions().isEmpty()) {
				jteOutput.writeContent("\r\n                        ");
				for (var perm : authorities.effectivePermissions()) {
					jteOutput.writeContent("\r\n                            <span class=\"tag tag-perm\">");
					jteOutput.setContext("span", null);
					jteOutput.writeUserContent(perm);
					jteOutput.writeContent("</span>\r\n                        ");
				}
				jteOutput.writeContent("\r\n                    ");
			} else {
				jteOutput.writeContent("\r\n                        <span style=\"color: var(--text-muted); font-size: 0.875rem;\">No permissions granted</span>\r\n                    ");
			}
			jteOutput.writeContent("\r\n                </div>\r\n            </div>\r\n        </div>\r\n    ");
		}
		jteOutput.writeContent("\r\n</div>\r\n\r\n</body>\r\n</html>");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		kh.edu.istad.platform.iam.domain.dto.UserResult user = (kh.edu.istad.platform.iam.domain.dto.UserResult)params.get("user");
		kh.edu.istad.platform.iam.domain.dto.UserAuthoritiesResult authorities = (kh.edu.istad.platform.iam.domain.dto.UserAuthoritiesResult)params.get("authorities");
		String activeTab = (String)params.getOrDefault("activeTab",  "overview");
		org.springframework.security.web.csrf.CsrfToken _csrf = (org.springframework.security.web.csrf.CsrfToken)params.getOrDefault("_csrf",  null);
		kh.edu.istad.platform.iam.web.i18n.I18n msg = (kh.edu.istad.platform.iam.web.i18n.I18n)params.get("msg");
		render(jteOutput, jteHtmlInterceptor, user, authorities, activeTab, _csrf, msg);
	}
}
