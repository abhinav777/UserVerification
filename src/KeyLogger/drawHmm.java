package KeyLogger;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import be.ac.ulg.montefiore.run.jahmm.Hmm;
import be.ac.ulg.montefiore.run.jahmm.ObservationVector;
import be.ac.ulg.montefiore.run.jahmm.draw.GenericHmmDrawerDot;
import be.ac.ulg.montefiore.run.jahmm.io.FileFormatException;
import be.ac.ulg.montefiore.run.jahmm.io.HmmReader;
import be.ac.ulg.montefiore.run.jahmm.io.OpdfMultiGaussianReader;

public class drawHmm {
	private Reader reader;
	private OpdfMultiGaussianReader opdfReader;
	private Hmm<ObservationVector> hmm;
	
	public static void main (String args[]) throws IOException, FileFormatException  {
		drawHmm draw = new drawHmm ();
		draw.draw();
	}
	
	int draw () throws IOException, FileFormatException {
		reader = new FileReader ("abhinavgupta\\hmm-abhinavgupta.txt");
		opdfReader = new OpdfMultiGaussianReader ();
		hmm = HmmReader.read(reader, opdfReader);
		String fileOutput = "file.dot";
		GenericHmmDrawerDot  drawer = new GenericHmmDrawerDot ();
		drawer.write(hmm, fileOutput);
		return 0;
	}
}
