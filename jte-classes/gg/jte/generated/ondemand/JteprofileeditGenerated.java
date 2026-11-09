package gg.jte.generated.ondemand;
@SuppressWarnings("unchecked")
@javax.annotation.processing.Generated("gg.jte.TemplateEngine")
public final class JteprofileeditGenerated {
	public static final String JTE_NAME = "profile-edit.jte";
	public static final int[] JTE_LINE_INFO = {0,0,0,0,0,0,0,9,9,9,9,9,9,9,9,9,13,13,13,13,13,13,172,172,172,173,173,173,177,177,177,177,177,177,177,177,177,178,178,178,178,178,178,178,178,178,181,181,181,186,186,186,187,187,187,188,188,188,189,189,189,190,190,190,193,193,194,194,194,195,195,196,196,197,197,197,198,198,202,202,203,203,203,203,203,203,203,203,203,203,203,203,203,203,203,203,203,204,204,208,208,208,210,210,210,210,210,210,210,210,210,213,213,213,215,215,215,215,215,215,215,215,215,221,221,221,222,222,222,222,222,222,222,222,222,225,225,225,229,229,229,229,229,232,232,232,232,232,235,235,235,235,235,243,243,243,245,245,245,245,245,245,245,245,245,248,248,248,250,250,250,250,250,253,253,253,253,253,260,260,260,263,263,263,265,265,265,265,265,265,265,265,265,270,270,270,271,271,271,271,271,271,271,271,271,274,274,274,275,275,275,275,275,275,275,275,275,281,281,281,283,283,283,283,283,283,283,283,283,286,286,286,288,288,288,288,288,288,288,288,288,292,292,292,298,0,0,0,1,2,3,4,5,6,0,0,0};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, kh.edu.istad.platform.iam.web.dto.ProfileEditForm form, kh.edu.istad.platform.iam.domain.dto.UserResult user, String successMessage, String errorMessage, String activeTab, org.springframework.security.web.csrf.CsrfToken _csrf, kh.edu.istad.platform.iam.web.i18n.I18n msg) {
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
		jteOutput.writeUserContent(msg.get("profileEdit.title"));
		jteOutput.writeContent(" - ");
		jteOutput.setContext("title", null);
		jteOutput.writeUserContent(msg.get("common.appName"));
		jteOutput.writeContent("</title>\r\n    <style>\r\n        :root {\r\n            --primary: #4f46e5;\r\n            --primary-hover: #4338ca;\r\n            --bg: #f8fafc;\r\n            --card-bg: #ffffff;\r\n            --text-main: #0f172a;\r\n            --text-muted: #64748b;\r\n            --border: #e2e8f0;\r\n            --danger: #ef4444;\r\n            --success: #22c55e;\r\n        }\r\n\r\n        * {\r\n            box-sizing: border-box;\r\n            margin: 0;\r\n            padding: 0;\r\n        }\r\n\r\n        body {\r\n            font-family: system-ui, -apple-system, sans-serif;\r\n            background-color: var(--bg);\r\n            color: var(--text-main);\r\n            padding: 2rem 1rem;\r\n        }\r\n\r\n        .container {\r\n            max-width: 900px;\r\n            margin: 0 auto;\r\n        }\r\n\r\n        .header {\r\n            display: flex;\r\n            justify-content: space-between;\r\n            align-items: center;\r\n            margin-bottom: 1.5rem;\r\n            padding-bottom: 1rem;\r\n            border-bottom: 1px solid var(--border);\r\n        }\r\n\r\n        .nav-tabs {\r\n            display: flex;\r\n            gap: 0.5rem;\r\n            margin-bottom: 2rem;\r\n            border-bottom: 1px solid var(--border);\r\n            padding-bottom: 0.5rem;\r\n            flex-wrap: wrap;\r\n        }\r\n\r\n        .nav-tab {\r\n            text-decoration: none;\r\n            color: var(--text-muted);\r\n            padding: 0.5rem 1rem;\r\n            border-radius: 6px;\r\n            font-size: 0.9rem;\r\n            font-weight: 500;\r\n        }\r\n\r\n        .nav-tab.active {\r\n            background: var(--primary);\r\n            color: white;\r\n        }\r\n\r\n        .card {\r\n            background: var(--card-bg);\r\n            border: 1px solid var(--border);\r\n            border-radius: 8px;\r\n            padding: 2rem;\r\n            box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);\r\n        }\r\n\r\n        .form-row {\r\n            display: grid;\r\n            grid-template-columns: 1fr 1fr;\r\n            gap: 1rem;\r\n            margin-bottom: 1rem;\r\n        }\r\n\r\n        .form-group {\r\n            margin-bottom: 1rem;\r\n        }\r\n\r\n        label {\r\n            display: block;\r\n            font-size: 0.875rem;\r\n            font-weight: 500;\r\n            margin-bottom: 0.35rem;\r\n        }\r\n\r\n        input, select {\r\n            width: 100%;\r\n            padding: 0.65rem 0.85rem;\r\n            border: 1px solid var(--border);\r\n            border-radius: 6px;\r\n            font-size: 0.95rem;\r\n        }\r\n\r\n        .section-title {\r\n            font-size: 1.1rem;\r\n            font-weight: 600;\r\n            margin: 1.5rem 0 1rem 0;\r\n            padding-bottom: 0.5rem;\r\n            border-bottom: 1px solid var(--border);\r\n        }\r\n\r\n        .btn-primary {\r\n            background: var(--primary);\r\n            color: white;\r\n            border: none;\r\n            padding: 0.75rem 1.5rem;\r\n            border-radius: 6px;\r\n            font-size: 1rem;\r\n            cursor: pointer;\r\n            font-weight: 600;\r\n        }\r\n\r\n        .btn-primary:hover {\r\n            background: var(--primary-hover);\r\n        }\r\n\r\n        .alert {\r\n            padding: 0.75rem 1rem;\r\n            border-radius: 6px;\r\n            font-size: 0.875rem;\r\n            margin-bottom: 1.25rem;\r\n        }\r\n\r\n        .alert-success {\r\n            background: #dcfce7;\r\n            color: #166534;\r\n        }\r\n\r\n        .alert-danger {\r\n            background: #fee2e2;\r\n            color: #991b1b;\r\n        }\r\n\r\n        .lang-switch a {\r\n            text-decoration: none;\r\n            color: var(--text-muted);\r\n            padding: 0.25rem 0.5rem;\r\n            border-radius: 4px;\r\n            border: 1px solid var(--border);\r\n            font-size: 0.85rem;\r\n        }\r\n\r\n        .lang-switch a.active {\r\n            background: var(--primary);\r\n            color: white;\r\n            border-color: var(--primary);\r\n        }\r\n    </style>\r\n</head>\r\n<body>\r\n\r\n<div class=\"container\">\r\n    <div class=\"header\">\r\n        <div>\r\n            <h1 style=\"color: var(--primary); font-size: 1.75rem;\">");
		jteOutput.setContext("h1", null);
		jteOutput.writeUserContent(msg.get("profileEdit.title"));
		jteOutput.writeContent("</h1>\r\n            <p style=\"color: var(--text-muted); font-size: 0.9rem;\">");
		jteOutput.setContext("p", null);
		jteOutput.writeUserContent(msg.get("profileEdit.subtitle"));
		jteOutput.writeContent("</p>\r\n        </div>\r\n        <div style=\"display: flex; gap: 1rem; align-items: center;\">\r\n            <div class=\"lang-switch\">\r\n                <a href=\"/profile/edit?lang=en\"");
		var __jte_html_attribute_1 = "en".equals(msg.getCurrentLang()) ? "active" : "";
		if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_1)) {
			jteOutput.writeContent(" class=\"");
			jteOutput.setContext("a", "class");
			jteOutput.writeUserContent(__jte_html_attribute_1);
			jteOutput.setContext("a", null);
			jteOutput.writeContent("\"");
		}
		jteOutput.writeContent(">EN</a>\r\n                <a href=\"/profile/edit?lang=km\"");
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
		jteOutput.writeContent("</a>\r\n        <a href=\"/profile/edit\" class=\"nav-tab active\">");
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
		jteOutput.writeContent("\r\n\r\n    <div class=\"card\">\r\n        <form action=\"/profile/edit\" method=\"post\">\r\n            ");
		if (_csrf != null) {
			jteOutput.writeContent("\r\n                <input type=\"hidden\"");
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
			jteOutput.writeContent(">\r\n            ");
		}
		jteOutput.writeContent("\r\n\r\n            <div class=\"form-row\">\r\n                <div class=\"form-group\">\r\n                    <label for=\"givenName\">");
		jteOutput.setContext("label", null);
		jteOutput.writeUserContent(msg.get("register.givenName"));
		jteOutput.writeContent("</label>\r\n                    <input type=\"text\" id=\"givenName\" name=\"givenName\"\r\n                          ");
		var __jte_html_attribute_5 = form != null ? form.getGivenName() : "";
		if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_5)) {
			jteOutput.writeContent(" value=\"");
			jteOutput.setContext("input", "value");
			jteOutput.writeUserContent(__jte_html_attribute_5);
			jteOutput.setContext("input", null);
			jteOutput.writeContent("\"");
		}
		jteOutput.writeContent(" required>\r\n                </div>\r\n                <div class=\"form-group\">\r\n                    <label for=\"familyName\">");
		jteOutput.setContext("label", null);
		jteOutput.writeUserContent(msg.get("register.familyName"));
		jteOutput.writeContent("</label>\r\n                    <input type=\"text\" id=\"familyName\" name=\"familyName\"\r\n                          ");
		var __jte_html_attribute_6 = form != null ? form.getFamilyName() : "";
		if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_6)) {
			jteOutput.writeContent(" value=\"");
			jteOutput.setContext("input", "value");
			jteOutput.writeUserContent(__jte_html_attribute_6);
			jteOutput.setContext("input", null);
			jteOutput.writeContent("\"");
		}
		jteOutput.writeContent(" required>\r\n                </div>\r\n            </div>\r\n\r\n            <div class=\"form-row\">\r\n                <div class=\"form-group\">\r\n                    <label for=\"nickname\">");
		jteOutput.setContext("label", null);
		jteOutput.writeUserContent(msg.get("profileEdit.nickname"));
		jteOutput.writeContent("</label>\r\n                    <input type=\"text\" id=\"nickname\" name=\"nickname\"");
		var __jte_html_attribute_7 = form != null ? form.getNickname() : "";
		if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_7)) {
			jteOutput.writeContent(" value=\"");
			jteOutput.setContext("input", "value");
			jteOutput.writeUserContent(__jte_html_attribute_7);
			jteOutput.setContext("input", null);
			jteOutput.writeContent("\"");
		}
		jteOutput.writeContent(">\r\n                </div>\r\n                <div class=\"form-group\">\r\n                    <label for=\"gender\">");
		jteOutput.setContext("label", null);
		jteOutput.writeUserContent(msg.get("profileEdit.gender"));
		jteOutput.writeContent("</label>\r\n                    <select id=\"gender\" name=\"gender\">\r\n                        <option value=\"\">Select Gender</option>\r\n                        <option value=\"male\"\r\n                               ");
		var __jte_html_attribute_8 = "male".equalsIgnoreCase(form != null ? form.getGender() : "");
		if (__jte_html_attribute_8) {
		jteOutput.writeContent(" selected");
		}
		jteOutput.writeContent(">Male\r\n                        </option>\r\n                        <option value=\"female\"\r\n                               ");
		var __jte_html_attribute_9 = "female".equalsIgnoreCase(form != null ? form.getGender() : "");
		if (__jte_html_attribute_9) {
		jteOutput.writeContent(" selected");
		}
		jteOutput.writeContent(">Female\r\n                        </option>\r\n                        <option value=\"other\"\r\n                               ");
		var __jte_html_attribute_10 = "other".equalsIgnoreCase(form != null ? form.getGender() : "");
		if (__jte_html_attribute_10) {
		jteOutput.writeContent(" selected");
		}
		jteOutput.writeContent(">Other\r\n                        </option>\r\n                    </select>\r\n                </div>\r\n            </div>\r\n\r\n            <div class=\"form-row\">\r\n                <div class=\"form-group\">\r\n                    <label for=\"phoneNumber\">");
		jteOutput.setContext("label", null);
		jteOutput.writeUserContent(msg.get("profileEdit.phone"));
		jteOutput.writeContent("</label>\r\n                    <input type=\"text\" id=\"phoneNumber\" name=\"phoneNumber\"\r\n                          ");
		var __jte_html_attribute_11 = form != null ? form.getPhoneNumber() : "";
		if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_11)) {
			jteOutput.writeContent(" value=\"");
			jteOutput.setContext("input", "value");
			jteOutput.writeUserContent(__jte_html_attribute_11);
			jteOutput.setContext("input", null);
			jteOutput.writeContent("\"");
		}
		jteOutput.writeContent(" placeholder=\"+85512345678\">\r\n                </div>\r\n                <div class=\"form-group\">\r\n                    <label for=\"locale\">");
		jteOutput.setContext("label", null);
		jteOutput.writeUserContent(msg.get("profileEdit.locale"));
		jteOutput.writeContent("</label>\r\n                    <select id=\"locale\" name=\"locale\">\r\n                        <option value=\"en\"");
		var __jte_html_attribute_12 = "en".equalsIgnoreCase(form != null ? form.getLocale() : "en");
		if (__jte_html_attribute_12) {
		jteOutput.writeContent(" selected");
		}
		jteOutput.writeContent(">\r\n                            English (en)\r\n                        </option>\r\n                        <option value=\"km\"");
		var __jte_html_attribute_13 = "km".equalsIgnoreCase(form != null ? form.getLocale() : "");
		if (__jte_html_attribute_13) {
		jteOutput.writeContent(" selected");
		}
		jteOutput.writeContent(">\r\n                            ភាសាខ្មែរ (km)\r\n                        </option>\r\n                    </select>\r\n                </div>\r\n            </div>\r\n\r\n            <div class=\"section-title\">");
		jteOutput.setContext("div", null);
		jteOutput.writeUserContent(msg.get("profileEdit.addressHeader"));
		jteOutput.writeContent("</div>\r\n\r\n            <div class=\"form-group\">\r\n                <label for=\"streetAddress\">");
		jteOutput.setContext("label", null);
		jteOutput.writeUserContent(msg.get("profileEdit.streetAddress"));
		jteOutput.writeContent("</label>\r\n                <input type=\"text\" id=\"streetAddress\" name=\"streetAddress\"\r\n                      ");
		var __jte_html_attribute_14 = form != null ? form.getStreetAddress() : "";
		if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_14)) {
			jteOutput.writeContent(" value=\"");
			jteOutput.setContext("input", "value");
			jteOutput.writeUserContent(__jte_html_attribute_14);
			jteOutput.setContext("input", null);
			jteOutput.writeContent("\"");
		}
		jteOutput.writeContent(">\r\n            </div>\r\n\r\n            <div class=\"form-row\">\r\n                <div class=\"form-group\">\r\n                    <label for=\"locality\">");
		jteOutput.setContext("label", null);
		jteOutput.writeUserContent(msg.get("profileEdit.locality"));
		jteOutput.writeContent("</label>\r\n                    <input type=\"text\" id=\"locality\" name=\"locality\"");
		var __jte_html_attribute_15 = form != null ? form.getLocality() : "";
		if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_15)) {
			jteOutput.writeContent(" value=\"");
			jteOutput.setContext("input", "value");
			jteOutput.writeUserContent(__jte_html_attribute_15);
			jteOutput.setContext("input", null);
			jteOutput.writeContent("\"");
		}
		jteOutput.writeContent(">\r\n                </div>\r\n                <div class=\"form-group\">\r\n                    <label for=\"region\">");
		jteOutput.setContext("label", null);
		jteOutput.writeUserContent(msg.get("profileEdit.region"));
		jteOutput.writeContent("</label>\r\n                    <input type=\"text\" id=\"region\" name=\"region\"");
		var __jte_html_attribute_16 = form != null ? form.getRegion() : "";
		if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_16)) {
			jteOutput.writeContent(" value=\"");
			jteOutput.setContext("input", "value");
			jteOutput.writeUserContent(__jte_html_attribute_16);
			jteOutput.setContext("input", null);
			jteOutput.writeContent("\"");
		}
		jteOutput.writeContent(">\r\n                </div>\r\n            </div>\r\n\r\n            <div class=\"form-row\">\r\n                <div class=\"form-group\">\r\n                    <label for=\"postalCode\">");
		jteOutput.setContext("label", null);
		jteOutput.writeUserContent(msg.get("profileEdit.postalCode"));
		jteOutput.writeContent("</label>\r\n                    <input type=\"text\" id=\"postalCode\" name=\"postalCode\"\r\n                          ");
		var __jte_html_attribute_17 = form != null ? form.getPostalCode() : "";
		if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_17)) {
			jteOutput.writeContent(" value=\"");
			jteOutput.setContext("input", "value");
			jteOutput.writeUserContent(__jte_html_attribute_17);
			jteOutput.setContext("input", null);
			jteOutput.writeContent("\"");
		}
		jteOutput.writeContent(">\r\n                </div>\r\n                <div class=\"form-group\">\r\n                    <label for=\"country\">");
		jteOutput.setContext("label", null);
		jteOutput.writeUserContent(msg.get("profileEdit.country"));
		jteOutput.writeContent("</label>\r\n                    <input type=\"text\" id=\"country\" name=\"country\" maxlength=\"2\"\r\n                          ");
		var __jte_html_attribute_18 = form != null ? form.getCountry() : "";
		if (gg.jte.runtime.TemplateUtils.isAttributeRendered(__jte_html_attribute_18)) {
			jteOutput.writeContent(" value=\"");
			jteOutput.setContext("input", "value");
			jteOutput.writeUserContent(__jte_html_attribute_18);
			jteOutput.setContext("input", null);
			jteOutput.writeContent("\"");
		}
		jteOutput.writeContent(" placeholder=\"KH\">\r\n                </div>\r\n            </div>\r\n\r\n            <button type=\"submit\" class=\"btn-primary\" style=\"margin-top: 1rem;\">");
		jteOutput.setContext("button", null);
		jteOutput.writeUserContent(msg.get("common.save"));
		jteOutput.writeContent("</button>\r\n        </form>\r\n    </div>\r\n</div>\r\n\r\n</body>\r\n</html>");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		kh.edu.istad.platform.iam.web.dto.ProfileEditForm form = (kh.edu.istad.platform.iam.web.dto.ProfileEditForm)params.get("form");
		kh.edu.istad.platform.iam.domain.dto.UserResult user = (kh.edu.istad.platform.iam.domain.dto.UserResult)params.get("user");
		String successMessage = (String)params.getOrDefault("successMessage",  null);
		String errorMessage = (String)params.getOrDefault("errorMessage",  null);
		String activeTab = (String)params.getOrDefault("activeTab",  "editProfile");
		org.springframework.security.web.csrf.CsrfToken _csrf = (org.springframework.security.web.csrf.CsrfToken)params.getOrDefault("_csrf",  null);
		kh.edu.istad.platform.iam.web.i18n.I18n msg = (kh.edu.istad.platform.iam.web.i18n.I18n)params.get("msg");
		render(jteOutput, jteHtmlInterceptor, form, user, successMessage, errorMessage, activeTab, _csrf, msg);
	}
}
