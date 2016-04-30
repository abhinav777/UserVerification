package KeyLogger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.LinkedList;
import java.util.List;

import be.ac.ulg.montefiore.run.jahmm.*;
import be.ac.ulg.montefiore.run.jahmm.io.FileFormatException;
import be.ac.ulg.montefiore.run.jahmm.io.HmmReader;
import be.ac.ulg.montefiore.run.jahmm.io.ObservationSequencesReader;
import be.ac.ulg.montefiore.run.jahmm.io.ObservationVectorReader;
import be.ac.ulg.montefiore.run.jahmm.io.OpdfMultiGaussianReader;
import be.ac.ulg.montefiore.run.jahmm.ObservationVector;

public class Scorer {
	private Reader reader;
	private static Reader read;
	private OpdfMultiGaussianReader opdfReader;
	private Hmm<ObservationVector> hmm;
	private ViterbiCalculator viterbiCalc;
	private double probability;
	private List<List<ObservationVector>> sequences;
	private BufferedWriter out;
	private String username;
	private String password;

	/*
	 * public static void main (String args[]) throws IOException,
	 * FileFormatException { Scorer sc = new Scorer (); List <ObservationVector>
	 * sequence = new LinkedList <ObservationVector> ();
	 * 
	 * List<List<ObservationVector>> v ; v = sc.readFile (); //sequence =
	 * sc.readScoreFile(); sequence = v.get(0); sc.getProbability (sequence); //
	 * sc.getThreshold(0); }
	 */

	public Scorer(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public double getProbability(List<ObservationVector> sequence)
			throws IOException, FileFormatException {
		reader = new FileReader(username + "\\hmm-" + username + ".txt");
		opdfReader = new OpdfMultiGaussianReader();
		hmm = HmmReader.read(reader, opdfReader);
		reader.close();
		viterbiCalc = new ViterbiCalculator(sequence, hmm);
		probability = viterbiCalc.lnProbability();
		System.out.println("probability : " + probability);
		System.out.println();
		int a[] = new int[100];
		a = viterbiCalc.stateSequence();
		for (int i = 0; i < a.length; ++i)
			System.out.println(a[i]);
		getThreshold();
		return probability;
	}

	public int getThreshold() throws IOException, FileFormatException {
		Trainer trainer = new Trainer(username, password);
		sequences = trainer.getSequences();
		double mean = 0;
		double standardDeviation;
		double extendedStandardDeviation;
		double probabilities[] = new double[150];

		for (int i = 0; i < sequences.size(); ++i) {
			ViterbiCalculator calc = new ViterbiCalculator(sequences.get(i),
					hmm);
			probabilities[i] = calc.lnProbability();
		}

		System.out.println();
		mean = getMean(sequences, probabilities);
		System.out.println(mean);

		standardDeviation = getStandardDeviation(sequences.size(),
				probabilities, mean);
		extendedStandardDeviation = standardDeviation + (0.1)
				* standardDeviation;
		writeMean(mean, standardDeviation, extendedStandardDeviation);
		return 0;
	}

	public List<List<ObservationVector>> readFile(String userName)
			throws IOException, FileFormatException {
		read = new FileReader(username + "\\score-" + userName + ".txt");
		List<List<ObservationVector>> v = ObservationSequencesReader
				.readSequences(new ObservationVectorReader(2), read);
		read.close();
		return v;
	}

	public List<ObservationVector> readScoreFile(String userName)
			throws IOException, FileFormatException {
		List<ObservationVector> sequence = new LinkedList<ObservationVector>();
		read = new FileReader(username + "\\score-" + userName + ".txt");
		List<List<ObservationVector>> v = ObservationSequencesReader
				.readSequences(new ObservationVectorReader(2), read);
		read.close();
		sequence = v.get(0);
		return sequence;
	}

	private int writeMean(double mean, double standardDeviation,
			double extendedStandardDeviation) throws IOException {
		out = new BufferedWriter(new FileWriter(username + "\\meanSd-"
				+ username + ".txt", false));
		out.append(Double.toString(mean));
		out.newLine();
		out.append(Double.toString(standardDeviation));
		out.newLine();
		out.append(Double.toString(extendedStandardDeviation));
		out.flush();
		out.close();
		System.out.println(standardDeviation);
		return 0;
	}

	private double getMean(List<List<ObservationVector>> data,
			double probabilities[]) {
		double mean = 0;

		for (int i = 0; i < data.size(); ++i) {
			ViterbiCalculator calc = new ViterbiCalculator(data.get(i), hmm);
			probabilities[i] = calc.lnProbability();
			// System.out.println(probabilities[i]);
			mean = mean + probabilities[i];
		}

		mean = mean / data.size();
		System.out.println("mean : " + mean);
		return mean;
	}

	private double getStandardDeviation(int size, double probabilities[],
			double mean) {
		double variance = 0;
		double standardDeviation;
		for (int i = 0; i < size; ++i) {
			variance = variance + (probabilities[i] - mean)
					* (probabilities[i] - mean);
		}
		variance = variance / size;

		standardDeviation = Math.sqrt(variance);
		// relaxedStandardDeviation = standardDeviation + (0.1) *
		// standardDeviation;
		return standardDeviation;
	}

	public int getResult(List<ObservationVector> sequence) throws IOException,
			FileFormatException {
		double probability;
		double mean;
		double standardDeviation;
		// double extendedStandardDeviation;
		String sCurrentLine;
		String valueString[] = new String[3];
		BufferedReader meanReader;
		int i = 0;

		probability = getProbability(sequence);

		meanReader = new BufferedReader(new FileReader(username + "\\meanSd-"
				+ username + ".txt"));

		while ((sCurrentLine = meanReader.readLine()) != null) {
			valueString[i] = sCurrentLine;
			i++;
		}

		meanReader.close();

		mean = Double.valueOf(valueString[0]);
		standardDeviation = Double.valueOf(valueString[1]);
		// extendedStandardDeviation = Double.valueOf(valueString[2]);
		// image = getImageRecognition ();
		// System.out.println(image);

		if (probability > (mean + (-1) * standardDeviation)
				&& probability < (mean - (-1) * standardDeviation)) {
			System.out.println("Authenticated");
			return 1;
		} else if (probability < (mean + (-1) * standardDeviation)
				&& probability > (mean - (-1) * standardDeviation)) {
			System.out.println("Not Authenticated");
			return -1;
		} else if (probability > (mean + (-1) * standardDeviation)
				&& probability < (mean - (-1) * standardDeviation)) {
			System.out.println("Authenticated");
			return 1;
		} else if (probability < (mean + (-1) * standardDeviation)
				&& probability > (mean - (-1) * standardDeviation)) {
			System.out.println("Try again");
			return 0;
		}
		return 0;
	}

	public boolean getImageRecognition() throws IOException {
		String sCurrentLine;
		ImageRecognition imageRecognition = new ImageRecognition(username);
		imageRecognition.Image();

		BufferedReader imageReader = new BufferedReader(new FileReader(username
				+ "\\Result.txt"));

		sCurrentLine = imageReader.readLine();
		imageReader.close();

		if (sCurrentLine.equalsIgnoreCase("matches")) {
			return true;
		} else if (sCurrentLine.equalsIgnoreCase("doesn't match")) {
			return false;
		}

		return false;
	}
}
