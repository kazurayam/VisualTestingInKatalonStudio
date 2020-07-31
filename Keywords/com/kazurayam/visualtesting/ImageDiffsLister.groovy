package com.kazurayam.visualtesting

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

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
		List<Object> comparisonResultList = sortComparisonResultsByDiffRatio(slurper.parse(input_.toFile()))
		//println JsonOutput.prettyPrint(JsonOutput.toJson(obj))
		StringBuilder sb = new StringBuilder()
		sb.append("|file name|description|diff|criteria|diff>criteria?|\n")
		sb.append("|---------|-----------|----|--------|--------------|\n")
		for (entry in comparisonResultList) {
			def href = entry.ComparisonResult.diffMaterial.Material.hrefRelativeToRepositoryRoot
			List<String> pathElements = href.split('/') as List
			def fileName = pathElements.last()
			StringBuilder line = new StringBuilder()
			line.append('|')
			line.append(fileName)
			line.append('|')
			line.append(entry.ComparisonResult.MaterialDescription.description.replace('|',' '))
			line.append('|')
			line.append(entry.ComparisonResult.diffRatio)
			line.append('|')
			line.append(entry.ComparisonResult.criteriaPercentage)
			line.append('|')
			line.append((entry.ComparisonResult.imagesAreSimilar) ? '' : 'X')
			line.append('|')
			line.append("\n")
			sb.append(line)
		}
		return sb.toString()
	}

	String toCsv() {
		JsonSlurper slurper = new JsonSlurper()
		List<Object> comparisonResultList = sortComparisonResultsByDiffRatio(slurper.parse(input_.toFile()))
		//println JsonOutput.prettyPrint(JsonOutput.toJson(obj))
		StringBuilder sb = new StringBuilder()
		sb.append("file name,description,diff,criteria,diff>criteria?")
		sb.append("\n")
		for (entry in comparisonResultList) {
			def href = entry.ComparisonResult.diffMaterial.Material.hrefRelativeToRepositoryRoot
			List<String> pathElements = href.split('/') as List
			def fileName = pathElements.last()
			StringBuilder line = new StringBuilder()
			line.append(fileName)
			line.append(',')
			line.append(entry.ComparisonResult.MaterialDescription.description.replace(',', ' '))
			line.append(',')
			line.append(entry.ComparisonResult.diffRatio)
			line.append(',')
			line.append(entry.ComparisonResult.criteriaPercentage)
			line.append(',')
			line.append((entry.ComparisonResult.imagesAreSimilar) ? '' : 'X')
			line.append("\n")
			sb.append(line)
		}
		return sb.toString()
	}


	private List<Object> sortComparisonResultsByDiffRatio(Object obj) {
		List<Object> comparisonResultList = obj.ComparisonResultBundle
		comparisonResultList.sort { a, b ->
			int result = (a.ComparisonResult.diffRatio <=> b.ComparisonResult.diffRatio) * (-1)
			if (result == 0) {
				result = (a.ComparisonResult.diffMaterial.Material.hrefRelativeToRepositoryRoot
						<=> b.ComparisonResult.diffMaterial.Material.hrefRelativeToRepositoryRoot)
			}
			return result
		}
		return comparisonResultList
	}
}