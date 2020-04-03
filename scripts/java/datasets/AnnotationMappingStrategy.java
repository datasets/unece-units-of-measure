package datasets;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.HeaderColumnNameTranslateMappingStrategy;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AnnotationMappingStrategy<T> extends HeaderColumnNameTranslateMappingStrategy<T> {

    Map<String, String> columnMap = new LinkedHashMap<>();

    public AnnotationMappingStrategy(Class<? extends T> clazz) {

        for (Field field : clazz.getDeclaredFields()) {
            CsvBindByName annotation = field.getAnnotation(CsvBindByName.class);
            if (annotation != null) {
                columnMap.put(field.getName().toUpperCase(), annotation.column());
            }
        }
        setType(clazz);
        List<String> orderedKeys = new ArrayList<>(columnMap.keySet());
        setColumnOrderOnWrite(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                int i1 = orderedKeys.contains(o1) ? orderedKeys.indexOf(o1) : orderedKeys.size();
                int i2 = orderedKeys.contains(o2) ? orderedKeys.indexOf(o2) : orderedKeys.size();
                return i1 - i2;
            }
        });
    }

    @Override
    public String getColumnName(int col) {
        String name = headerIndex.getByPosition(col);
        return name;
    }

    public String getColumnName1(int col) {
        String name = headerIndex.getByPosition(col);
        if (name != null) {
            name = columnMap.get(name);
        }
        return name;
    }

    @Override
    public String[] generateHeader(T bean) throws CsvRequiredFieldEmptyException {
        String[] result = super.generateHeader(bean);
        for (int i = 0; i < result.length; i++) {
            result[i] = getColumnName1(i);
        }
        return result;
    }
}