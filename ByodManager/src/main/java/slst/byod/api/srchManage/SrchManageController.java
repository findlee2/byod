package slst.byod.api.srchManage;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import slst.byod.api.attchFileManage.AttchFileManageMapper;
import slst.byod.api.attchFileManage.AttchFileManageVO;
import slst.byod.api.logManage.LogManageUtilParsingController;
import slst.byod.api.userManage.UserManageMapper;
import slst.byod.api.userManage.UserManageVO;
import slst.byod.api.util.Base64Utils;
import slst.byod.api.util.ByodApiUtil;
import slst.byod.api.util.UserPwAlgorithm;

@Slf4j
@Api(value = "SearchBusiness manage API")
@RestController
@RequestMapping(value = "", produces = { "application/json" })
public class SrchManageController extends LogManageUtilParsingController{
	
	@Autowired SrchManageMapper      srchManageMapper;
	@Autowired AttchFileManageMapper attchManageMapper;
	@Autowired UserManageMapper      userManageMapper;
	
	@Value("${Globals.RoundKey}")
	private String RoundKey;
	
	@Value("${server.address}")
	private String sAddress;
	
	@Value("${server.port}")
	private String sPort;
	
	
	private static String fileStorePath;
	
	@Value("${fileStorePath}")
	private void setFileStroePath(String path){
		fileStorePath = path;
	}
	
	Base64Utils base64  = new Base64Utils();
	
	HttpSession session = null;
	
	/**
	 * 조사자가 자신의 조사업무 목록을 조회한다.(조사자용)
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "웹-조사업무 목록 조회(조사자용)", notes = "조사자가 자신의 조사 업무  리스트를 조회한다.", response = SrchManageVO.class)
	@ApiImplicitParams({
	    @ApiImplicitParam(name = "userId",   value = "사용자 아이디",  required = true, dataType = "string", paramType = "query")
	  })
	@ApiResponses(value = {@ApiResponse(code = 400, message = "Bad Request")})
	@RequestMapping(value = "/Byod/srchWebInfoList", method = RequestMethod.GET)
	public ResponseEntity<Object> srchWebInfoList(@RequestParam String userId) throws Exception {

		SrchManageVO srchVO             = new SrchManageVO();
		List<SrchManageVO> responseBody = null;
		srchVO.setUser_id(userId);
			
		responseBody = srchManageMapper.selectSrchWebInfoList(srchVO);

		for(int i=0; i<responseBody.size(); i++){
			//복호화
			responseBody.get(i).setBusiness_nm(base64.decrypt(responseBody.get(i).getBusiness_nm(), RoundKey));
			responseBody.get(i).setObject_place_nm(base64.decrypt(responseBody.get(i).getObject_place_nm(), RoundKey));
			responseBody.get(i).setObject_location(base64.decrypt(responseBody.get(i).getObject_location(), RoundKey));
			responseBody.get(i).setObject_use(base64.decrypt(responseBody.get(i).getObject_use(), RoundKey));
			responseBody.get(i).setObject_buil_struc(base64.decrypt(responseBody.get(i).getObject_buil_struc(), RoundKey));
			responseBody.get(i).setCheck_extgsh_result(base64.decrypt(responseBody.get(i).getCheck_extgsh_result(), RoundKey));
			responseBody.get(i).setCheck_alarm_equip_result(base64.decrypt(responseBody.get(i).getCheck_alarm_equip_result(), RoundKey));
			responseBody.get(i).setCheck_extgsh_equip_result(base64.decrypt(responseBody.get(i).getCheck_extgsh_equip_result(), RoundKey));
			responseBody.get(i).setCheck_flee_equip_result(base64.decrypt(responseBody.get(i).getCheck_flee_equip_result(), RoundKey));
			responseBody.get(i).setCheck_etc_equip_result(base64.decrypt(responseBody.get(i).getCheck_etc_equip_result(), RoundKey));
			responseBody.get(i).setCheck_user_nm(base64.decrypt(responseBody.get(i).getCheck_user_nm(), RoundKey));
		}
		
		return new ResponseEntity<Object>(responseBody, HttpStatus.OK);
	}
	
	/**
	 * 조사자가 자신의 조사업무 목록을 조회한다.(조사자용)
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "조사업무 목록 조회(조사자용)", notes = "조사자가 자신의 조사 업무  리스트를 조회한다.", response = SrchManageVO.class)
	@ApiImplicitParams({
	    @ApiImplicitParam(name = "userId",   value = "사용자 아이디",  required = true, dataType = "string", paramType = "query")
	  })
	@ApiResponses(value = {@ApiResponse(code = 400, message = "Bad Request")})
	@RequestMapping(value = "/Byod/srchInfoList", method = RequestMethod.GET)
	public ResponseEntity<Object> srchInfoList(@RequestParam String userId) throws Exception {

		SrchManageVO srchVO             = new SrchManageVO();
		List<SrchManageVO> responseBody = null;
		srchVO.setUser_id(userId);
			
		responseBody = srchManageMapper.selectSrchInfoList(srchVO);

		for(int i=0; i<responseBody.size(); i++){
			//복호화
			responseBody.get(i).setBusiness_nm(base64.decrypt(responseBody.get(i).getBusiness_nm(), RoundKey));
			responseBody.get(i).setObject_place_nm(base64.decrypt(responseBody.get(i).getObject_place_nm(), RoundKey));
			responseBody.get(i).setObject_location(base64.decrypt(responseBody.get(i).getObject_location(), RoundKey));
			responseBody.get(i).setObject_use(base64.decrypt(responseBody.get(i).getObject_use(), RoundKey));
			responseBody.get(i).setObject_buil_struc(base64.decrypt(responseBody.get(i).getObject_buil_struc(), RoundKey));
			responseBody.get(i).setCheck_extgsh_result(base64.decrypt(responseBody.get(i).getCheck_extgsh_result(), RoundKey));
			responseBody.get(i).setCheck_alarm_equip_result(base64.decrypt(responseBody.get(i).getCheck_alarm_equip_result(), RoundKey));
			responseBody.get(i).setCheck_extgsh_equip_result(base64.decrypt(responseBody.get(i).getCheck_extgsh_equip_result(), RoundKey));
			responseBody.get(i).setCheck_flee_equip_result(base64.decrypt(responseBody.get(i).getCheck_flee_equip_result(), RoundKey));
			responseBody.get(i).setCheck_etc_equip_result(base64.decrypt(responseBody.get(i).getCheck_etc_equip_result(), RoundKey));
			responseBody.get(i).setCheck_user_nm(base64.decrypt(responseBody.get(i).getCheck_user_nm(), RoundKey));
		}
		
		return new ResponseEntity<Object>(responseBody, HttpStatus.OK);
	}
	
	/**
	 * 조사자가 자신의 조사업무를 상세 조회한다.(조사자용)
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "조사업무 상세 조회(조사자용)", notes = "조사자가 자신의 조사 업무를  상세조회한다.", response = SrchManageVO.class)
	@ApiImplicitParams({
	    @ApiImplicitParam(name = "reportNo",   value = "보고서 번호",  required = true, dataType = "string", paramType = "query")
	  })
	@ApiResponses(value = {@ApiResponse(code = 400, message = "Bad Request")})
	@RequestMapping(value = "/Byod/srchDetailInfo", method = RequestMethod.GET)
	public ResponseEntity<Object> srchDetailInfo(@RequestParam String reportNo) throws Exception {

		SrchManageVO srchVO       = new SrchManageVO();
		SrchManageVO responseBody = null;
		srchVO.setReport_no(reportNo);
			
		responseBody = srchManageMapper.selectSrchDetailInfo(srchVO);
		//복호화
		responseBody.setBusiness_nm(base64.decrypt(responseBody.getBusiness_nm(), RoundKey));
		responseBody.setObject_place_nm(base64.decrypt(responseBody.getObject_place_nm(), RoundKey));
		responseBody.setObject_location(base64.decrypt(responseBody.getObject_location(), RoundKey));
		responseBody.setObject_use(base64.decrypt(responseBody.getObject_use(), RoundKey));
		responseBody.setObject_buil_struc(base64.decrypt(responseBody.getObject_buil_struc(), RoundKey));
		responseBody.setCheck_extgsh_result(base64.decrypt(responseBody.getCheck_extgsh_result(), RoundKey));
		responseBody.setCheck_alarm_equip_result(base64.decrypt(responseBody.getCheck_alarm_equip_result(), RoundKey));
		responseBody.setCheck_extgsh_equip_result(base64.decrypt(responseBody.getCheck_extgsh_equip_result(), RoundKey));
		responseBody.setCheck_flee_equip_result(base64.decrypt(responseBody.getCheck_flee_equip_result(), RoundKey));
		responseBody.setCheck_etc_equip_result(base64.decrypt(responseBody.getCheck_etc_equip_result(), RoundKey));
		responseBody.setCheck_user_nm(base64.decrypt(responseBody.getCheck_user_nm(), RoundKey));
				
		
		return new ResponseEntity<Object>(responseBody, HttpStatus.OK);
	}
	
	/**
	 * 조사업무 목록 조회(관리자용)
	 * @param 
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "조사업무 목록 조회(관리자용)", notes = "관리자가 조사 업무  리스트를  조회한다.", response = SrchManageVO.class)	
	@ApiResponses(value = {@ApiResponse(code = 400, message = "Bad Request")})
	@RequestMapping(value = "/Byod/adminSrchInfoList", method = RequestMethod.GET)
	public ResponseEntity<Object> adminSrchInfoList()throws Exception {
		
		List<SrchManageVO> responseBody = srchManageMapper.selectUserSrchInfoList();

		for(int i=0; i<responseBody.size(); i++){
			//복호화
			responseBody.get(i).setBusiness_nm(base64.decrypt(responseBody.get(i).getBusiness_nm(), RoundKey));
			responseBody.get(i).setObject_place_nm(base64.decrypt(responseBody.get(i).getObject_place_nm(), RoundKey));
			responseBody.get(i).setObject_location(base64.decrypt(responseBody.get(i).getObject_location(), RoundKey));
			responseBody.get(i).setObject_use(base64.decrypt(responseBody.get(i).getObject_use(), RoundKey));
			responseBody.get(i).setObject_buil_struc(base64.decrypt(responseBody.get(i).getObject_buil_struc(), RoundKey));
			responseBody.get(i).setCheck_extgsh_result(base64.decrypt(responseBody.get(i).getCheck_extgsh_result(), RoundKey));
			responseBody.get(i).setCheck_alarm_equip_result(base64.decrypt(responseBody.get(i).getCheck_alarm_equip_result(), RoundKey));
			responseBody.get(i).setCheck_extgsh_equip_result(base64.decrypt(responseBody.get(i).getCheck_extgsh_equip_result(), RoundKey));
			responseBody.get(i).setCheck_flee_equip_result(base64.decrypt(responseBody.get(i).getCheck_flee_equip_result(), RoundKey));
			responseBody.get(i).setCheck_etc_equip_result(base64.decrypt(responseBody.get(i).getCheck_etc_equip_result(), RoundKey));
			responseBody.get(i).setCheck_user_nm(base64.decrypt(responseBody.get(i).getCheck_user_nm(), RoundKey));
		}
		
		return new ResponseEntity<Object>(responseBody, HttpStatus.OK);
	}
	
	/**
	 * 조사업무 상세 조회(관리자용)
	 * @param reportNo
	 * @return
	 * @throws Exception
	 */
	/*@ApiOperation(value = "조사업무 상세 조회(관리자용)", notes = "관리자가 조사 업무  상세조회를 조회한다.", response = SrchManageVO.class)
	@ApiImplicitParams({
	    @ApiImplicitParam(name = "reportNo",   value = "보고서 번호",  required = true, dataType = "string", paramType = "query")
	  })
	@ApiResponses(value = {@ApiResponse(code = 400, message = "Bad Request")})
	@RequestMapping(value = "/Byod/adminSrchDetailInfo", method = RequestMethod.GET)
	public ResponseEntity<Object> adminSrchDetailInfo(@RequestParam("reportNo") String reportNo) throws Exception {

		SrchManageVO srchVO = new SrchManageVO();
		
		srchVO.setReport_no(reportNo);
		
		SrchManageVO responseBody = srchManageMapper.selectSrchDetailInfo(srchVO);

		//복호화
		responseBody.setBusiness_nm(base64.decrypt(responseBody.getBusiness_nm(), RoundKey));
		responseBody.setObject_place_nm(base64.decrypt(responseBody.getObject_place_nm(), RoundKey));
		responseBody.setObject_location(base64.decrypt(responseBody.getObject_location(), RoundKey));
		responseBody.setObject_use(base64.decrypt(responseBody.getObject_use(), RoundKey));
		responseBody.setObject_buil_struc(base64.decrypt(responseBody.getObject_buil_struc(), RoundKey));
		responseBody.setCheck_extgsh_result(base64.decrypt(responseBody.getCheck_extgsh_result(), RoundKey));
		responseBody.setCheck_alarm_equip_result(base64.decrypt(responseBody.getCheck_alarm_equip_result(), RoundKey));
		responseBody.setCheck_extgsh_equip_result(base64.decrypt(responseBody.getCheck_extgsh_equip_result(), RoundKey));
		responseBody.setCheck_flee_equip_result(base64.decrypt(responseBody.getCheck_flee_equip_result(), RoundKey));
		responseBody.setCheck_etc_equip_result(base64.decrypt(responseBody.getCheck_etc_equip_result(), RoundKey));
		responseBody.setCheck_user_nm(base64.decrypt(responseBody.getCheck_user_nm(), RoundKey));
				
		return new ResponseEntity<Object>(responseBody, HttpStatus.OK);
	}
	*/
	/**
	 * 조사업무 등록 전 보고서 번호 조회(관리자용)
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "조사업무 등록 전 보고서 번호 조회(관리자용)", notes = "관리자가 조사 업무 등록전에 생성가능한 보고서 번호를 조회한다.", response = SrchManageVO.class)	
	@RequestMapping(value = "/Byod/adminBeforeRegistBusiness", method = RequestMethod.GET)
	public ResponseEntity<Object> adminBeforeRegistBusiness() throws Exception {

		SrchManageVO responseBody = new SrchManageVO();
		String tmpResult          = "";
			
		tmpResult = srchManageMapper.selectMaxReportNo();
		
		//타입 1:보고서, 2:로그
		responseBody.setReport_no(ByodApiUtil.numberParsing("1", tmpResult));
		
		return new ResponseEntity<Object>(responseBody, HttpStatus.OK);
	}
	
	/**
	 * 조사업무 등록(관리자용)
	 * @param reportNo
	 * @param userId
	 * @param businessNm
	 * @param objectPlaceNm
	 * @param objectRelationUser
	 * @param objectRelationUserHomeTel
	 * @param objectRelationUserMobilTel
	 * @param objectLocation
	 * @param objectUse
	 * @param objectBuilStruc
	 * @param checkPeriodStart
	 * @param checkPeriodEnd
	 * @param checkUserNm
	 * @param checkUserQualficationClass
	 * @param checkUserQualficationNo
	 * @param headFireDepart
	 * @param locationLat
	 * @param locationLon
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "조사업무 등록(관리자용)", notes = "관리자가 조사업무 등록을 한다.(로그를 남길려면 관리자 로그인 후 진행해야한다.)")
	@ApiImplicitParams({
	    @ApiImplicitParam(name = "reportNo",                     value = "보고서 번호",                	required = true,  dataType = "string", paramType = "query"),
	    @ApiImplicitParam(name = "userId",                       value = "사용자 아이디",               	required = true,  dataType = "string", paramType = "query"),
	    @ApiImplicitParam(name = "businessNm",                   value = "업무명",                   	required = true,  dataType = "string", paramType = "query"),	    
	    @ApiImplicitParam(name = "objectPlaceNm",                value = "명칭",                     	required = true,  dataType = "string", paramType = "query"),
	    @ApiImplicitParam(name = "objectRelationUser",           value = "관계인",                   	required = true,  dataType = "string", paramType = "query"),
	    @ApiImplicitParam(name = "objectRelationUserHomeTel",    value = "관계인 전화('-'하이픈입력)",    	required = false, dataType = "string", paramType = "query"),
	    @ApiImplicitParam(name = "objectRelationUserMobilTel",   value = "관계인 휴대전화('-'하이픈입력) ", required = false, dataType = "string", paramType = "query"),
	    @ApiImplicitParam(name = "objectLocation",               value = "소재지",                   	required = true,  dataType = "string", paramType = "query"),
	    @ApiImplicitParam(name = "objectUse",                    value = "용도",                     	required = true,  dataType = "string", paramType = "query"),
	    @ApiImplicitParam(name = "objectBuilStruc",              value = "건물구조",                  	required = true,  dataType = "string", paramType = "query"),
	    @ApiImplicitParam(name = "checkPeriodStart",             value = "점검기간 시작일(ex 20161212)",	required = true,  dataType = "string", paramType = "query"),
	    @ApiImplicitParam(name = "checkPeriodEnd",               value = "점검기간 종료일(ex 20161212)",	required = true,  dataType = "string", paramType = "query"),
	    @ApiImplicitParam(name = "checkUserNm",                  value = "점검자 성명",                	required = true,  dataType = "string", paramType = "query"),
	    @ApiImplicitParam(name = "checkUserQualficationClass",   value = "자격구분",                  	required = true,  dataType = "string", paramType = "query"),
	    @ApiImplicitParam(name = "checkUserQualficationNo",      value = "자격번호('-'하이픈입력)",      	required = true,  dataType = "string", paramType = "query"),
	    @ApiImplicitParam(name = "headFireDepart",               value = "소방서장",                  	required = true,  dataType = "string", paramType = "query"),
	    @ApiImplicitParam(name = "locationLat",                  value = "소재지 위도",                	required = true,  dataType = "string", paramType = "query"),
	    @ApiImplicitParam(name = "locationLon",                  value = "소재지 경도",               	required = true,  dataType = "string", paramType = "query"),
	    @ApiImplicitParam(name = "adminUserId",                  value = "관리자 아이디",		        required = true,  dataType = "string", paramType = "query"),
	    @ApiImplicitParam(name = "adminUserNm",                  value = "관리자 이름",		            required = true,  dataType = "string", paramType = "query")
	  })	
	@ApiResponses(value = {@ApiResponse(code = 409, message = "Conflict(해당 보고서가 이미 존재)")})
	@RequestMapping(value = "/Byod/adminRegistBusiness", method = RequestMethod.POST)	
	public ResponseEntity<String> adminRegistBusiness(@RequestParam("reportNo") String reportNo,
			@RequestParam("userId") String userId,   @RequestParam("businessNm") String businessNm,
			@RequestParam("objectPlaceNm") String objectPlaceNm, 
			@RequestParam("objectRelationUser") String objectRelationUser,
			@RequestParam(value="objectRelationUserHomeTel", required=false) String objectRelationUserHomeTel,
			@RequestParam(value="objectRelationUserMobilTel", required=false) String objectRelationUserMobilTel,
			@RequestParam("objectLocation") String objectLocation,
			@RequestParam("objectUse") String objectUse,
			@RequestParam("objectBuilStruc") String objectBuilStruc,
			@RequestParam("checkPeriodStart") String checkPeriodStart,
			@RequestParam("checkPeriodEnd") String checkPeriodEnd,
			@RequestParam("checkUserNm") String checkUserNm,
			@RequestParam("checkUserQualficationClass") String checkUserQualficationClass,
			@RequestParam("checkUserQualficationNo") String checkUserQualficationNo,
			@RequestParam("headFireDepart") String headFireDepart,
			@RequestParam("locationLat") String locationLat, 
			@RequestParam("locationLon") String locationLon,
			@RequestParam("adminUserId") String adminUserId,
			@RequestParam("adminUserNm") String adminUserNm
			) throws Exception {
		
		SrchManageVO responseBody     = new SrchManageVO();
		
		try{
			
			if(ByodApiUtil.isEmpty(adminUserId)){
				log.info("[ByodApiUtil.isEmpty(adminUserId)] : null");
				return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			}
			
			responseBody.setReport_no(reportNo);
			responseBody.setUser_id(userId);
			responseBody.setBusiness_nm(base64.encrypt(businessNm, RoundKey));
			responseBody.setObject_place_nm(base64.encrypt(objectPlaceNm, RoundKey));
			responseBody.setObject_relation_user(objectRelationUser);
			responseBody.setObject_relation_user_home_tel(objectRelationUserHomeTel);
			responseBody.setObject_relation_user_mobil_tel(objectRelationUserMobilTel);
			responseBody.setObject_location(base64.encrypt(objectLocation, RoundKey));
			responseBody.setObject_use(base64.encrypt(objectUse, RoundKey));//
			responseBody.setObject_buil_struc(base64.encrypt(objectBuilStruc, RoundKey));
			responseBody.setCheck_period_start(checkPeriodStart);
			responseBody.setCheck_period_end(checkPeriodEnd);
			responseBody.setCheck_user_nm(base64.encrypt(checkUserNm, RoundKey));
			responseBody.setCheck_user_qualfication_class(checkUserQualficationClass);
			responseBody.setCheck_user_qualfication_no(checkUserQualficationNo);
			responseBody.setHead_fire_depart(headFireDepart);
			responseBody.setLocation_lat(locationLat);
			responseBody.setLocation_lon(locationLon);
			
			srchManageMapper.insertRegistBusiness(responseBody);
			
				
			/**
			 * [로그생성]
			 * [argument]
			 * 구분 (NotNull)
			      보고서 번호 
			      업무명            
			      보고서 담당자 아이디        
			      보고서 담당자 이름                   
			      최종 보고서 업로드 시간
			      보고서 처리구분
			      단말기 접속경로
			      단말기위치 경도
			      단말기위치 위도
			      처리자 이름(NotNull)
			      처리자 아이디(NotNull)
			 */
			adminInsertRegistBusinessLog("3",reportNo,businessNm,userId,checkUserNm,null,"1", null, 
					null, null, adminUserNm, adminUserId);
				
			
		}catch(Exception e){
			e.getStackTrace();
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	/**
	 * 조사업무 수정(관리자용)
	 * @param reportNo
	 * @param userId
	 * @param businessNm
	 * @param objectPlaceNm
	 * @param objectRelationUser
	 * @param objectRelationUserHomeTel
	 * @param objectRelationUserMobilTel
	 * @param objectLocation
	 * @param objectUse
	 * @param objectBuilStruc
	 * @param checkPeriodStart
	 * @param checkPeriodEnd
	 * @param checkUserNm
	 * @param checkUserQualficationClass
	 * @param checkUserQualficationNo
	 * @param headFireDepart
	 * @param locationLat
	 * @param locationLon
	 * @param adminUserId
	 * @param adminUserNm
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "조사업무 수정(관리자용)", notes = "관리자가 조사업무 수정을 한다.(로그를 남길려면 관리자 로그인 후 진행해야한다.)")
	@ApiImplicitParams({
	    @ApiImplicitParam(name = "reportNo",                     value = "보고서 번호",                	required = true,   dataType = "string", paramType = "query"),
	    @ApiImplicitParam(name = "userId",                       value = "사용자 아이디",               	required = false,  dataType = "string", paramType = "query"),
	    @ApiImplicitParam(name = "businessNm",                   value = "업무명",                   	required = false,  dataType = "string", paramType = "query"),	    
	    @ApiImplicitParam(name = "objectPlaceNm",                value = "명칭",                     	required = false,  dataType = "string", paramType = "query"),
	    @ApiImplicitParam(name = "objectRelationUser",           value = "관계인",                   	required = false,  dataType = "string", paramType = "query"),
	    @ApiImplicitParam(name = "objectRelationUserHomeTel",    value = "관계인 전화('-'하이픈입력)",    	required = false,  dataType = "string", paramType = "query"),
	    @ApiImplicitParam(name = "objectRelationUserMobilTel",   value = "관계인 휴대전화('-'하이픈입력) ", required = false,  dataType = "string", paramType = "query"),
	    @ApiImplicitParam(name = "objectLocation",               value = "소재지",                   	required = false,  dataType = "string", paramType = "query"),
	    @ApiImplicitParam(name = "objectUse",                    value = "용도",                     	required = false,  dataType = "string", paramType = "query"),
	    @ApiImplicitParam(name = "objectBuilStruc",              value = "건물구조",                  	required = false,  dataType = "string", paramType = "query"),
	    @ApiImplicitParam(name = "checkPeriodStart",             value = "점검기간 시작일(ex 20161212)",	required = false,  dataType = "string", paramType = "query"),
	    @ApiImplicitParam(name = "checkPeriodEnd",               value = "점검기간 종료일(ex 20161212)",	required = false,  dataType = "string", paramType = "query"),
	    @ApiImplicitParam(name = "checkUserNm",                  value = "점검자 성명",                	required = false,  dataType = "string", paramType = "query"),
	    @ApiImplicitParam(name = "checkUserQualficationClass",   value = "자격구분",                  	required = false,  dataType = "string", paramType = "query"),
	    @ApiImplicitParam(name = "checkUserQualficationNo",      value = "자격번호('-'하이픈입력)",      	required = false,  dataType = "string", paramType = "query"),
	    @ApiImplicitParam(name = "headFireDepart",               value = "소방서장",                  	required = false,  dataType = "string", paramType = "query"),
	    @ApiImplicitParam(name = "locationLat",                  value = "소재지 위도",                	required = false,  dataType = "string", paramType = "query"),
	    @ApiImplicitParam(name = "locationLon",                  value = "소재지 경도",               	required = false,  dataType = "string", paramType = "query"),
	    @ApiImplicitParam(name = "adminUserId",                  value = "관리자 아이디",		        required = true,   dataType = "string", paramType = "query"),
	    @ApiImplicitParam(name = "adminUserNm",                  value = "관리자 이름",		            required = true,   dataType = "string", paramType = "query")
	  })	
	@RequestMapping(value = "/Byod/adminUpdateBusiness", method = RequestMethod.PUT)	
	public ResponseEntity<String> adminUpdateBusiness(@RequestParam("reportNo") String reportNo,
			@RequestParam(value="userId", required=false) String userId,   
			@RequestParam(value="businessNm", required=false) String businessNm,
			@RequestParam(value="objectPlaceNm", required=false) String objectPlaceNm, 
			@RequestParam(value="objectRelationUser", required=false) String objectRelationUser,
			@RequestParam(value="objectRelationUserHomeTel", required=false) String objectRelationUserHomeTel,
			@RequestParam(value="objectRelationUserMobilTel", required=false) String objectRelationUserMobilTel,
			@RequestParam(value="objectLocation", required=false) String objectLocation,
			@RequestParam(value="objectUse", required=false) String objectUse,
			@RequestParam(value="objectBuilStruc", required=false) String objectBuilStruc,
			@RequestParam(value="checkPeriodStart", required=false) String checkPeriodStart,
			@RequestParam(value="checkPeriodEnd", required=false) String checkPeriodEnd,
			@RequestParam(value="checkUserNm", required=false) String checkUserNm,
			@RequestParam(value="checkUserQualficationClass", required=false) String checkUserQualficationClass,
			@RequestParam(value="checkUserQualficationNo", required=false) String checkUserQualficationNo,
			@RequestParam(value="headFireDepart", required=false) String headFireDepart,
			@RequestParam(value="locationLat", required=false) String locationLat, 
			@RequestParam(value="locationLon", required=false) String locationLon,
			@RequestParam("adminUserId") String adminUserId,
			@RequestParam("adminUserNm") String adminUserNm) throws Exception {
		
		SrchManageVO responseBody     = new SrchManageVO();
		
		try{
			
			if(ByodApiUtil.isEmpty(adminUserId)){
				log.info("[ByodApiUtil.isEmpty(adminUserId)] : null");
				return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			}
			
			responseBody.setReport_no(reportNo);
			responseBody.setUser_id(userId);
			responseBody.setBusiness_nm(base64.encrypt(businessNm, RoundKey));
			responseBody.setObject_place_nm(base64.encrypt(objectPlaceNm, RoundKey));
			responseBody.setObject_relation_user(objectRelationUser);
			responseBody.setObject_relation_user_home_tel(objectRelationUserHomeTel);
			responseBody.setObject_relation_user_mobil_tel(objectRelationUserMobilTel);
			responseBody.setObject_location(base64.encrypt(objectLocation, RoundKey));
			responseBody.setObject_use(base64.encrypt(objectUse, RoundKey));
			responseBody.setObject_buil_struc(base64.encrypt(objectBuilStruc, RoundKey));
			responseBody.setCheck_period_start(checkPeriodStart);
			responseBody.setCheck_period_end(checkPeriodEnd);
			responseBody.setCheck_user_nm(base64.encrypt(checkUserNm, RoundKey));
			responseBody.setCheck_user_qualfication_class(checkUserQualficationClass);
			responseBody.setCheck_user_qualfication_no(checkUserQualficationNo);
			responseBody.setHead_fire_depart(headFireDepart);
			responseBody.setLocation_lat(locationLat);
			responseBody.setLocation_lon(locationLon);
			
			srchManageMapper.updateBusiness(responseBody);
				
			/**
			 * [로그생성]
			 * [argument]
			 * 구분 (NotNull)
			      보고서 번호 
			      업무명            
			      보고서 담당자 아이디        
			      보고서 담당자 이름                   
			      최종 보고서 업로드 시간
			      보고서 처리구분
			      단말기 접속경로
			      단말기위치 경도
			      단말기위치 위도
			      처리자 이름(NotNull)
			      처리자 아이디(NotNull)
			 */
			adminInsertRegistBusinessLog("3",reportNo,businessNm,userId,checkUserNm,null,"2", null, 
					null, null, adminUserNm, adminUserId);				
			
		}catch(Exception e){
			e.getStackTrace();
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	/**
	 * 조사업무 삭제(공통 또는 관리자용)
	 * @param reportNo
	 * @param adminUserId
	 * @param adminUserNm
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "조사업무 삭제(공통 또는 관리자용)", notes = "관리자 또는 조사자가 조사 업무를 삭제한다.(로그를 남길려면 로그인 후 진행해야한다.)")
	@ApiImplicitParams({
	    @ApiImplicitParam(name = "reportNo",    value = "보고서 번호", 	 required = true,  dataType = "string", paramType = "query"),
	    @ApiImplicitParam(name = "adminUserId", value = "관리자 아이디",	 required = true,  dataType = "string", paramType = "query"),
	    @ApiImplicitParam(name = "adminUserNm", value = "관리자 이름",	 required = true,  dataType = "string", paramType = "query")
	  })
	@ApiResponses(value = {@ApiResponse(code = 400, message = "Bad Request")})
	@RequestMapping(value = "/Byod/adminSrchInfoDelete", method = RequestMethod.PUT)
	public ResponseEntity<String> adminSrchInfoDelete(@RequestParam("reportNo") String reportNo,
													  @RequestParam("adminUserId") String adminUserId,
													  @RequestParam("adminUserNm") String adminUserNm) throws Exception {

		SrchManageVO srchVO   = new SrchManageVO();
		SrchManageVO reSrchVO = null;
		
		if(ByodApiUtil.isEmpty(adminUserId)){
			log.info("[ByodApiUtil.isEmpty(adminUserId)] : null");
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
		
		srchVO.setReport_no(reportNo);
		
		reSrchVO = (SrchManageVO) srchManageMapper.selectSrchDetailInfo(srchVO);
		
		reSrchVO.setBusiness_nm(base64.decrypt(reSrchVO.getBusiness_nm(), RoundKey));
		reSrchVO.setCheck_user_nm(base64.decrypt(reSrchVO.getBusiness_nm(), RoundKey));
		
		srchManageMapper.deleteSrchInfoList(srchVO);
		
		
		/**
		 * [로그생성]
		 * [argument]
		 * 구분 (NotNull)
		      보고서 번호 
		      업무명            
		      보고서 담당자 아이디        
		      보고서 담당자 이름                   
		      최종 보고서 업로드 시간
		      보고서 처리구분
		      단말기 접속경로
		      단말기위치 경도
		      단말기위치 위도
		      처리자 이름(NotNull)
		      처리자 아이디(NotNull)
		 */
		adminInsertRegistBusinessLog("3",reSrchVO.getReport_no(),reSrchVO.getBusiness_nm(),reSrchVO.getUser_id(),
				reSrchVO.getCheck_user_nm(),null,"3", null,null, null, adminUserNm, adminUserId);
		
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	/**
	 * 보고서(조사업무) 업로드 (조사자용)
	 * @param reportNo
	 * @param userId
	 * @param networkPath
	 * @param locationLat
	 * @param locationLon
	 * @param sourceFile
	 * @param checkExtgshKind
	 * @param checkExtgshResult
	 * @param checkAlarmEquipKind
	 * @param checkAlarmEquipResult
	 * @param checkExtgshEquipKind
	 * @param checkExtgshEquipResult
	 * @param checkFleeEquipKind
	 * @param checkFleeEquipResult
	 * @param checkEtcEquipKind
	 * @param checkEtcEquipResult
	 * @param specialNote
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "보고서(조사업무) 업로드 (조사자용)", notes = "조사자가 보고서(조사업무)를 업로드 한다.(로그를 남길려면 로그인 후 진행해야한다.)")
	@ApiImplicitParams({
	    @ApiImplicitParam(name = "reportNo",                   value = "보고서 번호",			required = true,   dataType = "string", paramType = "query"),
	    @ApiImplicitParam(name = "userId",                     value = "사용자 아이디",			required = true,   dataType = "string", paramType = "query"),
	    @ApiImplicitParam(name = "networkPath",                value = "단말기 접속경로", 	    required = true,   dataType = "string", paramType = "query"),
	    @ApiImplicitParam(name = "locationLat",                value = "소재지 위도",        	required = true,   dataType = "string", paramType = "query"),
	    @ApiImplicitParam(name = "locationLon",                value = "소재지 경도",         	required = true,   dataType = "string", paramType = "query"),
	    @ApiImplicitParam(name = "sourceFile",                 value = "첨부파일",         	required = false,  dataType = "file",   paramType = "form"),	    
	    @ApiImplicitParam(name = "checkExtgshKind",			   value = "소화기구 종류",       	required = false,  dataType = "string", paramType = "query"),
		@ApiImplicitParam(name = "checkExtgshResult",		   value = "소화기구 점검결과",     	required = false,  dataType = "string", paramType = "query"),
		@ApiImplicitParam(name = "checkAlarmEquipKind",		   value = "경보설비 종류",       	required = false,  dataType = "string", paramType = "query"),
		@ApiImplicitParam(name = "checkAlarmEquipResult",	   value = "경보설비 점검결과",     	required = false,  dataType = "string", paramType = "query"),
		@ApiImplicitParam(name = "checkExtgshEquipKind",	   value = "소화설비 종류",       	required = false,  dataType = "string", paramType = "query"),
		@ApiImplicitParam(name = "checkExtgshEquipResult",	   value = "소화설비 점검결과",     	required = false,  dataType = "string", paramType = "query"),
		@ApiImplicitParam(name = "checkFleeEquipKind",		   value = "피난설비 종류",        	required = false,  dataType = "string", paramType = "query"),
		@ApiImplicitParam(name = "checkFleeEquipResult",	   value = "피난설비 점검결과",    	required = false,  dataType = "string", paramType = "query"),
		@ApiImplicitParam(name = "checkEtcEquipKind",		   value = "기타설비 종류",       	required = false,  dataType = "string", paramType = "query"),
		@ApiImplicitParam(name = "checkEtcEquipResult",		   value = "기타설비 점검결과",      	required = false,  dataType = "string", paramType = "query"),
		@ApiImplicitParam(name = "specialNote",		   		   value = "특기 사항 ",      		required = false,  dataType = "string", paramType = "query")
		
	  })	
	@ApiResponses(value = {@ApiResponse(code = 409, message = "Conflict(해당 보고서가 이미 존재)")})
	@RequestMapping(value = "/Byod/srchUserReportUpload", method = RequestMethod.POST,  consumes = {"multipart/form-data","application/x-www-form-urlencoded"})	
	public ResponseEntity<String> srchUserReportUpload(@RequestParam("reportNo") String reportNo,
			@RequestParam("userId") String userId,   
			@RequestParam("networkPath") String networkPath,
			@RequestParam("locationLat") String locationLat, 
			@RequestParam("locationLon") String locationLon,
			@RequestPart(value="sourceFile", required=false) MultipartFile[] sourceFile,
			
			@RequestParam(value="checkExtgshKind", required=false) String checkExtgshKind,
			@RequestParam(value="checkExtgshResult", required=false) String checkExtgshResult,
			@RequestParam(value="checkAlarmEquipKind", required=false) String checkAlarmEquipKind,
			@RequestParam(value="checkAlarmEquipResult", required=false) String checkAlarmEquipResult,
			@RequestParam(value="checkExtgshEquipKind", required=false) String checkExtgshEquipKind,
			@RequestParam(value="checkExtgshEquipResult", required=false) String checkExtgshEquipResult,
			@RequestParam(value="checkFleeEquipKind", required=false) String checkFleeEquipKind,
			@RequestParam(value="checkFleeEquipResult", required=false) String checkFleeEquipResult,
			@RequestParam(value="checkEtcEquipKind", required=false) String checkEtcEquipKind,
			@RequestParam(value="checkEtcEquipResult", required=false) String checkEtcEquipResult,
			@RequestParam(value="specialNote", required=false) String specialNote,			
			HttpServletRequest request) throws Exception {
		
		SrchManageVO responseBody  = new SrchManageVO();
		int cnt                    = 0;
		
		try{
			
			responseBody.setReport_no(reportNo);
			responseBody.setUser_id(userId);
			//responseBody.setLocation_lat(locationLat);
			//responseBody.setLocation_lon(locationLon);
			responseBody.setCheck_extgsh_kind(checkExtgshKind);
			responseBody.setCheck_extgsh_result(base64.encrypt(checkExtgshResult, RoundKey));
			responseBody.setCheck_alarm_equip_kind(checkAlarmEquipKind);
			responseBody.setCheck_alarm_equip_result(base64.encrypt(checkAlarmEquipResult, RoundKey));
			responseBody.setCheck_extgsh_equip_kind(checkExtgshEquipKind);
			responseBody.setCheck_extgsh_equip_result(base64.encrypt(checkExtgshEquipResult, RoundKey));
			responseBody.setCheck_flee_equip_kind(checkFleeEquipKind);
			responseBody.setCheck_flee_equip_result(base64.encrypt(checkFleeEquipResult, RoundKey));
			responseBody.setCheck_etc_equip_kind(checkEtcEquipKind);
			responseBody.setCheck_etc_equip_result(base64.encrypt(checkEtcEquipResult, RoundKey));
			
			responseBody.setSpecial_note(specialNote);
			
			//update (보고서 생성은 관리자, 업로드는 조사자)
			srchManageMapper.updateReportUpload(responseBody);
			
			log.info("[updateReportUpload method - update]: success!!");
			
			cnt = attchManageMapper.selectReportAndFileCnt(reportNo);
			
			if(sourceFile.length > 0){
				
				if(cnt + sourceFile.length > 2){
					//첨부파일 갯수 2개로 제한
					log.info("[selectReportAndFileCnt method] : Attachment file Exceeded!!!");
					return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
				}else{
					//insert 첨부파일
					if(!uploadAttachment(reportNo, userId, sourceFile)){
						return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);						
					}
				}
			}
			 
			//로그생성
			UserManageVO UserVO = (UserManageVO)request.getSession().getAttribute("userVO");
			
			//테스트관련 Exception 대신 조건문으로 [임시 블록처러]
			if(UserVO != null){
				
				SrchManageVO logSrch = (SrchManageVO)srchManageMapper.selectSrchDetailInfo(responseBody);
				
				/**
				 * [로그생성]
				 * [argument]
				 * 구분 (NotNull)
				      보고서 번호 
				      업무명            
				      보고서 담당자 아이디        
				      보고서 담당자 이름                   
				      최종 보고서 업로드 시간
				      보고서 처리구분
				      단말기 접속경로
				      단말기위치 경도
				      단말기위치 위도
				      처리자 이름(NotNull)
				      처리자 아이디(NotNull)
				 */
				adminInsertRegistBusinessLog("3",logSrch.getReport_no(),logSrch.getBusiness_nm(),userId,
						logSrch.getCheck_user_nm(),null,"4", networkPath, 
						locationLon, locationLat, UserVO.getUser_nm(),UserVO.getUser_id());
				
			}
			
		}catch(Exception e){
			e.getStackTrace();
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
		
		return new ResponseEntity<String>(HttpStatus.OK);
	}
	
	/**
	 * 첨부파일 업로드
	 * @param reportNo
	 * @param userId
	 * @param sourceFile
	 * @throws Exception
	 */
	public boolean uploadAttachment(String reportNo, String userId, MultipartFile[] sourceFile) throws Exception {

		AttchFileManageVO attchVO = new AttchFileManageVO();
		String tmpResultID        = "";
		String extensionGb        = "";
		
		try{
				
			String sourceFileName          = "";
			String sourceFileNameExtension = "";

			//mp3,jpg,png,mp4
			for(int i=0; i<sourceFile.length; i++){
				
				sourceFileName = sourceFile[i].getOriginalFilename();
				sourceFileNameExtension = FilenameUtils.getExtension(sourceFileName).toLowerCase();
				
				if(!ByodApiUtil.isValidFileExtension(sourceFileNameExtension)){
					return false;
				}
				
				extensionGb = ByodApiUtil.isValidFileExtensionGb(sourceFileNameExtension);
				
				File destinationFile;
				String destinationFileName;
				
				do {
					destinationFileName = RandomStringUtils.randomAlphanumeric(32) + "." + sourceFileNameExtension;
					destinationFile = new File(fileStorePath + destinationFileName);

				} while (destinationFile.exists());
				destinationFile.getParentFile().mkdirs();
				sourceFile[i].transferTo(destinationFile);
				
				//첨부파일 아이디 조회
				tmpResultID = attchManageMapper.selectMaxFileId();
				
				attchVO.setFile_id(ByodApiUtil.numberParsing("4", tmpResultID));
				attchVO.setReport_no(reportNo);
				attchVO.setUser_id(userId);
				attchVO.setAttch_file_kind(extensionGb);
				attchVO.setAttch_file_path(base64.encrypt(fileStorePath, RoundKey));
				attchVO.setAttch_file_nm(destinationFileName);
				attchVO.setOri_file_nm(base64.encrypt(sourceFile[i].getOriginalFilename(), RoundKey));
				attchVO.setAttch_file_extsn(sourceFileNameExtension);
				
				//첨부파일 정보 테이블에 insert
				attchManageMapper.insertAttchFile(attchVO);
				log.info("[uploadAttachment method - sourceFile "+sourceFile[i].getOriginalFilename() +"]: success!!");
			}
				
		}catch(Exception e){
			e.getStackTrace();
			log.info("[uploadAttachment ]: fail!!!!!");
			return false;
		}
		
		return true;
	}
	
	/**
	 * 완료된 조사업무 목록 조회(조사자용)
	 * @param reportNo
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "완료된 조사업무 목록 조회(조사자용)", notes = "조사자가 자신의 완료된 조사 업무  리스트를 조회한다.", response = SrchManageVO.class)
	@ApiImplicitParams({
	    @ApiImplicitParam(name = "userId",   value = "사용자 아이디",  required = true, dataType = "string", paramType = "query")
	  })
	@ApiResponses(value = {@ApiResponse(code = 400, message = "Bad Request")})
	@RequestMapping(value = "/Byod/srchFinishInfoFList", method = RequestMethod.GET)
	public ResponseEntity<Object> srchFinishInfoFList(@RequestParam String userId) throws Exception {

		SrchManageVO srchVO             = new SrchManageVO();
		List<SrchManageVO> responseBody = null;
		srchVO.setUser_id(userId);
			
		responseBody = srchManageMapper.selectSrchFinishInfoList(srchVO);

		for(int i=0; i<responseBody.size(); i++){
			responseBody.get(i).setBusiness_nm(base64.decrypt(responseBody.get(i).getBusiness_nm(), RoundKey));
		}
		
		return new ResponseEntity<Object>(responseBody, HttpStatus.OK);
	}
	
	/**
	 * 조사업무 상세 조회(공통)
	 * @param reportNo
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "조사업무 상세 조회(공통)", notes = "조사업무를  상세 조회한다.", response = SrchManageVO.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "reportNo",  value = "보고서 번호",  required = true,   dataType = "string", paramType = "query")
	  })
	@ApiResponses(value = {@ApiResponse(code = 400, message = "Bad Request")})
	@RequestMapping(value = "/Byod/srchFinishDetailInfo", method = RequestMethod.GET)
	public ResponseEntity<Object> srchFinishDetailInfo(@RequestParam String reportNo) throws Exception {

		SrchManageVO srchVO       = new SrchManageVO();
		SrchManageVO responseBody = null;
		int cnt = 0;
		
		srchVO.setReport_no(reportNo);
		
		//첨부파일 테이블 조회(해당 보고서에 첨부된 파일 갯수 조회)
		cnt = attchManageMapper.selectReportAndFileCnt(reportNo);
		
		srchVO.setAttch_file_cnt(cnt);
		
		responseBody = (SrchManageVO)srchManageMapper.selectSrchFinishDetailInfo(srchVO);
		
		//복호화
		responseBody.setBusiness_nm(base64.decrypt(responseBody.getBusiness_nm(), RoundKey));
		responseBody.setObject_place_nm(base64.decrypt(responseBody.getObject_place_nm(), RoundKey));
		responseBody.setObject_location(base64.decrypt(responseBody.getObject_location(), RoundKey));
		responseBody.setObject_use(base64.decrypt(responseBody.getObject_use(), RoundKey));
		responseBody.setObject_buil_struc(base64.decrypt(responseBody.getObject_buil_struc(), RoundKey));
		responseBody.setCheck_extgsh_result(base64.decrypt(responseBody.getCheck_extgsh_result(), RoundKey));
		responseBody.setCheck_alarm_equip_result(base64.decrypt(responseBody.getCheck_alarm_equip_result(), RoundKey));
		responseBody.setCheck_extgsh_equip_result(base64.decrypt(responseBody.getCheck_extgsh_equip_result(), RoundKey));
		responseBody.setCheck_flee_equip_result(base64.decrypt(responseBody.getCheck_flee_equip_result(), RoundKey));
		responseBody.setCheck_etc_equip_result(base64.decrypt(responseBody.getCheck_etc_equip_result(), RoundKey));
		responseBody.setCheck_user_nm(base64.decrypt(responseBody.getCheck_user_nm(), RoundKey));
		
		if(cnt > 0){
			responseBody.setAttch_file_cnt(cnt);
		}
		//첨부된 이미지 체크(base64 data로 세팅)
		if(!ByodApiUtil.isEmpty(responseBody.getImg_attch_file_nm())){
			responseBody.setImg_attch_file_url("data:image/jpeg;base64,"+ ByodApiUtil.encodeBase64Convert(fileStorePath + responseBody.getImg_attch_file_nm()));			
		}
		
		//첨부된 오디오 체크(base64 data로 세팅)
		if(!ByodApiUtil.isEmpty(responseBody.getAudio_attch_file_nm())){			
			responseBody.setAudio_attch_file_url("data:audio/mp3;base64," + ByodApiUtil.encodeBase64Convert(fileStorePath + responseBody.getAudio_attch_file_nm()));
		}
		
		return new ResponseEntity<Object>(responseBody, HttpStatus.OK);
				
	}
	
	/**
	 * 모바일 전용 조사업무 상세 조회(조사자)
	 * @param reportNo
	 * @param userId
	 * @param userPw
	 * @return
	 * @throws Exception
	 */
	@ApiOperation(value = "모바일 전용 조사업무 상세 조회(조사자)", notes = "조사업무를  상세 조회한다.", response = SrchManageVO.class)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "reportNo",  value = "보고서 번호",  	required = true,   dataType = "string", paramType = "query"),
		@ApiImplicitParam(name = "userId",    value = "사용자 아이디", 	required = true,   dataType = "string", paramType = "query"),
		@ApiImplicitParam(name = "userPw",    value = "사용자 비밀번호",	required = true,   dataType = "string", paramType = "query")
	  })
	@ApiResponses(value = {@ApiResponse(code = 400, message = "Bad Request")})
	@RequestMapping(value = "/Byod/srchMobileFinishDetailInfo", method = RequestMethod.GET)
	public ResponseEntity<Object> srchMobileFinishDetailInfo(@RequestParam String reportNo,
															 @RequestParam("userId") String userId,
															 @RequestParam("userPw") String userPw) throws Exception {

		SrchManageVO srchVO             = new SrchManageVO();
		UserManageVO userVO             = new UserManageVO();
		UserPwAlgorithm userPwAlgorithm = new UserPwAlgorithm();
		SrchManageVO responseBody = null;
		int cnt = 0;
		
		userVO.setUser_id(userId);
		//비밀번호 암호화(단방향 SHA-256)
		userVO.setUser_pw(userPwAlgorithm.UserPwAlgorithm(userPw));
		
		try{
			UserManageVO response  = userManageMapper.selectUserInfo(userVO);
			if(ByodApiUtil.isEmpty(response)){
				return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
			}
		}catch(Exception e){
			e.getStackTrace();			
		}
		
		
		srchVO.setReport_no(reportNo);
		
		//첨부파일 테이블 조회(해당 보고서에 첨부된 파일 갯수 조회)
		cnt = attchManageMapper.selectReportAndFileCnt(reportNo);
		
		srchVO.setAttch_file_cnt(cnt);
		
		responseBody = (SrchManageVO)srchManageMapper.selectSrchFinishDetailInfo(srchVO);
		
		//복호화
		responseBody.setBusiness_nm(base64.decrypt(responseBody.getBusiness_nm(), RoundKey));
		responseBody.setObject_place_nm(base64.decrypt(responseBody.getObject_place_nm(), RoundKey));
		responseBody.setObject_location(base64.decrypt(responseBody.getObject_location(), RoundKey));
		responseBody.setObject_use(base64.decrypt(responseBody.getObject_use(), RoundKey));
		responseBody.setObject_buil_struc(base64.decrypt(responseBody.getObject_buil_struc(), RoundKey));
		responseBody.setCheck_extgsh_result(base64.decrypt(responseBody.getCheck_extgsh_result(), RoundKey));
		responseBody.setCheck_alarm_equip_result(base64.decrypt(responseBody.getCheck_alarm_equip_result(), RoundKey));
		responseBody.setCheck_extgsh_equip_result(base64.decrypt(responseBody.getCheck_extgsh_equip_result(), RoundKey));
		responseBody.setCheck_flee_equip_result(base64.decrypt(responseBody.getCheck_flee_equip_result(), RoundKey));
		responseBody.setCheck_etc_equip_result(base64.decrypt(responseBody.getCheck_etc_equip_result(), RoundKey));
		responseBody.setCheck_user_nm(base64.decrypt(responseBody.getCheck_user_nm(), RoundKey));
		
		if(cnt > 0){
			responseBody.setAttch_file_cnt(cnt);
		}
		//첨부된 이미지 체크(base64 data로 세팅)
		if(!ByodApiUtil.isEmpty(responseBody.getImg_attch_file_nm())){
			responseBody.setImg_attch_file_url("data:image/jpeg;base64,"+ ByodApiUtil.encodeBase64Convert(fileStorePath + responseBody.getImg_attch_file_nm()));			
		}
		
		//첨부된 오디오 체크(base64 data로 세팅)
		if(!ByodApiUtil.isEmpty(responseBody.getAudio_attch_file_nm())){			
			responseBody.setAudio_attch_file_url("data:audio/mp3;base64," + ByodApiUtil.encodeBase64Convert(fileStorePath + responseBody.getAudio_attch_file_nm()));
		}
		
		return new ResponseEntity<Object>(responseBody, HttpStatus.OK);
				
	}	
	
}
