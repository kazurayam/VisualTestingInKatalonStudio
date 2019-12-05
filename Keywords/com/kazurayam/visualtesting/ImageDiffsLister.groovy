package com.kazurayam.visualtesting

import java.nio.file.Files
import java.nio.file.Path

import groovy.json.JsonSlurper

/**
 * Parse a comparison-result-bundle file produced by com.kazurayam.visualtesting.ImageDiffer,
 * extract the information of PNG files as diff images,
 * and compile a text in Markdown format (a table).
 */
public class ImageDiffsLister {

	private Path input_

	/**
	 * @param comparisonResultBundle
	 */
	ImageDiffsLister(Path comparisonResultBundle) {
		if ( !Files.exists(comparisonResultBundle) ) {
			throw new FileNotFoundException("${comparisonResultBundle} is not found")
		}
		this.input_ = comparisonResultBundle
	}

	String toMarkdown() {
		JsonSlurper slurper = new JsonSlurper()
		def obj = slurper.parse(input_.toFile())

		//println JsonOutput.prettyPrint(JsonOutput.toJson(obj))
		StringBuilder sb = new StringBuilder()
		sb.append("|file name|diff％|criteria%|diff > criteria ?|\n")
		sb.append("|---------|------|---|---|\n")

		for (def cr in obj.ComparisonResultBundle) {
			def comparisonResult = cr.ComparisonResult
			def href = comparisonResult.diffMaterial.Material.hrefRelativeToRepositoryRoot
			List<String> pathElements = href.split('/') as List
			def fileName = pathElements.last()
			StringBuilder line = new StringBuilder()
			line.append('|')
			line.append(fileName)
			line.append('|')
			line.append(comparisonResult.diffRatio)
			line.append('|')
			line.append(comparisonResult.criteriaPercentage)
			line.append('|')
			line.append((comparisonResult.imagesAreSimilar) ? '' : 'X')
			line.append('|')
			line.append("\n")
			sb.append(line)
		}

		return sb.toString()
	}
}