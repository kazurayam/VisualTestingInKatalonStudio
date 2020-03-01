package com.kazurayam.visualtesting

import java.nio.file.Path

public class CapturedURLs {

	Map<String, List<CapturedURL>> sections_

	CapturedURLs() {
		sections_ = new HashMap<String, List<CapturedURL>>()
	}

	void add(String sectionName, String description, URL url, Path screenshotPath) {
		Objects.requireNonNull(sectionName, "sectionName must not be null")
		Objects.requireNonNull(description, "description must not be null")
		Objects.requireNonNull(url, "url must not be null")
		Objects.requireNonNull(screenshotPath, "screenshotPath must not be null")
		List<CapturedURL> list
		if (sections_.containsKey(sectionName)) {
			list = sections_.get(sectionName)
		} else {
			list = new ArrayList<CapturedURL>()
		}
		CapturedURL entry = new CapturedURL(description, url, screenshotPath)
		list.add(entry)
		sections_.put(sectionName, list)
	}

	@Override
	String toString() {
		return this.toJsonText()
	}

	String toJsonText() {
		StringBuilder sb = new StringBuilder()
		sb.append("{")
		sb.append("\"CapturedURLs\":[")
		int c1 = 0
		List<String> sNames = sections_.keySet() as List
		for (String sectionName in sNames.sort()) {
			if (c1 > 0) {
				sb.append(",")
			}
			sb.append("{")
			sb.append("\"sectionName\":\"")
			sb.append(sectionName)
			sb.append("\",")
			sb.append("\"entries\":")
			sb.append("[")
			List<CapturedURL> list = sections_.get(sectionName)
			int c2 = 0
			for (CapturedURL entry in list) {
				if (c2 > 0) {
					sb.append(",")
				}
				sb.append(entry.toJsonText())
				c2 += 1
			}
			sb.append("]")
			sb.append("}")
			c1 += 1
		}
		sb.append("]")
		sb.append("}")
		return sb.toString()
	}

	void serializeMarkdown(Writer wrt) {
		PrintWriter pw = new PrintWriter(wrt)
		List<String> sectionNames = this.sections_.keySet() as List
		for (String sectionName in sectionNames.sort()) {
			pw.println("### ${sectionName}")
			pw.println("| desc | URL | Path |")
			pw.println("|:-----|:----|:-----|")
			for (CapturedURL entry in this.sections_.get(sectionName)) {
				pw.println("| ${entry.getDescription()} | ${entry.getURL().toExternalForm()} | ${entry.getMaterialPath().toString()} |")
			}
			pw.println("")
		}
	}

	void serializeText(Writer wrt) {
		PrintWriter pw = new PrintWriter(wrt)
		List<String> sectionNames = this.sections_.keySet() as List
		for (String sectionName in sectionNames.sort()) {
			pw.println("${sectionName}")
			for (CapturedURL entry in this.sections_.get(sectionName)) {
				pw.println("${entry.getDescription()}\t${entry.getURL().toExternalForm()}\t${entry.getMaterialPath().toString()}")
			}
			pw.println("")
		}
	}

	class CapturedURL {
		private String description_
		private URL url_
		private Path materialPath_

		CapturedURL(String description, URL url, Path materialPath) {
			Objects.requireNonNull(description, "description must not be null")
			Objects.requireNonNull(url, "url must not be null")
			Objects.requireNonNull(materialPath, "materialPath must not be null")
			this.description_ = description
			this.url_ = url
			this.materialPath_ = materialPath
		}

		String getDescription() {
			return this.description_
		}

		URL getURL() {
			return this.url_
		}

		Path getMaterialPath() {
			return this.materialPath_
		}

		@Override
		String toString() {
			return this.toJsonText()
		}

		String toJsonText() {
			StringBuilder sb = new StringBuilder()
			sb.append("{")
			sb.append("\"description\":")
			sb.append("\"")
			sb.append(this.getDescription())
			sb.append("\",")
			sb.append("\"url\":")
			sb.append("\"")
			sb.append(this.getURL().toExternalForm())
			sb.append("\"")
			sb.append(",")
			sb.append("\"materialPath\":")
			sb.append("\"")
			sb.append(this.getMaterialPath().toString())
			sb.append("\"")
			sb.append("}")
			return sb.toString()
		}

		@Override
		boolean equals(Object obj) {
			if (obj == null) return false
			if (!(obj instanceof CapturedURL)) return false
			CapturedURL other = (CapturedURL)obj
			return this.getURL().equals(other.getURL()) &&
					this.getMaterialPath().equals(other.getMaterialPath())
		}

		@Override
		int hashCode() {
			int result = 17
			result = 31 * result + this.getDescription().hashCode()
			result = 31 * result + this.getURL().hashCode()
			result = 31 * result + this.getMaterialPath().hashCode()
			return result
		}
	}
}
