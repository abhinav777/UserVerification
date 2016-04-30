package KeyLogger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringTokenizer;

public class FileCorrect {

	public int correct(String username) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(username
				+ "\\hmm-" + username + "-unmodified.txt"));
		BufferedWriter wr = new BufferedWriter(new FileWriter(username
				+ "\\hmm-" + username + ".txt", false));
		String sCurrentLine;

		while ((sCurrentLine = br.readLine()) != null) {
			StringTokenizer tokenizer = new StringTokenizer(sCurrentLine);
			String token;
			while (tokenizer.hasMoreTokens()) {
				token = tokenizer.nextToken();
				if (token.contains(",")) {
					String elements[] = new String[2];
					elements = token.split(",");
					System.out.println(elements[0] + "    " + elements[1]);
					token = elements[0] + elements[1];
					System.out.println(token);
					// token = elements[0];
				}
				wr.append(token + " ");
			}
			wr.newLine();
		}
		br.close();
		wr.flush();
		wr.close();
		return 0;
	}
}
