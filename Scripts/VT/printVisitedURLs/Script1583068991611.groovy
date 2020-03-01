import java.nio.file.Path

import com.kazurayam.materials.MaterialRepository
import com.kazurayam.materials.metadata.MaterialMetadataBundle
import com.kazurayam.visualtesting.ManagedGlobalVariable as MGV

import internal.GlobalVariable as GlobalVariable

// resolve the directories to read/write
MaterialRepository mr = (MaterialRepository)GlobalVariable[MGV.MATERIAL_REPOSITORY.getName()]
Path baseDir          = mr.getBaseDir()
File markdown         = baseDir.resolve(MaterialMetadataBundle.URLS_MARKDOWN_FILE_NAME).toFile()
File tsv              = baseDir.resolve(MaterialMetadataBundle.URLS_TSV_FILE_NAME).toFile()

// print Visited URL List in Markdown format
mr.printVisitedURLsAsMarkdown(new FileWriter(markdown))

// print Visited URL List in Tab-seperated-values format
mr.printVisitedURLsAsTSV(new FileWriter(tsv))