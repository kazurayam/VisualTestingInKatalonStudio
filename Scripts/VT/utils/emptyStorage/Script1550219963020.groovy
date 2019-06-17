import com.kazurayam.materials.MaterialStorage
import com.kazurayam.visualtesting.ManagedGlobalVariable as MGV
import internal.GlobalVariable as GlobalVariable

/**
 * Test Cases/VT/utils/emptyStorage
 * 
 * remove ALL files and directories in the MaterialStorage
 * 
 */
MaterialStorage    ms = (MaterialStorage)   GlobalVariable[MGV.MATERIAL_STORAGE.getName()]

ms.empty()

