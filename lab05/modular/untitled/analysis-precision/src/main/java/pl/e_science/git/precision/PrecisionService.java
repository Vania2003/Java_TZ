package pl.e_science.git.precision;

import pl.e_science.git.ivahan0788.AnalysisException;
import pl.e_science.git.ivahan0788.AnalysisService;
import pl.e_science.git.ivahan0788.DataSet;

public class PrecisionService implements AnalysisService {
    private DataSet inputData;
    private DataSet result;
    @Override
    public void setOptions(String[] options) throws AnalysisException {
        // No options required
    }

    @Override
    public String getName() {
        return "Precision Service";
    }

    @Override
    public void submit(DataSet ds) throws AnalysisException {
        if(inputData != null){
            throw new AnalysisException("Data analyzing is already executed");
        }
        inputData = ds;
        countPrecision();
    }

    private void countPrecision() throws AnalysisException {
        String[][] confusionMatrix = inputData.getData();
        int n = confusionMatrix.length;
        int m = confusionMatrix[0].length;
        if(n != m){
            inputData = null;
            throw new AnalysisException("Confusion Matrix must be square!(NxN)");
        }

        double sumPrecision = 0;

        for(int i = 0; i < n; i++){
            int tp = Integer.parseInt(confusionMatrix[i][i]);
            int fp = 0;
            for(int j = 0; j < n; j++){
                if(i != j){
                    fp += Integer.parseInt(confusionMatrix[j][i]);
                }
            }
            double precision = (tp + fp) == 0 ? 0 : (double) tp / (tp + fp);
            sumPrecision += precision;
        }

        double macroPrecision = sumPrecision / n;
        result = new DataSet();
        result.setHeader(new String[]{"Precision"});
        result.setData(new String[][]{{String.format("%.4f", macroPrecision)}});
    }

    @Override
    public DataSet retrieve(boolean clear) throws AnalysisException {
        if(result == null){
            return null;
        }
        DataSet returnData = result;
        if(clear){
            inputData = null;
            result = null;
        }
        return returnData;
    }
}
