import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Укажите полный путь до файла");
		String path = sc.nextLine();
	    System.out.println("Введите пороговое число для отбора по Q-score: ");
	    int qScoreThreshold = Integer.parseInt(sc.nextLine());
	    File file = new File(path);
		int counter = 0;
		Collection<FastqRead> reads = new HashSet<>();
	    try {
		    //создаем объект FileReader для объекта File
		    FileReader fr = new FileReader(file);
		    //создаем BufferedReader с существующего FileReader для построчного считывания
		    BufferedReader reader = new BufferedReader(fr);
		    // считаем сначала первую строку
		    String line = reader.readLine();
			counter++;
			ArrayList<String> readLines = new ArrayList<>();
			readLines.add(line);
		    while (line != null) {
			    // считываем остальные строки в цикле
			    line = reader.readLine();
				readLines.add(line);
				counter++;
			    if (counter % 4 == 0) {
					reads.add(FastqRead.parse(readLines));
					readLines = new ArrayList<>();
			    }
		    }
	    } catch (IOException e) {
		    e.printStackTrace();
	    }

		FastqStatistics statistics = FastqStatistics.getStatistics(reads, qScoreThreshold);

		System.out.println();
	    System.out.println("1. Количество ридов: " + reads.size());
	    System.out.println("2. Общее количество букв (включая N, если они есть): " + statistics.nucleotidsTotal);
		System.out.println("3. Среднюю длину рида: " + statistics.averageReadLength);
	    System.out.println("4. Процент букв, качество прочтения которых по шкале Q-score " +
		    "больше или равно "  + qScoreThreshold + ": " + statistics.elementsWithQScoreAboveThresholdPercent);
    }
}
