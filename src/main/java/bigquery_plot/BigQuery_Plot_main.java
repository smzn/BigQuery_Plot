package bigquery_plot;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import com.google.cloud.bigquery.FieldValueList;
import com.google.cloud.bigquery.TableResult;
import com.mathworks.engine.MatlabEngine;

public class BigQuery_Plot_main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		int rownumber = 10;
		int column = 50;
		String tableName = "Fe_OFF_1_99";
		BigQuery_Plot_lib blib = new BigQuery_Plot_lib("/Users/mizuno/Downloads/closedqueue-929a267e03b8.json", tableName);
		double plotdata[][] = new double[rownumber][column];
		
		
		//BigQueryからSQLで取り込み
		for(int i = 0; i < rownumber; i++) {
			TableResult response = blib.getSelect(i+1);
			for (FieldValueList row : response.iterateAll()) {
    				for(int j = 0; j < column; j++) {
    					//System.out.println("("+i+","+(j+1)+")sum"+j+": "+row.get("sum"+j).getValue());
    					plotdata[i][j] = row.get("sum"+j).getDoubleValue();
    				}	
			}
		}
		
		//BigQueryで生成したCSVから取り込み(CSVファイルを並べ替えしておく) 
		//BigQueryからCSVに落とせなくて諦めた
		//plotdata = blib.getCSV2(column, column);
		
		//System.out.println("PlotData" +Arrays.deepToString(plotdata));
	
		BigQuery_Plot_Matlab bmat = new BigQuery_Plot_Matlab(plotdata, tableName);
		bmat.getImagesc();
		
	}

}
