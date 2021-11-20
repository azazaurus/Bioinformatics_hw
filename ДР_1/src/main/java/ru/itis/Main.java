package ru.itis;

import java.util.*;
import java.util.stream.*;

import static java.util.Map.*;

public class Main {
	public static final int MIN_TRIPLETS_PER_ORF = 4;

	private static final Map<Character, Character> reversedNucleotideMap = Map.ofEntries(
		entry('A', 'T'),
		entry('T', 'A'),
		entry('C', 'G'),
		entry('G', 'C'));

	public static void main(String[] args) {
		int CGcontent;
		int dnaLength;

		Scanner sc = new Scanner(System.in);
		System.out.print("Enter CG-content (%): ");
		CGcontent = sc.nextInt();
		while (CGcontent < 20 || CGcontent > 80) {
			System.out.println("CG-content must be in the range from 20 to 80");
			System.out.print("Enter CG-content (%): ");
			CGcontent = sc.nextInt();
		}

		System.out.println();
		System.out.print("Enter DNA length: ");
		dnaLength = sc.nextInt();
		while (dnaLength < 100 || dnaLength > 1000) {
			System.out.println("DNA length must be in the range from 100 to 1000");
			System.out.print("Enter DNA length: ");
			dnaLength = sc.nextInt();
		}

		List<Character> sequence = generateDNA(CGcontent, dnaLength);
		List<Character> reversedSequence = getReversedSequence(sequence);

		Stream<Orf> orfs = Stream.concat(
			getOrfs(new String(toArray(sequence)), MIN_TRIPLETS_PER_ORF, Orf.Chain.FORWARD).stream(),
			getOrfs(new String(toArray(reversedSequence)), MIN_TRIPLETS_PER_ORF, Orf.Chain.REVERSE).stream());
		Optional<Orf> longestOrf = orfs.max(Comparator.comparing(Orf::getLength));

		for (char nuc : sequence) {
			System.out.print(nuc);
		}
		System.out.println();
		for (char nuc : reversedSequence) {
			System.out.print(nuc);
		}

		output(longestOrf);
	}

	public static ArrayList<Character> generateDNA(int CGcontent, int dnaLength) {
		ArrayList<Character> sequence = new ArrayList<>();
		int c = (int) Math.round(dnaLength * (CGcontent * 0.01) / 2);
		for (int i = 0; i < c; i++) {
			sequence.add('C');
			sequence.add('G');
		}
		int a = (int) Math.round(dnaLength * ((100 - CGcontent) * 0.01) / 2);
		for (int i = 0; i < a; i++) {
			sequence.add('A');
		}
		while (sequence.size() < dnaLength)
			sequence.add('T');

		Collections.shuffle(sequence);
		return sequence;
	}

	public static List<Character> getReversedSequence(List<Character> sequence) {
		List<Character> reversedSequence = sequence.stream()
			.map(reversedNucleotideMap::get)
			.collect(Collectors.toList());
		Collections.reverse(reversedSequence);
		return reversedSequence;
	}

	public static ArrayList<Orf> getOrfs(String sequence, int MIN_TRIPLETS_PER_ORF, Orf.Chain chain) {
		int length = sequence.length();
		Triplet temp;
		ArrayList<Orf> orfs = new ArrayList<>();
		Orf orf1 = new Orf();
		Orf orf2 = new Orf();
		Orf orf3 = new Orf();
		for (int i = 0; i <= length - 1; i += 3) {
			if (i <= length - 3) {
				temp = new Triplet(sequence.charAt(i), sequence.charAt(i+1), sequence.charAt(i+2));
				orf1 = tryAddTriplet(orf1, temp, orfs, i, 1, MIN_TRIPLETS_PER_ORF, chain);
			}

			if (i <= length - 4) {
				temp = new Triplet(sequence.charAt(i+1), sequence.charAt(i+2), sequence.charAt(i+3));
				orf2 = tryAddTriplet(orf2, temp, orfs, i+1, 2, MIN_TRIPLETS_PER_ORF, chain);
			}
			else
				continue;

			if (i <= length - 5) {
				temp = new Triplet(sequence.charAt(i+2), sequence.charAt(i+3), sequence.charAt(i+4));
				orf3 = tryAddTriplet(orf3, temp, orfs, i+2, 3, MIN_TRIPLETS_PER_ORF, chain);
			}
		}
		return orfs;
	}

	private static Orf tryAddTriplet(Orf orf, Triplet triplet, ArrayList<Orf> orfs, int i, int frame,
	                                 int MIN_TRIPLETS_PER_ORF, Orf.Chain chain) {
		if (triplet.isStartTriplet())
			if (orf.isStarted)
				orf.addTriplet(triplet);
			else
				orf.startOrf(triplet, i, chain, frame);

		else if (triplet.isStopTriplet()) {
			if (orf.isStarted) {
				orf.stopOrf(triplet, i + 2);

				if (orf.length >= MIN_TRIPLETS_PER_ORF) {
					orfs.add(orf);
					orf = new Orf();
				}
			}
		}
		else if (orf.isStarted)
			orf.addTriplet(triplet);
		return orf;
	}

	private static char[] toArray(final List<Character> list){
		final char[] array = new char[list.size()];
		for(int i = 0; i < array.length; i++)
			array[i] = list.get(i);
		return array;
	}
	private static void output(Optional<Orf> orf) {
		if (orf.isEmpty()) {
			System.out.println("ORF not found");
		}
		else {
			System.out.println();
			System.out.println("1) ORF with the largest length: " + orf.get().getOrf());

			System.out.println("2) Translated protein: " + orf.get().getTranslatedProtein());

			System.out.println("3) ORF is in the range: from " + orf.get().startIndex + " to " +
				(orf.get().startIndex + (orf.get().getLength()+1)*3));

			System.out.println("4) Chain: " + orf.get().chain);

			System.out.println("5) Frame: " + orf.get().frame);
		}
	}
}
