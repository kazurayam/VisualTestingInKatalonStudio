package com.kazurayam.ksbackyard

import com.kms.katalon.core.annotation.Keyword

public class URLParser {

	@Keyword
	static Map<String,String> queryParameters(String urlString) {
		URL url = new URL(urlString)
		// get all query params as list
		def queryParams = url.query?.split('&') // safe operator for urls without query params
		// transform the params list to a Map spliting
		// each query param
		return queryParams.collectEntries { param -> param.split('=').collect { URLDecoder.decode(it,'UTF-8') }}
	}

	static String queryParameter(String urlString, String name) {
		def mapParams = queryParameters(urlString)
		return mapParams[name]
	}
}