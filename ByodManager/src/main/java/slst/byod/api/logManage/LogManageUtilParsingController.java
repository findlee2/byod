package slst.byod.api.logManage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import slst.byod.api.util.Base64Utils;
import slst.byod.api.util.ByodApiUtil;

@Slf4j
@Api(value = "ByodManager API")
@RestController
@RequestMapping(value = "", produces = { "application/json" })
public class LogManageUtilParsingController {
	@Autowired LogManageMapper logManageMapper;
	
	@Value("${daumApiKey}")
	String daumApiKey;
	
	@Value("${Globals.RoundKey}")
	private String RoundKey;
	
	Base64Utils base64 = new Base64Utils();
	
	/**
	 * 조사업무 등록 전 로그ID 조회(관리자용)
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public String adminBeforeRegistBusinessNoSrch(String type) throws Exception {

		
		String tmpResult     = "";
		String responseBody  = "";
		
		tmpResult = logManageMapper.selectMaxLogId();
		
		log.info("[adminBeforeRegistBusinessNoSrch method - tmpResult]: "+tmpResult);
		
		//타입 1:보고서, 2:로그
		responseBody = ByodApiUtil.numberParsing(type, tmpResult);
		
		log.info("[adminBeforeRegistBusinessNoSrch method - responseBody]: "+responseBody);
		
		return responseBody;
	}
	
	/**
	 * 조사업무 등록 로그 생성(관리자용)
	 * @param logGb
	 * @param reportNo
	 * @param businessNm
	 * @param userId
	 * @param userNm
	 * @param reportUploadTm
	 * @param reportStatus
	 * @param networkPath
	 * @param userLon
	 * @param userLat
	 * @param disposeUserNm
	 * @param disposeUserId
	 * @throws Exception
	 */
	public void adminInsertRegistBusinessLog(String logGb, String reportNo,
			String businessNm,     String userId,       String userNm,
			String reportUploadTm, String reportStatus,	String networkPath,
			String userLon,        String userLat,      String disposeUserNm, String disposeUserId) throws Exception {

		LogManageVO logVO = new LogManageVO();
		int rtnCnt        = 0;
		
		try{
			
			logVO.setLog_id(adminBeforeRegistBusinessNoSrch("2"));
			logVO.setLog_gb(logGb);
			logVO.setReport_no(reportNo);
			logVO.setBusiness_nm(base64.encrypt(businessNm, RoundKey));
			logVO.setUser_id(userId);
			logVO.setUser_nm(base64.encrypt(userNm, RoundKey));
			logVO.setReport_upload_tm(reportUploadTm);
			logVO.setReport_status(reportStatus);
			logVO.setNetwork_path(networkPath);
			logVO.setAccess_loctn(base64.encrypt(ByodApiUtil.locationParsing(userLon,userLat,daumApiKey), RoundKey));
			logVO.setDispose_user_nm(base64.encrypt(disposeUserNm, RoundKey));
			logVO.setDispose_user_id(disposeUserId);
			logVO.setLocation_lat(userLat);
			logVO.setLocation_lon(userLon);
			
			rtnCnt = logManageMapper.insertRegistBusinessLog(logVO);
			
			log.info("[adminInsertRegistBusinessLog method - rtnCnt]: "+rtnCnt);
			
		}catch(Exception e){
			e.getStackTrace();
		}
	}
	
}
