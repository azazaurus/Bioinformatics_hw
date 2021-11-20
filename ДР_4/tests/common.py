class AlignmentStatistics:
	def __init__(
			self,
			calculated_matches_count: int,
			calculated_mismatches_count: int,
			calculated_gap_openings_count: int,
			calculated_gap_extensions_count: int):
		self.calculated_matches_count = calculated_matches_count
		self.calculated_mismatches_count = calculated_mismatches_count
		self.calculated_gap_openings_count = calculated_gap_openings_count
		self.calculated_gap_extensions_count = calculated_gap_extensions_count

def get_alignment_statistics(aligned_sequence_1: str, aligned_sequence_2: str) -> AlignmentStatistics:
	calculated_matches_count: int = 0
	calculated_mismatches_count: int = 0
	calculated_gap_openings_count: int = 0
	calculated_gap_extensions_count: int = 0

	for current_index in range(0, len(aligned_sequence_1)):
		if aligned_sequence_1[current_index] == "-" or aligned_sequence_2[current_index] == "-":
			if aligned_sequence_1[current_index] == "-":
				calculated_gap_extensions_count += 1
				if current_index > 0 and aligned_sequence_1[current_index - 1] != "-":
					calculated_gap_openings_count += 1

			if aligned_sequence_2[current_index] == "-":
				calculated_gap_extensions_count += 1
				if current_index > 0 and aligned_sequence_2[current_index - 1] != "-":
					calculated_gap_openings_count += 1

		elif aligned_sequence_1[current_index] == aligned_sequence_2[current_index]:
			calculated_matches_count += 1
		else:
			calculated_mismatches_count += 1

	return AlignmentStatistics(
		calculated_matches_count,
		calculated_mismatches_count,
		calculated_gap_openings_count,
		calculated_gap_extensions_count)
