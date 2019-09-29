import java.nio.file.Path

import com.kazurayam.materials.Indexer
import com.kazurayam.materials.IndexerFactory
import com.kazurayam.materials.MaterialRepository
import com.kazurayam.materials.ReportsAccessor
import com.kazurayam.visualtesting.ManagedGlobalVariable as MGV

import internal.GlobalVariable as GlobalVariable

/**
 * Test Cases/VT/makeIndex
 * 
 * This test case creates ./Materials/index.html file.
 * The index.html shows the result of screenshots comparison tests.
 * 
 * This test case just read files from the local file system and write a file into the local file system.
 * This test case makes no interaction with web.
 */


// resolve the directories to read/write
MaterialRepository mr = (MaterialRepository)GlobalVariable[MGV.MATERIAL_REPOSITORY.getName()]
ReportsAccessor    ra = (ReportsAccessor)   GlobalVariable[MGV.REPORTS_ACCESSOR.getName()]
Path baseDir          = mr.getBaseDir()
Path reportsDir       = ra.getReportsDir()
Path index            = baseDir.resolve('index.html')

// create an Indexer object
Indexer indexer = IndexerFactory.newIndexer('com.kazurayam.materials.view.IndexerConcise')
indexer.setBaseDir(baseDir)
indexer.setReportsDir(reportsDir)
indexer.setOutput(index)

// finally we generate the index.html
indexer.execute()
