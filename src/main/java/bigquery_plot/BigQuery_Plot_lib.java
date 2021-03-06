package bigquery_plot;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryOptions;
import com.google.cloud.bigquery.JobException;
import com.google.cloud.bigquery.QueryJobConfiguration;
import com.google.cloud.bigquery.TableResult;

public class BigQuery_Plot_lib {
	
	BigQuery bigQuery;
	private String tableName;
	private String fileName;
	
	public BigQuery_Plot_lib(String key, String tableName) {
		this.tableName = tableName;
		this.fileName = "csv/"+tableName+".csv";
		try {
			this.bigQuery = getClientWithJsonKey(key);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public TableResult getSelect(int idx) {
		TableResult response = null;
		StringBuilder buff = new StringBuilder();
		buff.append("select sum0, sum1, sum2,sum3,sum4,sum5,sum6,sum7,sum8,sum9,sum10, sum11, sum12,sum13,sum14,sum15,sum16,sum17,sum18,sum19,sum20, sum21, sum22,sum23,sum24,sum25,sum26,sum27,sum28,sum29,sum30, sum31, sum32,sum33,sum34,sum35,sum36,sum37,sum38,sum39,sum40, sum41, sum42,sum43,sum44,sum45,sum46,sum47,sum48,sum49 from mznfe."+ tableName);
		//buff.append("select * from mznfe."+ tableName);
		buff.append(" where index = "+idx);
		String query = buff.toString();
		System.out.println(query);
		QueryJobConfiguration queryConfig = QueryJobConfiguration.of(query);
	    try {
			response = bigQuery.query(queryConfig);
		} catch (JobException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
	}

	//全てのレコードを対象として、order by indexで並べ替えをして取得
	public TableResult getSelectAll() {
		TableResult response = null;
		StringBuilder buff = new StringBuilder();
		buff.append("select * from mznfe."+ tableName);
		buff.append(" order by index");
		String query = buff.toString();
		System.out.println(query);
		QueryJobConfiguration queryConfig = QueryJobConfiguration.of(query);
	    try {
			response = bigQuery.query(queryConfig);
		} catch (JobException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
	}

	
	//複数種類のデータを一度に取り込む場合
	public double[][] getCSV2(int row, int column) {
		//CSVから取り込み
		double csvdata[][] = new double[row][column];
		try {
			File f = new File(fileName);
			BufferedReader br = new BufferedReader(new FileReader(f));
					 
			String[][] data = new String[row][column]; 
			String line = br.readLine();
			for (int i = 0; line != null; i++) {
				data[i] = line.split(",", 0);
				line = br.readLine();
			}
			br.close();
			
			// CSVから読み込んだ配列の中身を処理
			for(int i = 0; i < data.length; i++) {
				for(int j = 0; j < data[0].length; j++) {
					csvdata[i][j] = Double.parseDouble(data[i][j]);
				}
			} 
		} catch (IOException e) {
				System.out.println(e);
		}
			//CSVから取り込みここまで	
		return csvdata;
	}
	
	public double[][] getCSV2(int row, int column, String fileName) {
		//CSVから取り込み
		double csvdata[][] = new double[row][column];
		try {
			File f = new File(fileName);
			BufferedReader br = new BufferedReader(new FileReader(f));
					 
			String[][] data = new String[row][column]; 
			String line = br.readLine();
			for (int i = 0; line != null; i++) {
				data[i] = line.split(",", 0);
				line = br.readLine();
			}
			br.close();
			
			// CSVから読み込んだ配列の中身を処理
			for(int i = 0; i < data.length; i++) {
				for(int j = 0; j < data[0].length; j++) {
					csvdata[i][j] = Double.parseDouble(data[i][j]);
				}
			} 
		} catch (IOException e) {
				System.out.println(e);
		}
			//CSVから取り込みここまで	
		return csvdata;
	}
	
	//CSVファイルを2つ取り込んで差分をとる
	public double[][] getCSVDiff(String filename1, String filename2, int row, int column){
		double diff[][] = new double[row][column];
		double csvdata1[][] = new double[row][column];
		double csvdata2[][] = new double[row][column];
		
		csvdata1 = this.getCSV2(row, column, filename1);
		csvdata2 = this.getCSV2(row, column, filename2);
		//System.out.println("PlotData" +Arrays.deepToString(csvdata1));
		
		for(int i = 0; i < row; i++) {
			for(int j = 0; j < column; j++) {
				diff[i][j] = csvdata2[i][j] - csvdata1[i][j];
			}
		}
		
		return diff;
	}
	
	public void exportCsv(double[][] csvdata, String fileName){
	     
        try {
 
            // 出力ファイルの作成
            FileWriter f = new FileWriter("csv/"+fileName+".csv", false);
            PrintWriter p = new PrintWriter(new BufferedWriter(f));
  
            // 内容をセットする
            for(int i = 0; i < csvdata.length; i++){
            		for(int j = 0; j < csvdata[0].length; j++) {
            			p.print(csvdata[i][j]);
            			p.print(",");
            		}
            		p.println();    // 改行
            }
 
            // ファイルに書き出し閉じる
            p.close();
 
            System.out.println(fileName+":ファイル出力完了！");
 
        } catch (IOException ex) {
            ex.printStackTrace();
        }
         
    }
	
	//jsonファイル認証関数
	public BigQuery getClientWithJsonKey(String key) throws IOException {
		return BigQueryOptions.newBuilder()
				.setCredentials(ServiceAccountCredentials.fromStream(new FileInputStream(key)))
				.build()
				.getService();
	}

}
