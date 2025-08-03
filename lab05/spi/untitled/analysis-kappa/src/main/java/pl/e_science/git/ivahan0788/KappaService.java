package pl.e_science.git.ivahan0788;

import com.google.auto.service.AutoService;
import pl.e_science.git.ivahan0788.AnalysisException;
import pl.e_science.git.ivahan0788.AnalysisService;
import pl.e_science.git.ivahan0788.DataSet;

@AutoService(AnalysisService.class)
public class KappaService implements AnalysisService {
    private DataSet inputData;
    private DataSet result;

    @Override
    public void setOptions(String[] options) throws AnalysisException {
        // no options are required
    }

    @Override
    public String getName() {
        return "Kappa Service";
    }

    @Override
    public void submit(DataSet ds) throws AnalysisException {
        if(inputData != null){
            throw new AnalysisException("Data analyzing is already executed");
        }
        inputData = ds;
        countKappa();
    }

    private void countKappa() throws AnalysisException {
        String[][] confusionMatrix = inputData.getData();
        int n = confusionMatrix.length;
        int m = confusionMatrix[0].length;
        if(n != m){
            inputData = null;
            throw new AnalysisException("Confusion Matrix must be square!(NxN)");
        }

        int[] rowSums = new int[n];
        int[] colSums = new int[n];
        int total = 0;
        int observedAgreement = 0;

        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                int val = Integer.parseInt(confusionMatrix[i][j]);
                rowSums[i] += val;
                colSums[j] += val;
                total += val;
                if(i == j){
                    observedAgreement += val;
                }
            }
        }

        double po = (double) observedAgreement / total;
        double pe = 0.0;
        for(int i = 0; i < n; i++){
            pe += (double) rowSums[i] * colSums[i];
        }
        pe /= (total * total);

        double kappa = (po - pe) / (1 - pe);
        result = new DataSet();
        result.setHeader(new String[]{"Kappa"});
        result.setData(new String[][]{{String.format("%.4f", kappa)}});

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
