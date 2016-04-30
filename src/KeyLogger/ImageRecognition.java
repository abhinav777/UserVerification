package KeyLogger;

import java.io.BufferedReader;
import java.io.InputStreamReader;

class ImageRecognition {

	private String username;

	ImageRecognition(String username) {
		this.username = username;
	}

	public int Image() {
		System.out.println(username);
		String command = "python test.py";
		String command1 = "python crop_resize.py";
		String command2 = "python pyfacesdemo outputimage0.png /home/agrim/image_data/Agrim 4 3";

		String output = executeCommand(command);
		System.out.println(output);

		String output1 = executeCommand(command1);
		System.out.println(output1);

		String output2 = executeCommand(command2);
		System.out.println(output2);

		return 0;
	}

	private String executeCommand(String command) {
		StringBuffer output = new StringBuffer();
		Process p;
		try {
			p = Runtime.getRuntime().exec(command);
			p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			String line = "";
			while ((line = reader.readLine()) != null) {
				output.append(line + "\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return output.toString();
	}
}