/**
 * Test Cases/VT/utils/executeSystemCommandExample
 */

String CMD =
		"ipconfig";

// Run the Windows command
Process process = Runtime.getRuntime().exec(CMD)

// Get input streams
BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()))
BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()))

// Read command standard output
String s
StringBuilder sb = new StringBuilder()
sb.append("Standard output: \n")
while ((s = stdInput.readLine()) != null) {
	sb.append(s + "\n")
}

// Read command errors
sb.append("Standard error: \n")
while ((s = stdError.readLine()) != null) {
	sb.append(s+ "\n")
}

System.out.println(sb.toString())
