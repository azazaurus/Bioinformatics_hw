import org.apache.commons.collections4.*;
import org.hamcrest.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;
import ru.itis.*;

import java.util.*;
import java.util.stream.*;

import static org.apache.commons.collections4.CollectionUtils.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

public class MainTest {
	@Nested
	public class OnGetOrfs {
		@ParameterizedTest
		@ArgumentsSource(OnGetOrfs_ReturnsCorrectOrfs_TestCaseSource.class)
		public void returnsCorrectOrfs(String dna, List<Orf> expectedOrfs) {
			List<Orf> actualOrfs = Main.getOrfs(dna, 1, Orf.Chain.FORWARD);

			assertTrue(isEqualCollection(actualOrfs, expectedOrfs, new OrfEquator()));
			// assertThat(actualOrfs, containsInAnyOrder(expectedOrfs.toArray(new Orf[0])));
			// assertArrayEquals(expectedOrfs.toArray(), actualOrfs.toArray());
		}
	}

	@Nested
	public class OnGetReversedSequence {
		@ParameterizedTest
		@CsvSource({
			"ATGC,GCAT",
			"AATTGGCC,GGCCAATT"
		})
		public void returnsCorrectSequence(String originalSequence, String expectedSequence) {
			List<Character> originalSequenceChars = originalSequence.chars()
				.mapToObj(x -> (char)x)
				.collect(Collectors.toList());

			List<Character> actualSequenceChars = Main.getReversedSequence(originalSequenceChars);
			String actualSequence = new String(toArray(actualSequenceChars));

			assertThat(actualSequence, is(expectedSequence));
		}
	}

	public static Orf createOrf(String orfSequence, int startIndex, Orf.Chain chain, int frame) {
		Orf orf = new Orf();
		orf.startOrf(new Triplet(orfSequence.substring(0, Triplet.LENGTH)), startIndex, chain, frame);
		for (int i = Triplet.LENGTH;
		     i <= orfSequence.length() - Triplet.LENGTH * 2;
			 i += Triplet.LENGTH) {
			orf.addTriplet(new Triplet(orfSequence.substring(i, i + Triplet.LENGTH)));
		}
		orf.stopOrf(
			new Triplet(orfSequence.substring(orfSequence.length() - Triplet.LENGTH)),
			startIndex + orfSequence.length());

		return orf;
	}

	public static class OnGetOrfs_ReturnsCorrectOrfs_TestCaseSource implements ArgumentsProvider {
		@Override
		public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
			return Stream.of(
				Arguments.of("ATGGCGGCGGCGGGCGCGGCGGCGGCGGCG", Collections.EMPTY_LIST),
				Arguments.of("GCGGCGGCGGCGGGCGCGGCGGCGGCGTAA", Collections.EMPTY_LIST),
				Arguments.of("GCGGCGGCTAGGGGCGCGGCATGGGCGGCG", Collections.EMPTY_LIST),
				Arguments.of("GCGGCGGCGATGGCGGCGGCGGCGGCGTAA", Arrays.asList(
					createOrf("ATGGCGGCGGCGGCGGCGTAA", 9, Orf.Chain.FORWARD, 1))),
				Arguments.of("GCGGCGGCGATGGCGGCGATGGCGGCGTAA", Arrays.asList(
					createOrf("ATGGCGGCGATGGCGGCGTAA", 9, Orf.Chain.FORWARD, 1))),
				Arguments.of("GCGGCGGCGATGGTAGCGATGGCGGCGTAA", Arrays.asList(
					createOrf("ATGGTAGCGATGGCGGCGTAA", 9, Orf.Chain.FORWARD, 1))),
				Arguments.of("GATGCGGCGATGGTAGCGATGGCGGCGTAA", Arrays.asList(
					createOrf("ATGCGGCGATGGTAG", 2, Orf.Chain.FORWARD, 2),
					createOrf("ATGGTAGCGATGGCGGCGTAA", 9, Orf.Chain.FORWARD, 1))),
				Arguments.of("GATGCGGCGATGGTAGCGATGGCGGCGTAA", Arrays.asList(
					createOrf("ATGCGGCGATGGTAG", 2, Orf.Chain.FORWARD, 2),
					createOrf("ATGGTAGCGATGGCGGCGTAA", 9, Orf.Chain.FORWARD, 1))),
				Arguments.of("GATGTGACGATGGATGCGATGGTAGCGTAA", Arrays.asList(
					createOrf("ATGTGA", 2, Orf.Chain.FORWARD, 2),
					createOrf("ATGCGATGGTAG", 14, Orf.Chain.FORWARD, 1),
					createOrf("ATGGATGCGATGGTAGCGTAA", 9, Orf.Chain.FORWARD, 1)))
				);
		}
	}

	private static <T> Matcher<T>[] samePropertyValuesAsIn(T... values) {
		//noinspection unchecked
		return Arrays.stream(values)
			.map(Matchers::samePropertyValuesAs)
			.toArray(Matcher[]::new);
	}

	private static char[] toArray(final List<Character> list){
		final char[] array = new char[list.size()];
		for (int i = 0; i < array.length; i++)
			array[i] = list.get(i);
		return array;
	}
}
