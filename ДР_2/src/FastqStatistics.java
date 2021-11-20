import java.math.*;
import java.text.*;
import java.util.*;

public class FastqStatistics {
	public final Collection<FastqRead> reads;

	public final long nucleotidsTotal;
	public final double averageReadLength;

	public final int qScoreThreshold;
	public final float elementsWithQScoreAboveThresholdPercent;

	public FastqStatistics(
			Collection<FastqRead> reads,
			long nucleotidsTotal,
			double averageReadLength,
			int qScoreThreshold,
			float elementsWithQScoreAboveThresholdPercent) {
		this.reads = reads;
		this.nucleotidsTotal = nucleotidsTotal;
		this.averageReadLength = averageReadLength;
		this.qScoreThreshold = qScoreThreshold;
		this.elementsWithQScoreAboveThresholdPercent = elementsWithQScoreAboveThresholdPercent;
	}

	public static FastqStatistics getStatistics(Collection<FastqRead> reads, int qScoreThreshold) {
		int nucleotidCounter = 0;
		int elementsWithQScoreAboveThresholdCounter = 0;
		for (FastqRead read : reads) {
			nucleotidCounter += read.elements.size();
			for (FastqRead.Element element : read.elements) {
				if (element.qScore >= qScoreThreshold) {
					elementsWithQScoreAboveThresholdCounter++;
				}
			}
		}

		float roundedReadLength = new BigDecimal(nucleotidCounter).divide
			(new BigDecimal(reads.size()), 2,  RoundingMode.HALF_EVEN).floatValue();

		float elementsWithQScoreAboveThresholdPercent = new BigDecimal (elementsWithQScoreAboveThresholdCounter)
			.multiply(new BigDecimal(100))
			.divide( new BigDecimal(nucleotidCounter), 2, RoundingMode.HALF_EVEN)
			.floatValue();

		return new FastqStatistics(
			reads,
			nucleotidCounter,
			roundedReadLength,
			qScoreThreshold,
			elementsWithQScoreAboveThresholdPercent);
	}
}
