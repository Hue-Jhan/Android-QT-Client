package data;

import java.io.*;
import java.sql.*;
import java.util.*;

import database.*;

import java.util.List;

public class Data<T extends Attribute> {
    private List<Example> data = new ArrayList<>();
    private int numberOfExamples;
    private List<Attribute> attributeSet;

    public Data(String tableName) throws SQLException, EmptySetException, DatabaseConnectionException, NoValueException {
        DbAccess db = new DbAccess();
        db.initConnection();

        attributeSet = new ArrayList<>();

        try {
            TableSchema tableSchema = new TableSchema(db, tableName);
            TableData tableData = new TableData(db);

            for (int i = 0; i < tableSchema.getNumberOfAttributes(); i++) {
                TableSchema.Column col = tableSchema.getColumn(i);

                if (col.isNumber()) {
                    Object minObj = tableData.getAggregateColumnValue(tableName, col, QUERY_TYPE.MIN);
                    Object maxObj = tableData.getAggregateColumnValue(tableName, col, QUERY_TYPE.MAX);

                    double min;
                    double max;

                    if (minObj instanceof Number && maxObj instanceof Number) {
                        min = ((Number) minObj).doubleValue();
                        max = ((Number) maxObj).doubleValue();
                    } else {
                        throw new NoValueException("Valori MIN/MAX non numerici per la colonna " + col.getColumnName());
                    }

                    attributeSet.add(new ContinuousAttribute(col.getColumnName(), i, min, max));
                } else {
                    Set<Object> distinctValues = tableData.getDistinctColumnValues(tableName, col);
                    String[] valuesArray = new String[distinctValues.size()];
                    int j = 0;
                    for (Object val : distinctValues) {
                        valuesArray[j++] = val.toString();
                    }

                    attributeSet.add(new DiscreteAttribute<String>(col.getColumnName(), i, valuesArray));
                }
            }

            data = tableData.getDistinctTransazioni(tableName);
            numberOfExamples = data.size();
        } catch (NoValueException e) {
            throw e;
        } finally {
            db.closeConnection();
        }
    }


    public int getNumberOfExamples() {
        return numberOfExamples;
    }

    public int getNumberOfAttributes() {
        return attributeSet.size();
    }

    public List<Attribute> getAttributeSchema() {
        return attributeSet;
    }

    public Attribute getAttribute(int index) {
        return attributeSet.get(index);
    }

    public Object getValue(int exampleIndex, int attributeIndex) {
        return data.get(exampleIndex).get(attributeIndex);
    }

    public Tuple getItemSet(int index) {
        Tuple tuple = new Tuple(attributeSet.size());

        for (int i = 0; i < attributeSet.size(); i++) {
            Attribute attr = attributeSet.get(i);
            Object value = data.get(index).get(i);
            Item item;

            if (attr instanceof DiscreteAttribute) {
                item = new DiscreteItem((DiscreteAttribute) attr, (String) value);
            } else if (attr instanceof ContinuousAttribute) {
                item = new ContinuousItem((ContinuousAttribute) attr, ((Number) value).doubleValue());
            } else {
                throw new IllegalArgumentException("Tipo di attributo non supportato");
            }

            tuple.add(item, i);
        }
        return tuple;
    }

    public String toString() {
        String s = "";
        Iterator<Attribute> it = attributeSet.iterator();
        while (it.hasNext()) {
            s += it.next().getName();
            if (it.hasNext()) s += ",";
        }
        s += "\n";

        for (int i = 0; i < getNumberOfExamples(); i++) {
            s += (i + 1) + ":";
            for (int j = 0; j < getNumberOfAttributes(); j++) {
                s += getValue(i, j);
                if (j < getNumberOfAttributes() - 1)
                    s += ", ";
            }
            s += "\n";
        }

        return s;
    }
}