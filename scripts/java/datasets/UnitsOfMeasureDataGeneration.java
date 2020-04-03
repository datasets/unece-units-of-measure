package datasets;

import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.apache.commons.collections4.EnumerationUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.opc.internal.FileHelper;
import org.apache.poi.ss.format.CellTextFormatter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbookFactory;

public class UnitsOfMeasureDataGeneration {

    private static final CellTextFormatter STRING_CELL_TEXT_FORMATTER = new CellTextFormatter("@");

    private static final Path DEFAULT_OUTPUT_FILE = Paths.get("data", "units-of-measure.csv");

    public static void main(String[] args) {
        Path outputFile = Optional.ofNullable(args)
            .filter(array -> array.length > 0)
            .map(array -> array[0])
            .map(File::new)
            .filter(File::exists)
            .map(File::toPath)
            .filter(path -> {
                System.out.println("Using outputFile from program argument: " + path);
                return true;
            })
            .orElseGet(() -> {
                System.out.println("Using default outputFile: " + DEFAULT_OUTPUT_FILE);
                return DEFAULT_OUTPUT_FILE;
            });

        try {
            UnitsOfMeasureDataGeneration unitsOfMeasureDataGeneration = new UnitsOfMeasureDataGeneration();
            unitsOfMeasureDataGeneration.execute(outputFile);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void execute(Path outputFile) throws Exception {
        Path excelInput = findExcelInput();
        Workbook workbook = XSSFWorkbookFactory.create(excelInput.toFile());
        Sheet annexIIAndIII = workbook.getSheetAt(2);

        Stream<UomRow> uomRows = readSheet(annexIIAndIII);

        Writer writer = new FileWriter(outputFile.toFile());

        StatefulBeanToCsv<UomRow> beanToCsv = new StatefulBeanToCsvBuilder<UomRow>(writer)
            .withApplyQuotesToAll(false)
            .withOrderedResults(true)
            .withMappingStrategy(new AnnotationMappingStrategy<>(UomRow.class))
            .build();

        beanToCsv.write(uomRows);
        writer.close();

    }

    public static Path findExcelInput() {
        List<Path> candidates = new ArrayList<>();
        try {
            List<URL> resourceRoots = EnumerationUtils.toList(FileHelper.class.getClassLoader().getResources(""));
            for (URL resourceRoot : resourceRoots) {
                Path path = Paths.get(resourceRoot.toURI());
                if (Files.isDirectory(path)) {
                    Files.list(path)
                        .filter(Files::isRegularFile)
                        .filter(p -> {
                            String fileName = p.getFileName().toString();
                            return StringUtils.endsWithIgnoreCase(fileName, ".xls") || StringUtils.endsWithIgnoreCase(fileName, ".xlsx");
                        })
                        .forEach(candidates::add);
                }
            }
        } catch (Exception e) {
            throw new IllegalStateException("Could not find excel source", e);
        }

        switch (candidates.size()) {
            case 0:
                throw new IllegalStateException("found no excel source file. please make sure there is an excel source file in the resources folder");
            case 1:
                return candidates.get(0);
            default:
                throw new IllegalStateException("found multiple excel source files. please make sure there is only one file available");
        }
    }

    /**
     * Read the whole given sheet
     */
    private Stream<UomRow> readSheet(Sheet sheet) {
        Iterator<Row> iterator = sheet.rowIterator();
        Row headerRow = iterator.next(); //do not use the header row
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED), false)
            .map(this::readRow);
    }

    /**
     * Process a single line and read it's values
     */
    private UomRow readRow(Row row) {
        String status = readCellValue(row, 0);
        String commonCode = readCellValue(row, 1);
        String name = readCellValue(row, 2);
        String description = readCellValue(row, 3);
        String level = readCellValue(row, 4);
        String symbol = readCellValue(row, 5);
        String conversionFactor = readCellValue(row, 6);

        return new UomRow(status, commonCode, name, description, level, symbol, conversionFactor);
    }

    private String readCellValue(Row row, int cellNum) {
        Cell cell = row.getCell(cellNum, MissingCellPolicy.CREATE_NULL_AS_BLANK);
        String cellValue = STRING_CELL_TEXT_FORMATTER.simpleFormat(cell);
        String cellValueWithoutNewlines = cellValue.replaceAll("\\R", ",");
        return cellValueWithoutNewlines;
    }

}
