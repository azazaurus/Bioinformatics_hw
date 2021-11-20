from pytest import mark, param

from alignment_without_affine_gap_penalty import AlignmentResult, local_align
from tests.common import *


def returns_expected_alignment_cases():
	return [
		param(
			"ATGAGTCTCTCTGATAAGGACAAGGCTGCTGTGAAAGCCCTATGG",
			"CTGTCTCCTGCCGACAAGACCAACGTCAAGGCCGCCTGGGGTAAG",
			5,
			-4,
			-1,
			121),
		param(
			"AGGAAGGAAATTTTTTTTTACTGTGTGGATTAAAAAAAGAGTGTCTGATAGCAGCTTCTGAACTCCAGACAAGTTACACCCATTTAAAATTA",
			"AAGCTCTTTCATTCTGACTGCAACGGGCAATATTCCCTGTGTGGATTAAAAAAAGAGTGTCTGATGAGCAGCTTCTGAACTGGTTACCTGCCGAATGAGTAAATTAATATTTTATTGACTTAGG",
			5,
			-4,
			-1,
			332),
		param(
			"GAGTCCACCCGCCGTATTGCGGCAAGCCGCATTCCGGCTGATCACATGGTGCTGATGGCAGGTTTCACCGTAATGAAAAAGGCGAACTGGTGGTGCTTGGACGCAGACGGTTCCGACGTGCTGGCTGCCTGTGTACGCGCCGATTGTGCGAGATTTGGACGGAC",
			"TCCACGCCTCTTAGGGAGTGTGCACACGGCAGTTCTGCCCTGCAGCCTGGCCGCTGCCCCCGCCCTCGACATGGTGCTGATGGCAGGTTTCACCGCCGGTAATGAAAAAGGCGAACTGGTGGTGCTTGGACGCATACGGTTCCGACGTGCTGGCTGCCCCAGGGATGGGACACGGTGGCCCTGG",
			5,
			-4,
			-1,
			602),
		param(
			"TGCTCGTAC",
			"TTCATACGTA",
			2,
			-1,
			-1,
			11)]


@mark.parametrize(
	"sequence_1,sequence_2,match_score,mismatch_score,gap_score,expected_total_score",
	returns_expected_alignment_cases())
def test_returns_correct_alignment(
		sequence_1: str,
		sequence_2: str,
		match_score: int,
		mismatch_score: int,
		gap_score: int,
		expected_total_score: int):
	actual_alignment: AlignmentResult = local_align(
		sequence_1,
		sequence_2,
		match_score,
		mismatch_score,
		gap_score)

	alignment_stats: AlignmentStatistics = get_alignment_statistics(
		actual_alignment.aligned_sequence_1,
		actual_alignment.aligned_sequence_2)
	assert actual_alignment.matches_count == alignment_stats.calculated_matches_count
	assert actual_alignment.mismatches_count == alignment_stats.calculated_mismatches_count
	assert actual_alignment.gaps_count == alignment_stats.calculated_gap_extensions_count


@mark.parametrize(
	"sequence_1,sequence_2,match_score,mismatch_score,gap_score,expected_total_score",
	returns_expected_alignment_cases())
def test_returns_alignment_with_expected_total_score(
		sequence_1: str,
		sequence_2: str,
		match_score: int,
		mismatch_score: int,
		gap_score: int,
		expected_total_score: int):
	actual_alignment: AlignmentResult = local_align(
		sequence_1,
		sequence_2,
		match_score,
		mismatch_score,
		gap_score)
	actual_total_score: int = actual_alignment.matches_count * match_score \
		+ actual_alignment.mismatches_count * mismatch_score \
		+ actual_alignment.gaps_count * gap_score
	assert actual_total_score == expected_total_score
