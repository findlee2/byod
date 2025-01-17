package slst.byod.api.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ByodApiUtil {
	
	static String[] GOOD_EXTENSION      = {"mp3", "jpg","png", "mp4"}; //향후 확장을 고려하여 배열로 세팅
	static String[] GOOD_EXTENSION_TYPE = {"1"  , "2"  , "2" ,"3"};    //오디오:1, 이미지:2, 비디오:3
	
	/**
	 * 보고서 NO, 로그 ID, 조사장비 ID, 첨부파일 ID 파싱 
	 * @param reportType   1:SL_ResultReport_ , 2:SYSLOG_ , 3:EqupmntID_ , 4:FILE_
	 * @param num          조회되서 넘어온 일련번호
	 * @param indexOfstr   ex)t_ , G_ , D_, E_
	 * @param format       타입에 맞게 자릿수 포맷팅
	 * @return
	 */
	public static String numberParsing(String pType ,String pNum){
		
		String result     = "";
		String reportType = "";
		String format     = "";
		String indexOfstr = "";
		int tmpLogId      = 0;
		int tmpLogIdFmat  = 0;
		
		if(pType.equals("1")){
			reportType = "SL_ResultReport_";
			format     = "%06d";
			indexOfstr = "t_";
		}else if(pType.equals("2")) {
			reportType = "SYSLOG_";
			format     = "%010d";
			indexOfstr = "G_";
		}else if(pType.equals("3")){
			reportType = "EqupmntID_";
			format     = "%06d";
			indexOfstr = "D_";
		}else{
			reportType = "FILE_";
			format     = "%010d";
			indexOfstr = "E_";
		}
		
		if(pNum == null){
			tmpLogIdFmat= 1;
		}else{
			
			tmpLogId = Integer.parseInt(pNum.substring(pNum.indexOf(indexOfstr)+2));
			tmpLogIdFmat= tmpLogId+1;
		}
		
		result = reportType + String.format(format , tmpLogIdFmat);
		
		return result;
	}
	
	/**
	 * 좌표 -> 주소로 파싱(다음 OpenApi)
	 * @param lon
	 * @param lat
	 * @return
	 * @throws ParseException
	 */
	@SuppressWarnings("rawtypes")
	public static String locationParsing(String lon, String lat, String apiKey) throws ParseException{
		
		String rtnStr = "";
		
		if(lon != null){
			
			try {
				
				URL url = new URL("https://apis.daum.net/local/geo/coord2detailaddr?"
						+ "apikey="+apiKey
						+ "&x="+lon
						+ "&y="+lat
						+ "&inputCoordSystem=WGS84"
						+ "&output=json");
				
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				conn.setRequestProperty("Accept", "application/json");
				
				if (conn.getResponseCode() != 200) {
					throw new RuntimeException("Failed : HTTP error code : "
							+ conn.getResponseCode());
				}
				
				JSONParser jsonParser = new JSONParser();
				
				Object obj = jsonParser.parse(new InputStreamReader((conn.getInputStream())));
				
				JSONObject jsonObject = (JSONObject) obj;
				
				rtnStr = ((HashMap) jsonObject.get("new")).get("name").toString();
				
				log.info("Output from Server .... ");
				
				log.info("접속위치 : ["+rtnStr+"]");
				
				conn.disconnect();
				
			} catch (MalformedURLException e) {
				
				e.printStackTrace();
				
			} catch (IOException e) {
				
				e.printStackTrace();
				
			}
			
		}else{
			rtnStr = null;
		}
		
		return rtnStr;
	}
	
	/**
	 * null 체크(null이면 true 리턴)
	 * @param s
	 * @return
	 */
	public static boolean isEmpty(Object s) {
        if (s == null) {
            return true;
        }
        if ((s instanceof String) && (((String)s).trim().length() == 0)) {
            return true;
        }
        if (s instanceof Map) {
            return ((Map<?, ?>)s).isEmpty();
        }
        if (s instanceof List) {
            return ((List<?>)s).isEmpty();
        }
        if (s instanceof Object[]) {
            return (((Object[])s).length == 0);
        }
        return false;
    }
	
	/**
	 * 이미지,오디오 파일을 Base64 encoding 된 바이너리 파일로 리턴
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static String encodeBase64Convert(String url) throws IOException {
		
		String outputPath    = "";
		String encodedString = "";
		outputPath           = url;
		
		
		File f = new File(outputPath);
		if(f.exists()){
			FileInputStream fis=new FileInputStream(f);
			ByteArrayOutputStream bos=new ByteArrayOutputStream();
			int b;
			byte[] buffer = new byte[1024];
			while((b=fis.read(buffer))!=-1){
				bos.write(buffer,0,b);
			}
			byte[] fileBytes=bos.toByteArray();
			fis.close();
			bos.close();
			
			byte[] encoded=Base64.encodeBase64(fileBytes);
			encodedString = new String(encoded);
			
		}
		return encodedString;
	}
	
	/**
	 * 확장자 체크
	 * @param ext
	 * @return
	 */
	public static boolean isValidFileExtension(String ext) {
        
        int len = GOOD_EXTENSION.length;
        
        for (int i = 0; i < len; i++) {
            if (ext.equalsIgnoreCase(GOOD_EXTENSION[i])) {
                return true;
            }
        }
        return false;
	}
	
	/**
	 * 확장자 타입 구분
	 * @param ext
	 * @return
	 */
	public static String isValidFileExtensionGb(String ext) {

		String rtnGb = "";
        int len      = GOOD_EXTENSION.length;
        
        for (int i = 0; i < len; i++) {
            if (ext.equalsIgnoreCase(GOOD_EXTENSION[i])) {
            	rtnGb = GOOD_EXTENSION_TYPE[i];
            }
        }
        return rtnGb;
	}
}
