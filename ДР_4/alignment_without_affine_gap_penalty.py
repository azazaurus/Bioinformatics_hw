import os
from typing import *


def main() -> None:
	while True:
		try:
			seq1 = input("Enter sequence 1: ")
			seq2 = input("Enter sequence 2: ")

			type_select: str = str(input("Enter global or local alignment (G/L): "))
			# type_affine_select = str(input(
			# 	"Do u want {0} alignment with affine? \nEnter yes/no: ".format(type_select)))

			match_point = int(input("Enter match point: "))
			if match_point < 0:
				match_point = -match_point

			mismatch_point = int(input("Enter mismatch point: "))
			if mismatch_point > 0:
				mismatch_point = -mismatch_point

			gap_point = int(input("Enter gap point: "))
			if gap_point > 0:
				gap_point = -gap_point

			result = {
				"G": global_align(seq1, seq2, match_point, mismatch_point, gap_point),
				"L": local_align(seq1, seq2, match_point, mismatch_point, gap_point)
			}[type_select]
			print(os.linesep + 'Your result:')
			print(result.aligned_sequence_1)
			print(result.aligned_sequence_2)
			print('Count of matches =', result.matches_count)
			print('Count of gaps =', result.gaps_count)
			print('Count of mismatches =', result.mismatches_count)

			total_score = match_point * result.matches_count\
				+ mismatch_point * result.mismatches_count\
				+ gap_point * result.gaps_count
			print('Total score =', total_score)

		except ValueError:
			print("Incorrect data")
			continue

		if not isinstance(type_select, str):
			print("Enter global or local please")
			continue
		else:
			break


class AlignmentResult:
	def __init__(
			self,
			aligned_sequence_1: str,
			aligned_sequence_2: str,
			matches_count: int,
			mismatches_count: int,
			gaps_count: int):
		self.aligned_sequence_1: str = aligned_sequence_1
		self.aligned_sequence_2: str = aligned_sequence_2
		self.matches_count: int = matches_count
		self.mismatches_count: int = mismatches_count
		self.gaps_count: int = gaps_count


def global_align(x, y, match, mismatch, gap):
	A = []
	for i in range(len(y) + 1):
		A.append([0] * (len(x) + 1))
	for i in range(len(y) + 1):
		A[i][0] = gap * i
	for i in range(len(x) + 1):
		A[0][i] = gap * i

	for i in range(1, len(y) + 1):
		for j in range(1, len(x) + 1):
			A[i][j] = max(
				A[i][j - 1] + gap, A[i - 1][j] + gap,
				A[i - 1][j - 1] + (match if y[i - 1] == x[j - 1] else mismatch))

	align_X = ""
	align_Y = ""
	i = len(x)
	j = len(y)

	while i > 0 or j > 0:

		current_score = A[j][i]

		if i > 0 and j > 0 and x[i - 1] == y[j - 1]:
			align_X = x[i - 1] + align_X
			align_Y = y[j - 1] + align_Y
			i -= 1
			j -= 1

		elif i > 0 and (current_score == A[j][i - 1] + mismatch or current_score == A[j][i - 1] + gap):
			align_X = x[i - 1] + align_X
			align_Y = "-" + align_Y
			i -= 1

		else:
			align_X = "-" + align_X
			align_Y = y[j - 1] + align_Y
			j -= 1

	match_score = 0
	mismatch_score = 0
	gap_score = 0

	align_X_list = list(align_X)
	align_Y_list = list(align_Y)

	for i in range(len(align_X_list)):
		if align_X_list[i] == align_Y_list[i]:
			match_score = match_score + 1
		elif align_X_list[i] == '-' or align_Y_list[i] == '-':
			gap_score = gap_score + 1
		else:
			mismatch_score = mismatch_score + 1

	return AlignmentResult(align_X, align_Y, match_score, mismatch_score, gap_score)


def local_align(
		sequence_1: str,
		sequence_2: str,
		match_score: int,
		mismatch_score: int,
		gap_score: int) \
		-> AlignmentResult:
	rows_count: int = len(sequence_1)
	columns_count: int = len(sequence_2)
	alignment_scoring_matrix: List[List[int]] = [[0
		for _ in range(0, columns_count + 1)]
		for _ in range(0, rows_count + 1)]

	max_substitution_score: int = 0
	max_substitution_score_row_index: int = 0
	max_substitution_score_column_index: int = 0

	# Fill substitution matrix and remember location of max score
	# See https://en.wikipedia.org/wiki/Smith%E2%80%93Waterman_algorithm
	for row_index in range(1, rows_count + 1):
		for column_index in range(1, columns_count + 1):
			do_nucleotides_match: bool = sequence_1[row_index - 1] == sequence_2[column_index - 1]
			current_substitution_score: int = max(
				alignment_scoring_matrix[row_index - 1][column_index - 1]
					+ (match_score if do_nucleotides_match else mismatch_score),
				alignment_scoring_matrix[row_index - 1][column_index] + gap_score,
				alignment_scoring_matrix[row_index][column_index - 1] + gap_score,
				0)
			alignment_scoring_matrix[row_index][column_index] = current_substitution_score

			if current_substitution_score > max_substitution_score:
				max_substitution_score = current_substitution_score
				max_substitution_score_row_index = row_index
				max_substitution_score_column_index = column_index

	aligned_reversed_sequence_1: List[str] = []
	aligned_reversed_sequence_2: List[str] = []
	matches_count: int = 0
	mismatches_count: int = 0
	gaps_count: int = 0

	# Traceback substitution matrix to get aligned sequences
	# These sequences are reversed because we move from the end to the beginning
	row_index: int = max_substitution_score_row_index
	column_index: int = max_substitution_score_column_index
	current_score: int = max_substitution_score
	while current_score > 0:
		do_nucleotides_match: bool = sequence_1[row_index - 1] == sequence_2[column_index - 1]
		if do_nucleotides_match \
				and current_score == alignment_scoring_matrix[row_index - 1][column_index - 1] + match_score:
			aligned_reversed_sequence_1.append(sequence_1[row_index - 1])
			aligned_reversed_sequence_2.append(sequence_2[column_index - 1])

			row_index -= 1
			column_index -= 1
			matches_count += 1

		elif current_score == alignment_scoring_matrix[row_index - 1][column_index - 1] + mismatch_score:
			aligned_reversed_sequence_1.append(sequence_1[row_index - 1])
			aligned_reversed_sequence_2.append(sequence_2[column_index - 1])

			row_index -= 1
			column_index -= 1
			mismatches_count += 1

		elif current_score == alignment_scoring_matrix[row_index - 1][column_index] + gap_score:
			aligned_reversed_sequence_1.append(sequence_1[row_index - 1])
			aligned_reversed_sequence_2.append("-")

			row_index -= 1
			gaps_count += 1

		elif current_score == alignment_scoring_matrix[row_index][column_index - 1] + gap_score:
			aligned_reversed_sequence_1.append("-")
			aligned_reversed_sequence_2.append(sequence_2[column_index - 1])

			column_index -= 1
			gaps_count += 1

		else:
			raise RuntimeError("Couldn't traceback the substituton matrix, "
				+ f"stopped on row {row_index} and column {column_index}" + os.linesep
				+ to_str(alignment_scoring_matrix))

		current_score = alignment_scoring_matrix[row_index][column_index]

	aligned_reversed_sequence_1.reverse()
	aligned_reversed_sequence_2.reverse()
	return AlignmentResult(
		"".join(aligned_reversed_sequence_1),
		"".join(aligned_reversed_sequence_2),
		matches_count,
		mismatches_count,
		gaps_count)


T = TypeVar("T")
def to_str(matrix: List[List[T]]) -> str:
	return os.linesep.join(
		" ".join(str(x) for x in row)
		for row in matrix)


if __name__ == '__main__':
	main()
