package com.kazurayam.visualtesting

import java.nio.file.Path

public class CapturedURLs {
	
	Map<String, List<CapturedURL>> sections_
	
	CapturedURLs() {
		sections_ = new HashMap<String, List<CapturedURL>>()	
	}
	
	void add(String description, URL url, Path screenshotPath) {
		Objects.requireNonNull(description, "description must not be null")
		Objects.requireNonNull(url, "url must not be null")
		Objects.requireNonNull(screenshotPath, "screenshotPath must not be null")
		List<CapturedURL> list
		if (sections_.containsKey(description)) {
			list = sections_.get(description)
		} else {
			list = new ArrayList<CapturedURL>()
		}
		CapturedURL entry = new CapturedURL(url, screenshotPath)
		list.add(entry)
		sections_.put(description, list)
	}
	
	@Override
	String toString() {
		return this.toJsonText()
	}
	
	String toJsonText() {
		StringBuilder sb = new StringBuilder()
		sb.append("{")
		sb.append("\"CapturedURLs\":[")
		for (String desc in sections_.keySet()) {
			sb.append("{")
			sb.append("\"description\":\"")
			sb.append(desc)
			sb.append("\",[")
			List<CapturedURL> list = sections_.get(desc)
			int count = 0
			for (CapturedURL entry in list) {
				if (count > 0) {
					sb.append(",")
				}
				sb.append(entry.toJsonText())
				count += 1
			}
			sb.append("]}")
		}
		sb.append("]")
		sb.append("}")
		return sb.toString()
	}
	
	void serializeInMarkdownFormat(Writer writer) {
		
	}
	
	void serializeInCSVFormat(Writer writer) {
		
	}
	
	
	class CapturedURL {
		private URL url_
		private Path screenshotPath_
		
		CapturedURL(URL url, Path screenshotPath) {
			this.url_ = url
			this.screenshotPath_ = screenshotPath
		}
		
		URL getURL() {
			return this.url_
		}
		
		Path getScreenshotPath() {
			return this.screenshotPath_
		}
		
		@Override
		String toString() {
			return this.toJsonText()
		}
		
		String toJsonText() {
			StringBuilder sb = new StringBuilder()
			sb.append("{")
			sb.append("\"url\":")
			sb.append("\"")
			sb.append(this.getURL().toExternalForm())
			sb.append("\"")
			sb.append(",")
			sb.append("\"screenshotPath\":")
			sb.append("\"")
			sb.append(this.getScreenshotPath().toString())
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
				this.getScreenshotPath().equals(other.getScreenshotPath())
		}
		
		@Override
		int hashCode() {
			int result = 17
			result = 31 * result + this.getURL().hashCode()
			result = 31 * result + this.getScreenshotPath().hashCode()
			return result
		}
	}
}
