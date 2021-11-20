import java.io.*;
import java.util.*;

public class Main {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Укажите полный путь до файла");
		String path = sc.nextLine();
		List<FastaReadSequence> reads = new ArrayList<>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(path));
			// считаем сначала первую строку
			String line = reader.readLine();
			ArrayList<String> readSequenceLines = new ArrayList<>();
			while (line != null) {
				// считываем остальные строки в цикле

				if (!line.contains(">")) {
					readSequenceLines.add(line);
				}
				else if (!readSequenceLines.isEmpty()) {
					reads.add(FastaReadSequence.parse(readSequenceLines));
					readSequenceLines.clear();
				}
				line = reader.readLine();
			}

			if (!readSequenceLines.isEmpty()) {
				reads.add(FastaReadSequence.parse(readSequenceLines));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		FastaStatistics statistics = FastaStatistics.getStatistics(reads);

		System.out.println();
		System.out.println("1. Количество последовательностей: " + reads.size());
		System.out.println("2. Минимальная, максимальная и средняя длина последовательности: "
			+ statistics.minSequenceLength + ", " + statistics.maxSequenceLength + ", "
			+ statistics.averageReadLength);
		System.out.println("3. N50: " + statistics.n50);
	}
}

