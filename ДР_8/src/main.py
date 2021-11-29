import sys

from pybedtools import BedTool
import regex


def main() -> None:
	gtf_file_path: str = get_gtf_file_path()
	genes: BedTool = BedTool(gtf_file_path)
	genes_list = list(genes)

	pattern: str = r'gene_type "\w*";'
	genes_type_list = []

	for i in range(len(genes_list)):
		result = regex.search(pattern, genes_list[i][8])
		genes_type_list.append(genes_list[i][8][result.start() + 11:result.end() - 2])

	gene_type_set = set(genes_type_list)
	print(len(gene_type_set))

	pattern_coding = r'gene_type "protein_coding";'
	genes_type_protein_cds_list = []
	genes_type_protein_exon_list = []

	for i in range(len(genes_list)):
		if regex.search(pattern_coding, genes_list[i][8]) and genes_list[i][2] == 'CDS':
			genes_type_protein_cds_list.append(genes_list[i])
		# print(genes_list[i])
		elif regex.search(pattern_coding, genes_list[i][8]) and genes_list[i][2] == 'exon':
			genes_type_protein_exon_list.append(genes_list[i])

	with open('unmerged_exons.gtf', 'w') as exons_f:
		for i in range(len(genes_type_protein_exon_list)):
			exons_f.write(str(genes_type_protein_exon_list[i]))


def get_gtf_file_path() -> str:
	return sys.argv[1] if len(sys.argv) == 2 else input("Enter path to gtf file:")


if __name__ == '__main__':
	main()
