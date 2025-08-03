package pl.e_science.git.accuracy;

import pl.e_science.git.ivahan0788.AnalysisException;
import pl.e_science.git.ivahan0788.AnalysisService;
import pl.e_science.git.ivahan0788.DataSet;

public class AccuracyService implements AnalysisService {
    private DataSet inputData;
    private DataSet result;
    @Override
    public void setOptions(String[] options) throws AnalysisException {
        // Service requires no options
    }

    @Override
    public String getName() {
        return "Accuracy Service";
    }

    @Override
    public void submit(DataSet ds) throws AnalysisException {
        if(inputData != null){
            throw new AnalysisException("Data analyzing is already executed");
        }
        inputData = ds;
        countAccuracy();
    }

    private void countAccuracy() throws AnalysisException {
        String[][] confusionMatrix = inputData.getData();
        int n = confusionMatrix.length;
        int m = confusionMatrix[0].length;
        if(n != m){
            inputData = null;
            throw new AnalysisException("Confusion Matrix must be square!(NxN)");
        }
        int correct = 0;
        int total = 0;

        for(int i = 0; i < n; i++){
            for(int j = 0; j < confusionMatrix[i].length; j++){
                int val = Integer.parseInt(confusionMatrix[i][j]);
                total += val;
                if(i == j) correct +=val;
            }
        }

        double accuracy = (double) correct / total;
        result = new DataSet();
        result.setHeader(new String[]{"Accuracy"});
        result.setData(new String[][]{{String.format("%.4f", accuracy)}});
    }

    @Override
    public DataSet retrieve(boolean clear) throws AnalysisException {
        if(result == null){return null;}
        DataSet resultToReturn = result;
        if(clear){
            inputData = null;
            result = null;
            return resultToReturn;
        }
        return resultToReturn;
    }
}
