import java.nio.file.Paths

import com.kazurayam.materials.Helpers

/**
 * delete the contents under the ./Materials directory
 * while preseving it undeleted
 */
Helpers.deleteDirectoryContents(Paths.get('./Materials'))