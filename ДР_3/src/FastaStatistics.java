import java.math.*;
import java.util.*;

public class FastaStatistics {
	public final Collection<FastaReadSequence> reads;

	public final int minSequenceLength;
	public final int maxSequenceLength;
	public final double averageReadLength;
	public final int n50;

	public FastaStatistics(
		List<FastaReadSequence> reads,
		double averageReadLength,
		int minSequenceLength,
		int maxSequenceLength,
		int n50
		) {
		this.reads = reads;
		this.averageReadLength = averageReadLength;
		this.minSequenceLength = minSequenceLength;
		this.maxSequenceLength = maxSequenceLength;
		this.n50 = n50;
	}

	public static FastaStatistics getStatistics(List<FastaReadSequence> reads) {
		reads.sort(Comparator.<FastaReadSequence>comparingInt(x -> x.elements.size()).reversed());
		long nucleotideTotalSum = reads.stream().mapToLong(x -> x.elements.size()).sum();
		int minSequenceLength = reads.get(reads.size() - 1).elements.size();
		int maxSequenceLength = reads.get(0).elements.size();
		int readSequencesArrayLength = reads.size();
		int n50 = 0;

		long half = new BigDecimal(nucleotideTotalSum)
			.divide(new BigDecimal(2), 1, RoundingMode.HALF_EVEN)
			.longValue();
		long counter = 0;
		for (int i = 0; i < readSequencesArrayLength; i++) {
			counter += reads.get(i).elements.size();
			if (counter >= half) {
				n50 = reads.get(i).elements.size();
				break;
			}
		}

		double roundedReadLength = new BigDecimal(nucleotideTotalSum).divide
			(new BigDecimal(reads.size()), 2,  RoundingMode.HALF_EVEN).doubleValue();

		return new FastaStatistics(
			reads,
			roundedReadLength,
			minSequenceLength,
			maxSequenceLength,
			n50);
	}
}
