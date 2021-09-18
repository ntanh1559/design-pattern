package vn.mekosoft.tool.chamcong;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import ctu.edu.vn.excel.export.Excel;
import ctu.edu.vn.excel.export.ExportExcel;

public class BangChamCong {
	static String projectPath = "/Users/admin/git/repository/TaoBaoCao";
	static String templatePath = projectPath+"/template/chamcong.xlsx";
	static String outputPath = projectPath+"/output/nhansu";
	static String inputPath = projectPath+"/input/nhansu";
	public static void main(String[] args) {
		System.out.println("Start processing input path="+inputPath);
		List<String> khuVucFolders = listFolder(inputPath);
		for(String folder : khuVucFolders) {
			System.out.println("input="+folder);
		}
		for(String folder : khuVucFolders) {
			//Tao thu muc cho tung Khu vuc
			String folderPath = outputPath+"/"+folder;
			File theDir = new File(folderPath);
			if (!theDir.exists()){
			    theDir.mkdirs();
			}
			//List tat ca cac file nhan su .csv trong thu muc
			List<File> fileNhanSus = listFile(inputPath+"/"+folder);
			for(File danhSachNhanVien : fileNhanSus) {
				try {
					String fileName = getFilename(danhSachNhanVien);
					File folderKhuVuc = new File(outputPath+"/"+folder+"/"+fileName+"/");
					if (!folderKhuVuc.exists()){
						folderKhuVuc.mkdirs();
					}
					//Tao 12 bangcham cong 
					taoBangLuong(danhSachNhanVien,folderKhuVuc.getAbsolutePath());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	public static void taoBangLuong(File danhSachNhanVien,String outputFolder) throws IOException {
		List<String> nhanViens = new ArrayList<String>();
		 List<String[]> collect =
		          Files.lines(danhSachNhanVien.toPath())
		                .map(line -> line.split(","))
		                .collect(Collectors.toList());
		for(String[] line : collect) {
			nhanViens.add(line[0]);
		}
		String fileName = getFilename(danhSachNhanVien);
		File file = new File(templatePath);
		for(int month=0;month<12;month++){
			 Calendar cal = Calendar.getInstance();
			 int nam = cal.get(Calendar.YEAR);
			 int thang = month + 1;
			String tenBang ="BẢNG CHẤM CÔNG NHÂN SỰ - THÁNG "+thang+"/"+nam+"\n "+fileName;
			List<Object> headers = new ArrayList<Object>();
			headers.add(tenBang);
			
			    cal.set(Calendar.MONTH, month);
			    cal.set(Calendar.DAY_OF_MONTH, 1);
			    int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
			    System.out.println(cal.get(Calendar.MONTH)+1+" have "+cal.getActualMaximum(Calendar.DAY_OF_MONTH));
			    for (int day = 1; day <= 31; day++) {
			    	if(day<=maxDay) {
			    		headers.add(day);
			    	}else {
			    		headers.add("");
			    	}
			        
			    }
			    List<List<Object>> contents = new ArrayList<List<Object>>();
			    int i=1;
			    for(String nhanVien : nhanViens) {
			    	List<Object> objects = new ArrayList<Object>();
			    	//STT
			    	objects.add(i);
			    	//Ho ten
			    	objects.add(nhanVien);
			    	//Add du lieu 1-31 ngay
			    	int soNgayDiLam = 0;
			    	 for (int day = 1; day <=31; day++) {
			    		 if(day<=maxDay) {
			    			 Calendar calendar = Calendar.getInstance();
			    			 calendar.set(Calendar.MONTH, month);
			    			 calendar.set(Calendar.DAY_OF_MONTH, day);
							 if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
								 objects.add(0);
							 }else {
								 objects.add(1);
								 soNgayDiLam++;
							 }
			    		 }else {
			    			 objects.add("");
			    		 }
					 }
			    	 objects.add("");
			    	 objects.add("");
			    	 objects.add("");
			    	 objects.add(soNgayDiLam);
			    	 objects.add("");
			    	 objects.add(soNgayDiLam);
			    	 contents.add(objects);
			    	 i++;
			    }
			Excel excel = new Excel(headers, contents, null, null);
			try {
				ExportExcel exportExcel = new ExportExcel(new FileInputStream(file), excel);
				exportExcel.run();
				 FileOutputStream out = new FileOutputStream(outputFolder+"/"+"BangChamCong_"+thang+"_"+nam+".xlsx");
				exportExcel.write(out);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public static String getFilename(File file) {
		String fileName = file.getName();
		int pos = fileName.lastIndexOf(".");
		if (pos > 0 && pos < (fileName.length() - 1)) { // If '.' is not the first or last character.
		    fileName = fileName.substring(0, pos);
		}
		return fileName;
	}
	public static List<String> listFolder(String path) {
		File file = new File(path);
		List<String> folders = new ArrayList<String>();
		if(file.exists()) {
			System.out.println("file list="+file.list());
			String[] directories = file.list(new FilenameFilter() {
				  @Override
				  public boolean accept(File current, String name) {
				    return new File(current, name).isDirectory();
				  }
				});
				
				 if(directories!=null) {
						for(String folder : directories) {
							folders.add(folder);
						}
				 }
		}else {
			System.out.println("File "+path+" not found");
		}
		
		return folders;
	}
	public static List<File> listFile(String folderPath) {
		List<File> files = new ArrayList<File>();
		 try (Stream<Path> walk = Files.walk(Paths.get(folderPath))) {

		        List<String> result = walk.filter(Files::isRegularFile)
		                .map(x -> x.toString()).collect(Collectors.toList());

		        for(String r : result) {
		        	files.add(new File(r));
		        }

		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		 return files;
	}
}
