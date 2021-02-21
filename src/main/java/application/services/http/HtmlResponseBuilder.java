/**
 * HttpTemplater.java is part of the "SheetsIO" project (c) by Mark "Grandy" Bishop, 2021.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package application.services.http;

/**
 * Builder mechanism for the Html templating of various file types.
 *
 * @author Mark "Grandy" Bishop
 */
public class HtmlResponseBuilder {

	static String CORE_TEMPLATE = "<html>" //
			+ "	<head>" //
			+ "		<script type=\"text/javascript\" src=\"http://livejs.com/live.js\"></script>" //
			+ "		<style>body { margin: 0; padding: 0; } <scale> </style>" //
			+ "	</head>" //
			+ "	<body>" //
			+ "		<content></content>" //
			+ " </body>" //
			+ "</html>";
	static String SCALE_CSS = "#content { width: 100%; height: 100%; }";
	static String IFRAME_TEMPLATE = "<iframe src=\"%s\" id=\"content\" frameborder=\"0\"></iframe>";
	static String IMG_TEMPLATE = "<img src=\"%s\" id=\"content\" />";
	static String DIV_TEMPLATE = "<div id=\"content\"><span id=\"text\">%s</span></div>";
	static String VIDEO_TEMPLATE = "<video width=\"100%\" height=\"100%\" autoplay loop><source src=\"<src>\" type=\"<type>\" autoplay></video>";

	static String EMPTY_IMG_SRC = "data:image/gif;base64,R0lGODlhAQABAIAAAP///wAAACH5BAEAAAAALAAAAAABAAEAAAICRAEAOw==";

	private String innerContent;
	private boolean scale = true;

	public String build() {
		return formatCoreTemplate(innerContent, scale);
	}

	/**
	 * @param url
	 *            e.g. 'https://obs.ninja'
	 * @return a html doc with an <iframe> tag for a given src url
	 */
	public HtmlResponseBuilder buildIframeTemplate(String url) {
		this.innerContent = String.format(IFRAME_TEMPLATE, url);
		return this;
	}

	/**
	 * @param url
	 *            e.g. 'https://i.imgur.com/asd123.png'
	 * @return a html doc with an <img> tag for a given src url
	 */
	public HtmlResponseBuilder buildImgTemplate(String url) {
		String src = EMPTY_IMG_SRC;
		if (url != null && !url.trim().isEmpty()) {
			src = url;
		}
		this.innerContent = String.format(IMG_TEMPLATE, src);
		return this;
	}

	/**
	 * @param text
	 *            e.g. 'hello there'
	 * @return a html doc with a <div> tag for a given src url
	 */
	public HtmlResponseBuilder buildDivTemplate(String text) {
		this.innerContent = String.format(DIV_TEMPLATE, text);
		return this;
	}

	/**
	 * @param url
	 *            e.g. '/path/to/dir/files/AoE2/thing.webm'
	 * @param type
	 *            e.g. 'video/webm'
	 * @return a html doc with a <video> tag for a given src url and content-type
	 */
	public HtmlResponseBuilder buildVideoTemplate(String url, String type) {
		this.innerContent = VIDEO_TEMPLATE.replace("<src>", url).replace("<type>", type);
		return this;
	}

	/** @return an empty page, no value. */
	public HtmlResponseBuilder empty() {
		this.innerContent = "";
		return this;
	}

	/**
	 * @return a builder that either scales it content to 100% w/h, or doesn't try.
	 */
	public HtmlResponseBuilder scale(boolean scale) {
		this.scale = scale;
		return this;
	}

	/** Format the CORE_TEMPLATE to replace the placeholder with a value. */
	private static String formatCoreTemplate(String value, boolean scale) {
		return CORE_TEMPLATE.replace("<content></content>", value).replace("<scale>", scale ? SCALE_CSS : "");
	}
}
