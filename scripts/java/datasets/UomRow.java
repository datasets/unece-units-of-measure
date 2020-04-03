package datasets;

import com.opencsv.bean.CsvBindByName;

public class UomRow {

    @CsvBindByName(column = "Status")
    private final String status;

    @CsvBindByName(column = "CommonCode")
    private final String commonCode;

    @CsvBindByName(column = "Name")
    private final String name;

    @CsvBindByName(column = "Description")
    private final String description;

    @CsvBindByName(column = "LevelAndCategory")
    private final String level;

    @CsvBindByName(column = "Symbol")
    private final String symbol;

    @CsvBindByName(column = "ConversionFactor")
    private final String conversionFactor;

    public UomRow(String status, String commonCode, String name, String description, String level, String symbol, String conversionFactor) {
        this.status = status;
        this.commonCode = commonCode;
        this.name = name;
        this.description = description;
        this.level = level;
        this.symbol = symbol;
        this.conversionFactor = conversionFactor;
    }

    public String getStatus() {
        return this.status;
    }

    public String getCommonCode() {
        return this.commonCode;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public String getLevel() {
        return this.level;
    }

    public String getSymbol() {
        return this.symbol;
    }

    public String getConversionFactor() {
        return this.conversionFactor;
    }

    public String toString() {
        return "UomRow(status=" + this.getStatus() + ", commonCode=" + this.getCommonCode() + ", name=" + this.getName() + ", description=" + this.getDescription() + ", level=" + this.getLevel() + ", symbol=" + this.getSymbol() + ", conversionFactor=" + this.getConversionFactor() + ")";
    }
}
