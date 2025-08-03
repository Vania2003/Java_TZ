package pl.app;

import java.util.List;

public class FileData {
    private List<DataRecord> records;

    public FileData(List<DataRecord> records) {
        this.records = records;
    }

    public List<DataRecord> getRecords() {
        return records;
    }
}
