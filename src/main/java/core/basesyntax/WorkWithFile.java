package core.basesyntax;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class WorkWithFile {
    // Constants for operation types and file format
    private static final String SUPPLY_OPERATION = "supply";
    private static final String BUY_OPERATION = "buy";
    private static final String RESULT_OPERATION = "result";
    private static final String CSV_DELIMITER = ",";

    // Constants for array indices to improve readability
    private static final int SUPPLY_INDEX = 0;
    private static final int BUY_INDEX = 1;

    /**
     * Orchestrates the process of reading data from one file, generating a
     * statistic report, and writing it to another file.
     *
     * @param fromFileName The path to the input CSV file.
     * @param toFileName   The path to the output report file.
     */
    public void getStatistic(String fromFileName, String toFileName) {
        int[] totals = readAndCalculateTotals(fromFileName);
        String report = createReport(totals[SUPPLY_INDEX], totals[BUY_INDEX]);
        writeToFile(toFileName, report);
    }

    /**
     * Reads the source file, parses the data, and calculates the total amount
     * for "supply" and "buy" operations.
     *
     * @param fromFileName The path to the source data file.
     * @return An array of two integers: {supplyTotal, buyTotal}.
     */
    private int[] readAndCalculateTotals(String fromFileName) {
        int supplyTotal = 0;
        int buyTotal = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(fromFileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(CSV_DELIMITER);
                String operationType = parts[0];
                int amount = Integer.parseInt(parts[1]);

                if (SUPPLY_OPERATION.equals(operationType)) {
                    supplyTotal += amount;
                } else if (BUY_OPERATION.equals(operationType)) {
                    buyTotal += amount;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Can't read data from file: " + fromFileName, e);
        }
        return new int[]{supplyTotal, buyTotal};
    }

    /**
     * Creates a formatted report string from the calculated totals.
     *
     * @param supplyTotal The total amount for "supply" operations.
     * @param buyTotal    The total amount for "buy" operations.
     * @return A formatted multi-line string representing the report.
     */
    private String createReport(int supplyTotal, int buyTotal) {
        int result = supplyTotal - buyTotal;
        StringBuilder reportBuilder = new StringBuilder();

        reportBuilder.append(SUPPLY_OPERATION).append(CSV_DELIMITER).append(supplyTotal)
                .append(System.lineSeparator());
        reportBuilder.append(BUY_OPERATION).append(CSV_DELIMITER).append(buyTotal)
                .append(System.lineSeparator());
        reportBuilder.append(RESULT_OPERATION).append(CSV_DELIMITER).append(result)
                .append(System.lineSeparator());

        return reportBuilder.toString();
    }

    /**
     * Writes the given content to the specified file.
     *
     * @param toFileName    The path of the file to write to.
     * @param reportContent The string content to be written.
     */
    private void writeToFile(String toFileName, String reportContent) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(toFileName))) {
            writer.write(reportContent);
        } catch (IOException e) {
            throw new RuntimeException("Can't write data to file: " + toFileName, e);
        }
    }
}
