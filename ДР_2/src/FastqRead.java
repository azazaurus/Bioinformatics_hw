import java.util.*;
import java.util.stream.*;

public class FastqRead {
	public static class Element {
		public final char nucleotide;
		public final int qScore;

		public Element(char nucleotide, int qScore) {
			this.nucleotide = nucleotide;
			this.qScore = qScore;
		}
	}

	public final List<Element> elements;

	public FastqRead(List<Element> elements) {
		this.elements = elements;
	}

	public static FastqRead parse(List<String> fastqLines) {
		String nucleotidesLine = fastqLines.get(1);
		String qScoresLine = fastqLines.get(3);

		ArrayList<Element> elements = new ArrayList<>();
		for (int i = 0; i < nucleotidesLine.length(); i++) {
			int qScore = qScoresLine.charAt(i) - '!';
			elements.add(new Element(nucleotidesLine.charAt(i), qScore));
		}

		return new FastqRead(elements);
	}
}
