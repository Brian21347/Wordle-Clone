import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileWriter;
import java.util.Scanner;

public class Sort {
    public final static int NUM_WORDS = 9977;

    public static void main(String[] args) throws FileNotFoundException {
        String[] words = new String[NUM_WORDS];
        File file = new File("words.txt");
        Scanner scanner = new Scanner(file);
        int i = 0;
        while (scanner.hasNextLine()) {
            words[i] = scanner.nextLine();
            i++;
        }
        scanner.close();
        quickSort(words, 0, words.length - 1);

        String outStr = "";
        for (int j = 0; j < words.length; j++) {
            outStr += words[j] + "\n";
        }
        writeString(outStr);
    }

    public static void writeString(String str) {
        try {
            FileWriter writer = new FileWriter("sorted.txt");
            writer.write(str);
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occured");
            e.printStackTrace();
        }
    }

    public static void quickSort(String[] arr, int begin, int end) {
        if (begin < end) {
            int partitionIndex = partition(arr, begin, end);

            quickSort(arr, begin, partitionIndex - 1);
            quickSort(arr, partitionIndex + 1, end);
        }
    }

    private static int partition(String[] arr, int begin, int end) {
        String pivot = arr[end];
        int i = (begin - 1);

        for (int j = begin; j < end; j++) {
            if (arr[j].compareTo(pivot) < 0) { //
                i++;

                String swapTemp = arr[i];
                arr[i] = arr[j];
                arr[j] = swapTemp;
            }
        }

        String swapTemp = arr[i + 1];
        arr[i + 1] = arr[end];
        arr[end] = swapTemp;

        return i + 1;
    }
}