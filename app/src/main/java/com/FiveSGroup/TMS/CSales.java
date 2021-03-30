package com.FiveSGroup.TMS;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.app.Application;

public class CSales extends Application {

	public static String SalesCode_CDS="";
	public static String SalesCode = "";
	private static String folder = "TMS";
	private static String filename = "fsys_tms.txt";
	File f;

	
	//hàm lấy mã NVBH, CDS, ASM từ file fsys
	public static String getSaleCode(String saleCode) {
		try {
			if (SalesCode.trim() == "") {
				ArrayList<String> FsysInfo = new CSales().readFileSystem(folder,filename) ;
					if (FsysInfo.size() > 0)
					{
						SalesCode = FsysInfo.get(0); //get(0) tương ứng với dòng thứ 0 trong file txt
					}
			}
		} catch (Exception e) {
			// TODO: handle exception
			//CmnFns.writeLogError("getAuth " + e.getMessage());
				
		}
		return SalesCode.replaceAll(global.RegexReplaceStringNonASCII, ""); //xóa các kí tự đặc biệt
	}

    // lấy thông tin profile


	// hàm đọc file thành mảng string
	public ArrayList<String> readFileSystem(String folder,
											String filename) {
		ArrayList<String> arr = new ArrayList<String>();

		try {

			if (!f.exists())
				return arr;

			InputStream instream = new FileInputStream(f);
			if (instream != null) {
				// prepare the file for reading
				InputStreamReader inputreader = new InputStreamReader(instream,
						"UTF-8");
				BufferedReader buffreader = new BufferedReader(inputreader);
				String line;
				do {
					line = buffreader.readLine();
					if (line != null) {
						arr.add(line);
					}
				} while (line != null);

				inputreader.close();
				buffreader.close();

			}
			instream.close();

		} catch (FileNotFoundException e) {
			return new ArrayList<String>();

		} catch (Exception e) {
			return new ArrayList<String>();
		}
		return arr;
	}




}
