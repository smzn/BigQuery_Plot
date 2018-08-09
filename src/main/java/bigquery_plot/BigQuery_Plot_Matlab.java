package bigquery_plot;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import com.mathworks.engine.EngineException;
import com.mathworks.engine.MatlabEngine;
import com.mathworks.engine.MatlabExecutionException;
import com.mathworks.engine.MatlabSyntaxException;

public class BigQuery_Plot_Matlab {
	
	Future<MatlabEngine> eng;
	MatlabEngine ml;
	private double data[][];
	private String tableName;
	
	public BigQuery_Plot_Matlab(double[][] data, String tableName) {
		this.tableName = tableName;
		this.data = data;
		this.eng = MatlabEngine.startMatlabAsync();
		try {
			ml = eng.get();
			ml.putVariableAsync("data", data);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	//https://jp.mathworks.com/help/matlab/ref/imagesc.html
	//スケーリングした色によるイメージの表示
	//https://jp.mathworks.com/help/matlab/ref/contour.html
	//行列の等高線図
	public void getImagesc() {
		try {
			ml.eval("h = imagesc(data);");
			ml.eval("pause(10);");
			ml.eval("saveas(gcf,'"+tableName+"_imagesc.png')");
			ml.eval("contourf(data);");
			ml.eval("pause(10);");
			ml.eval("saveas(gcf,'"+tableName+"_contourf.png')");
			ml.eval("contour(data);");
			ml.eval("pause(10);");
			ml.eval("saveas(gcf,'"+tableName+"_contour.png')");
		} catch (MatlabExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MatlabSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CancellationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (EngineException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
