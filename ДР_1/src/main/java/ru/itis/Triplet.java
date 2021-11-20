package ru.itis;

import java.util.*;

import static java.util.Map.entry;

public class Triplet {
	public final String triplet;

	public static final int LENGTH = 3;

	public Triplet(char nucleotide1, char nucleotide2, char nucleotide3) {
		triplet = new String(new char[] { nucleotide1, nucleotide2, nucleotide3 });
	}

	public Triplet(String triplet) {
		this.triplet = triplet;
	}

	public boolean isStartTriplet() {
		return startTriplets.contains(triplet);
	}

	public boolean isStopTriplet() {
		return stopTriplets.contains(triplet);
	}

	public String getTranslatedProtein() {
		return tripletToProteinMap.get(triplet);
	}

	private static final Set<String> startTriplets = setOf("ATG");
	private static final Set<String> stopTriplets = setOf("TAA", "TAG", "TGA");

	private static final Map<String, String> tripletToProteinMap = Map.ofEntries(
		entry("TAA", ""),
		entry("TAG", ""),
		entry("TGA", ""),
		entry("GCT", "Ala"),
		entry("ATG", "Met"),
		entry("GCC", "Ala"),
		entry("GCA", "Ala"),
		entry("GCG", "Ala"),
		entry("ATT", "Ile"),
		entry("ATC", "Ile"),
		entry("ATA", "Ile"),
		entry("CGT", "Arg"),
		entry("CGC", "Arg"),
		entry("CGA", "Arg"),
		entry("CGG", "Arg"),
		entry("AGA", "Arg"),
		entry("AGG", "Arg"),
		entry("CTT", "Leu"),
		entry("CTC", "Leu"),
		entry("CTA", "Leu"),
		entry("CTG", "Leu"),
		entry("TTA", "Leu"),
		entry("TTG", "Leu"),
		entry("AAT", "Asn"),
		entry("AAC", "Asn"),
		entry("AAA", "Lys"),
		entry("AAG", "Lys"),
		entry("GAT", "Asp"),
		entry("GAC", "Asp"),
		entry("TTT", "Phe"),
		entry("TTC", "Phe"),
		entry("TGT", "Cys"),
		entry("TGC", "Cys"),
		entry("CCT", "Pro"),
		entry("CCC", "Pro"),
		entry("CCA", "Pro"),
		entry("CCG", "Pro"),
		entry("CAA", "Gln"),
		entry("CAG", "Gln"),
		entry("TCT", "Ser"),
		entry("TCC", "Ser"),
		entry("TCA", "Ser"),
		entry("TCG", "Ser"),
		entry("AGT", "Ser"),
		entry("AGC", "Ser"),
		entry("GAA", "Glu"),
		entry("GAG", "Glu"),
		entry("ACT", "Thr"),
		entry("ACC", "Thr"),
		entry("ACA", "Thr"),
		entry("ACG", "Thr"),
		entry("TGG", "Trp"),
		entry("GGT", "Gly"),
		entry("GGC", "Gly"),
		entry("GGA", "Gly"),
		entry("GGG", "Gly"),
		entry("TAT", "Tyr"),
		entry("TAC", "Tyr"),
		entry("CAT", "His"),
		entry("CAC", "His"),
		entry("GTT", "Val"),
		entry("GTC", "Val"),
		entry("GTA", "Val"),
		entry("GTG", "Val"));

	private static <T> Set<T> setOf(T... values)
	{
		return new HashSet<>(Arrays.asList(values));
	}
}