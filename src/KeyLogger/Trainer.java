package KeyLogger;

import be.ac.ulg.montefiore.run.jahmm.*;
import be.ac.ulg.montefiore.run.jahmm.io.FileFormatException;
import be.ac.ulg.montefiore.run.jahmm.io.HmmWriter;
import be.ac.ulg.montefiore.run.jahmm.io.ObservationSequencesReader;
import be.ac.ulg.montefiore.run.jahmm.io.ObservationVectorReader;
import be.ac.ulg.montefiore.run.jahmm.io.OpdfMultiGaussianWriter;
import be.ac.ulg.montefiore.run.jahmm.learn.BaumWelchScaledLearner;
import be.ac.ulg.montefiore.run.jahmm.learn.KMeansLearner;
import be.ac.ulg.montefiore.run.jahmm.learn.BaumWelchLearner;
import be.ac.ulg.montefiore.run.jahmm.ObservationVector;
import be.ac.ulg.montefiore.run.jahmm.OpdfMultiGaussianFactory;

import java.io.*;
import java.util.*;

public class Trainer {
	private List<List<ObservationVector>> sequences;
	private KMeansLearner<ObservationVector> kml;
	private OpdfMultiGaussianFactory gMix;
	private Reader reader;
	private Hmm<ObservationVector> hmm;
	private FileWriter writer;
	private OpdfMultiGaussianWriter opdfWriter;
	private String username;
	private String password;

	public static void main(String args[]) throws IOException,
			FileFormatException {
		// Trainer t = new Trainer ();
		// t.train();
	}

	public Trainer(String username, String password) {
		this.username = username;
		System.out.println("trainer username : " + this.username);
		password = this.password;
	}

	public int train() throws IOException, FileFormatException {
		sequences = getSequences();
		hmm = createHmm();
		writer = new FileWriter(username + "\\hmm-" + username
				+ "-unmodified.txt");
		opdfWriter = new OpdfMultiGaussianWriter();
		HmmWriter.write(writer, opdfWriter, hmm);
		writer.close();
		FileCorrect fileC = new FileCorrect();
		fileC.correct(username);
		return 0;
	}

	public List<List<ObservationVector>> getSequences() throws IOException,
			FileFormatException {

		// reader = new FileReader ("trainingData.txt");
		reader = new FileReader(username + "\\train-" + username + ".txt");
		List<List<ObservationVector>> v = ObservationSequencesReader
				.readSequences(new ObservationVectorReader(2), reader);
		reader.close();
		return v;

	}

	Hmm<ObservationVector> createHmm() {
		gMix = new OpdfMultiGaussianFactory(2);
		kml = new KMeansLearner<ObservationVector>(2, gMix, sequences);
		Hmm<ObservationVector> hm = kml.learn();

		BaumWelchLearner bwl = new BaumWelchScaledLearner();
		bwl.setNbIterations(40);
		bwl.learn(hm, sequences);
		return hm;
	}
}
