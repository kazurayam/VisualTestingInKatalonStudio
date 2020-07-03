package com.kazurayam.visualtesting

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import static org.hamcrest.CoreMatchers.*
import static org.junit.Assert.*
import com.kms.katalon.core.configuration.RunConfiguration
import org.junit.Before
import org.junit.Test
import static org.junit.Assert.*
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4.class)
class ImageDiffsListerTest{

	private Path comparisonResultBundle

	@Before
	void setup() {
		comparisonResultBundle = Paths.get(RunConfiguration.getProjectDir())
				.resolve("Include")
				.resolve("fixture")
				.resolve("Materials/CURA.chronos_exam")
				.resolve("CURA_DevelopmentEnv")
				.resolve("20200703_150015")
				.resolve("CURA.ImageDiff_chronos")
				.resolve("comparison-result-bundle.json")
	}

	@Test
	void test_toMarkDown() {
		// when:
		ImageDiffsLister lister = new ImageDiffsLister(comparisonResultBundle)
		String markdown = lister.toMarkdown()
		println("test_toMarkDown(): markdown=${markdown}")
		// then:
		assertTrue(markdown.contains('''|file name|diff|criteria|diff>criteria?|'''))
		assertTrue(markdown.contains('''|---------|------|---|---|'''))
		assertTrue(markdown.contains('''|home(1.02).png|1.02|2.0||'''))
		assertTrue(markdown.contains('''|トップ(1.02).png|1.02|2.0||'''))
		assertTrue(markdown.contains('''|default%23appointment(0.11).png|0.11|2.0||'''))
		assertTrue(markdown.contains('''|appointment.php%23summary(0.01).png|0.01|2.0||'''))
		assertTrue(markdown.contains('''|profile.php%23login(0.00).png|0.0|2.0||'''))
		assertTrue(markdown.contains('''|revisited(0.00).png|0.0|2.0||'''))
		// when:
		// assert that the rows are sorted by the descending order of the diff%
		List<String> lines = new ArrayList<String>()
		BufferedReader br = new BufferedReader(new StringReader(markdown))
		String line
		while ((line = br.readLine()) != null) {
			lines.add(line)
		}
		assertEquals('''|home(1.02).png|1.02|2.0||''', lines[2])
	}

	@Test
	void test_toCSV() {
		// when:
		ImageDiffsLister lister = new ImageDiffsLister(comparisonResultBundle)
		String csv = lister.toCsv()
		println("test_toCSV(): markdown=${csv}")
		// then:
		assertTrue(csv.contains('''file name,diff,criteria,diff>criteria?'''))
		assertTrue(csv.contains('''home(1.02).png,1.02,2.0,'''))
		assertTrue(csv.contains('''トップ(1.02).png,1.02,2.0,'''))
		assertTrue(csv.contains('''default%23appointment(0.11).png,0.11,2.0,'''))
		assertTrue(csv.contains('''appointment.php%23summary(0.01).png,0.01,2.0,'''))
		assertTrue(csv.contains('''profile.php%23login(0.00).png,0.0,2.0,'''))
		assertTrue(csv.contains('''revisited(0.00).png,0.0,2.0,'''))
		// when:
		// assert that the rows are sorted by descending order of diffRatio + ascending order of fileName
		List<String> lines = new ArrayList<String>()
		BufferedReader br = new BufferedReader(new StringReader(csv))
		String line
		while ((line = br.readLine()) != null) {
			lines.add(line)
		}
		assertEquals('''トップ(1.02).png,1.02,2.0,''', lines[2])
	}
}
