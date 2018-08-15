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

		int rownumber = 2048;
		int column = 2048;
		String tableName = "Fe_ON_200_299";
		BigQuery_Plot_lib blib = new BigQuery_Plot_lib("/Users/mizuno/Downloads/closedqueue-929a267e03b8.json", tableName);
		double plotdata[][] = new double[rownumber][column];
		
		
		//BigQueryからSQLで取り込み(1行ずつ、費用がかかるのでやってはいけない）
		/*
		for(int i = 0; i < rownumber; i++) {
			TableResult response = blib.getSelect(i+1);
			for (FieldValueList row : response.iterateAll()) {
    				for(int j = 0; j < column; j++) {
    					//System.out.println("("+i+","+(j+1)+")sum"+j+": "+row.get("sum"+j).getValue());
    					plotdata[i][j] = row.get("sum"+j).getDoubleValue();
    				}	
			}
		}
		*/
		
		//BigQueryを１度だけ発行。indexの順番に注意する(order by index)
		/*
		TableResult response = blib.getSelectAll();
		int i = 0;
		for (FieldValueList row : response.iterateAll()) {
    			for(int j = 0; j < column; j++) {
    				//System.out.println("("+i+","+(j+1)+")sum"+j+": "+row.get("sum"+j).getValue());
    				plotdata[i][j] = row.get("sum"+j).getDoubleValue();
    			}
    			i++;
		}
		*/
		
		
		//BigQueryで生成したCSVから取り込み(CSVファイルを並べ替えしておく) 
		plotdata = blib.getCSV2(rownumber, column);
		
		//System.out.println("PlotData" +Arrays.deepToString(plotdata));
	
		//各データ描画
		BigQuery_Plot_Matlab bmat = new BigQuery_Plot_Matlab(plotdata, tableName);
		bmat.getImagesc();
		
		//差分データ描画
		double diff[][] = blib.getCSVDiff("csv/Fe_ON_200_299.csv", "csv/Fe_OFF_200_299.csv", rownumber, column);
		//System.out.println("PlotData" +Arrays.deepToString(diff));
		BigQuery_Plot_Matlab bmat1 = new BigQuery_Plot_Matlab(diff, "Fe_200_299");
		bmat1.getImagesc();
		
	}

}
