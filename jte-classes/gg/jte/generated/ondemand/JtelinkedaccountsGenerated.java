package gg.jte.generated.ondemand;
@SuppressWarnings("unchecked")
@javax.annotation.processing.Generated("gg.jte.TemplateEngine")
public final class JtelinkedaccountsGenerated {
	public static final String JTE_NAME = "linked-accounts.jte";
	public static final int[] JTE_LINE_INFO = {0,0,0,0,0,0,0,8,8,8,8,8,8,8,8,8,12,12,12,12,12,12,154,154,154,155,155,155,159,159,159,159,159,159,159,159,159,160,160,160,160,160,160,160,160,160,163,163,163,168,168,168,169,169,169,170,170,170,171,171,171,172,172,172,175,175,176,176,176,177,177,178,178,179,179,179,180,180,183,183,187,187,187,188,188,188,189,189,189,190,190,190,191,191,191,195,195,197,197,197,198,198,198,199,199,199,200,200,200,202,202,202,202,204,204,204,204,205,205,206,206,206,206,206,206,206,206,206,206,206,206,206,206,206,206,206,207,207,208,208,208,212,212,215,215,216,216,216,217,217,222,0,0,0,1,2,3,4,5,0,0,0};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.List<kh.edu.istad.platform.iam.domain.dto.IdentityResult> identities, String successMessage, String errorMessage, String activeTab, org.springframework.security.web.csrf.CsrfToken _csrf, kh.edu.istad.platform.iam.web.i18n.I18n msg) {
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
		jteOutput.writeUserContent(msg.get("linkedAccounts.title"));
		jteOutput.writeContent(" - ");
		jteOutput.setContext("title", null);
		jteOutput.writeUserContent(msg.get("common.appName"));
		jteOutput.writeContent("</title>\r\n    <style>\r\n        :root {\r\n            --primary: #4f46e5;\r\n            --primary-hover: #4338ca;\r\n            --bg: #f8fafc;\r\n            --card-bg: #ffffff;\r\n            --text-main: #0f172a;\r\n            --text-muted: #64748b;\r\n            --border: #e2e8f0;\r\n            --danger: #ef4444;\r\n            --success: #22c55e;\r\n        }\r\n\r\n        * {\r\n            box-sizing: border-box;\r\n            margin: 0;\r\n            padding: 0;\r\n        }\r\n\r\n        body {\r\n            font-family: system-ui, -apple-system, sans-serif;\r\n            background-color: var(--bg);\r\n            color: var(--text-main);\r\n            padding: 2rem 1rem;\r\n        }\r\n\r\n        .container {\r\n            max-width: 900px;\r\n            margin: 0 auto;\r\n        }\r\n\r\n        .header {\r\n            display: flex;\r\n            justify-content: space-between;\r\n            align-items: center;\r\n            margin-bottom: 1.5rem;\r\n            padding-bottom: 1rem;\r\n            border-bottom: 1px solid var(--border);\r\n        }\r\n\r\n        .nav-tabs {\r\n            display: flex;\r\n            gap: 0.5rem;\r\n            margin-bottom: 2rem;\r\n            border-bottom: 1px solid var(--border);\r\n            padding-bottom: 0.5rem;\r\n            flex-wrap: wrap;\r\n        }\r\n\r\n        .nav-tab {\r\n            text-decoration: none;\r\n            color: var(--text-muted);\r\n            padding: 0.5rem 1rem;\r\n            border-radius: 6px;\r\n            font-size: 0.9rem;\r\n            font-weight: 500;\r\n        }\r\n\r\n        .nav-tab.active {\r\n            background: var(--primary);\r\n            color: white;\r\n        }\r\n\r\n        .card {\r\n            background: var(--card-bg);\r\n            border: 1px solid var(--border);\r\n            border-radius: 8px;\r\n            padding: 1.5rem;\r\n            box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);\r\n        }\r\n\r\n        table {\r\n            width: 100%;\r\n            border-collapse: collapse;\r\n            text-align: left;\r\n        }\r\n\r\n        th, td {\r\n            padding: 0.75rem 1rem;\r\n            border-bottom: 1px solid var(--border);\r\n            font-size: 0.9rem;\r\n        }\r\n\r\n        th {\r\n            color: var(--text-muted);\r\n            font-weight: 600;\r\n        }\r\n\r\n        .btn-unlink {\r\n            background: #fee2e2;\r\n            color: #991b1b;\r\n            border: 1px solid #fca5a5;\r\n            padding: 0.35rem 0.75rem;\r\n            border-radius: 4px;\r\n            cursor: pointer;\r\n            font-size: 0.85rem;\r\n            font-weight: 500;\r\n        }\r\n\r\n        .btn-unlink:hover {\r\n            background: #fecaca;\r\n        }\r\n\r\n        .alert {\r\n            padding: 0.75rem 1rem;\r\n            border-radius: 6px;\r\n            font-size: 0.875rem;\r\n            margin-bottom: 1.25rem;\r\n        }\r\n\r\n        .alert-success {\r\n            background: #dcfce7;\r\n            color: #166534;\r\n        }\r\n\r\n        .alert-danger {\r\n            background: #fee2e2;\r\n            color: #991b1b;\r\n        }\r\n\r\n        .lang-switch a {\r\n            text-decoration: none;\r\n            color: var(--text-muted);\r\n            padding: 0.25rem 0.5rem;\r\n            border-radius: 4px;\r\n            border: 1px solid var(--border);\r\n            font-size: 0.85rem;\r\n        }\r\n\r\n        .lang-switch a.active {\r\n            background: var(--primary);\r\n            color: white;\r\n            border-color: var(--primary);\r\n        }\r\n    </style>\r\n</head>\r\n<body>\r\n\r\n<div class=\"container\">\r\n    <div class=\"header\">\r\n        <div>\r\n            <h1 style=\"color: var(--primary); font-size: 1.75rem;\">");
		jteOutput.setContext("h1", null);
		jteOutput.writeUserContent(msg.get("linkedAccounts.title"));
		jteOutput.writeContent("</h1>\r\n            <p style=\"color: var(--text-muted); font-size: 0.9rem;\">");
		jteOutput.setContext("p", null);
		jteOutput.writeUserContent(msg.get("linkedAccounts.subtitle"));
		jteOutput.writeContent("</p>\r\n        </div>\r\n        <div style=\"display: flex; gap: 1rem; align-items: center;\">\r\n            <div class=\"lang-switch\">\r\n                <a href=\"/profile/accounts?lang=en\"");
		var __jte_html_attribute_1 = "en".equals(msg.getCurrentLang()) ? "active" : "";
		if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_1)) {
			jteOutput.writeContent(" class=\"");
			jteOutput.setContext("a", "class");
			jteOutput.writeUserContent(__jte_html_attribute_1);
			jteOutput.setContext("a", null);
			jteOutput.writeContent("\"");
		}
		jteOutput.writeContent(">EN</a>\r\n                <a href=\"/profile/accounts?lang=km\"");
		var __jte_html_attribute_2 = "km".equals(msg.getCurrentLang()) ? "active" : "";
		if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_2)) {
			jteOutput.writeContent(" class=\"");
			jteOutput.setContext("a", "class");
			jteOutput.writeUserContent(__jte_html_attribute_2);
			jteOutput.setContext("a", null);
			jteOutput.writeContent("\"");
		}
		jteOutput.writeContent(">KM</a>\r\n            </div>\r\n            <a href=\"/\"\r\n               style=\"text-decoration: none; color: var(--text-muted); font-size: 0.9rem;\">");
		jteOutput.setContext("a", null);
		jteOutput.writeUserContent(msg.get("common.back"));
		jteOutput.writeContent("</a>\r\n        </div>\r\n    </div>\r\n\r\n    <div class=\"nav-tabs\">\r\n        <a href=\"/\" class=\"nav-tab\">");
		jteOutput.setContext("a", null);
		jteOutput.writeUserContent(msg.get("nav.overview"));
		jteOutput.writeContent("</a>\r\n        <a href=\"/profile/edit\" class=\"nav-tab\">");
		jteOutput.setContext("a", null);
		jteOutput.writeUserContent(msg.get("nav.editProfile"));
		jteOutput.writeContent("</a>\r\n        <a href=\"/profile/password\" class=\"nav-tab\">");
		jteOutput.setContext("a", null);
		jteOutput.writeUserContent(msg.get("nav.changePassword"));
		jteOutput.writeContent("</a>\r\n        <a href=\"/profile/accounts\" class=\"nav-tab active\">");
		jteOutput.setContext("a", null);
		jteOutput.writeUserContent(msg.get("nav.linkedAccounts"));
		jteOutput.writeContent("</a>\r\n        <a href=\"/profile/apps\" class=\"nav-tab\">");
		jteOutput.setContext("a", null);
		jteOutput.writeUserContent(msg.get("nav.connectedApps"));
		jteOutput.writeContent("</a>\r\n    </div>\r\n\r\n    ");
		if (successMessage != null) {
			jteOutput.writeContent("\r\n        <div class=\"alert alert-success\">");
			jteOutput.setContext("div", null);
			jteOutput.writeUserContent(successMessage);
			jteOutput.writeContent("</div>\r\n    ");
		}
		jteOutput.writeContent("\r\n    ");
		if (errorMessage != null) {
			jteOutput.writeContent("\r\n        <div class=\"alert alert-danger\">");
			jteOutput.setContext("div", null);
			jteOutput.writeUserContent(errorMessage);
			jteOutput.writeContent("</div>\r\n    ");
		}
		jteOutput.writeContent("\r\n\r\n    <div class=\"card\">\r\n        ");
		if (identities != null && !identities.isEmpty()) {
			jteOutput.writeContent("\r\n            <table>\r\n                <thead>\r\n                <tr>\r\n                    <th>");
			jteOutput.setContext("th", null);
			jteOutput.writeUserContent(msg.get("linkedAccounts.provider"));
			jteOutput.writeContent("</th>\r\n                    <th>");
			jteOutput.setContext("th", null);
			jteOutput.writeUserContent(msg.get("linkedAccounts.displayName"));
			jteOutput.writeContent("</th>\r\n                    <th>");
			jteOutput.setContext("th", null);
			jteOutput.writeUserContent(msg.get("register.email"));
			jteOutput.writeContent("</th>\r\n                    <th>");
			jteOutput.setContext("th", null);
			jteOutput.writeUserContent(msg.get("linkedAccounts.linkedAt"));
			jteOutput.writeContent("</th>\r\n                    <th>");
			jteOutput.setContext("th", null);
			jteOutput.writeUserContent(msg.get("common.actions"));
			jteOutput.writeContent("</th>\r\n                </tr>\r\n                </thead>\r\n                <tbody>\r\n                ");
			for (var id : identities) {
				jteOutput.writeContent("\r\n                    <tr>\r\n                        <td><strong>");
				jteOutput.setContext("strong", null);
				jteOutput.writeUserContent(id.provider());
				jteOutput.writeContent("</strong></td>\r\n                        <td>");
				jteOutput.setContext("td", null);
				jteOutput.writeUserContent(id.displayName() != null ? id.displayName() : "-");
				jteOutput.writeContent("</td>\r\n                        <td>");
				jteOutput.setContext("td", null);
				jteOutput.writeUserContent(id.email() != null ? id.email() : "-");
				jteOutput.writeContent("</td>\r\n                        <td style=\"color: var(--text-muted); font-size: 0.8rem;\">");
				jteOutput.setContext("td", null);
				jteOutput.writeUserContent(id.linkedAt() != null ? id.linkedAt().toString() : "-");
				jteOutput.writeContent("</td>\r\n                        <td>\r\n                            <form action=\"/profile/accounts/");
				jteOutput.setContext("form", "action");
				jteOutput.writeUserContent(id.uuid().toString());
				jteOutput.setContext("form", null);
				jteOutput.writeContent("/unlink\" method=\"post\"\r\n                                  style=\"display:inline;\"\r\n                                  onsubmit=\"return confirm('");
				jteOutput.setContext("form", "onsubmit");
				jteOutput.writeUserContent(msg.get("linkedAccounts.unlinkConfirm"));
				jteOutput.setContext("form", null);
				jteOutput.writeContent("');\">\r\n                                ");
				if (_csrf != null) {
					jteOutput.writeContent("\r\n                                    <input type=\"hidden\"");
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
					jteOutput.writeContent(">\r\n                                ");
				}
				jteOutput.writeContent("\r\n                                <button type=\"submit\" class=\"btn-unlink\">");
				jteOutput.setContext("button", null);
				jteOutput.writeUserContent(msg.get("linkedAccounts.unlink"));
				jteOutput.writeContent("</button>\r\n                            </form>\r\n                        </td>\r\n                    </tr>\r\n                ");
			}
			jteOutput.writeContent("\r\n                </tbody>\r\n            </table>\r\n        ");
		} else {
			jteOutput.writeContent("\r\n            <p style=\"color: var(--text-muted);\">");
			jteOutput.setContext("p", null);
			jteOutput.writeUserContent(msg.get("linkedAccounts.noAccounts"));
			jteOutput.writeContent("</p>\r\n        ");
		}
		jteOutput.writeContent("\r\n    </div>\r\n</div>\r\n\r\n</body>\r\n</html>");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		java.util.List<kh.edu.istad.platform.iam.domain.dto.IdentityResult> identities = (java.util.List<kh.edu.istad.platform.iam.domain.dto.IdentityResult>)params.get("identities");
		String successMessage = (String)params.getOrDefault("successMessage",  null);
		String errorMessage = (String)params.getOrDefault("errorMessage",  null);
		String activeTab = (String)params.getOrDefault("activeTab",  "linkedAccounts");
		org.springframework.security.web.csrf.CsrfToken _csrf = (org.springframework.security.web.csrf.CsrfToken)params.getOrDefault("_csrf",  null);
		kh.edu.istad.platform.iam.web.i18n.I18n msg = (kh.edu.istad.platform.iam.web.i18n.I18n)params.get("msg");
		render(jteOutput, jteHtmlInterceptor, identities, successMessage, errorMessage, activeTab, _csrf, msg);
	}
}
