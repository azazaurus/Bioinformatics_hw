import java.util.*;

public class FastaReadSequence {
	public static class Element {
		public final char nucleotide;

		public Element(char nucleotide) {
			this.nucleotide = nucleotide;
		}
	}

	public final List<Element> elements;

	public FastaReadSequence(List<Element> elements) {
		this.elements = elements;
	}

	public static FastaReadSequence parse(List<String> fastaSequenceLines) {
		ArrayList<Element> elements = new ArrayList<>();
		for (String nucleotidesLine : fastaSequenceLines) {
			for (int i = 0; i < nucleotidesLine.length(); i++) {
				elements.add(new Element(nucleotidesLine.charAt(i)));
			}
		}

		return new FastaReadSequence(elements);
	}
}

