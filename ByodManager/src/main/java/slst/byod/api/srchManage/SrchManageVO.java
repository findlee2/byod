package slst.byod.api.srchManage;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonInclude(Include.NON_NULL)
public class SrchManageVO {
	private String report_no;                          //보고서 번호     
	private String user_id;                            //사용자 아이디                 
	private String business_nm;                        //업무명                        
	private String object_place_nm;                    //명칭                          
	private String object_relation_user;               //관계인                        
	private String object_relation_user_home_tel;      //관계인 전화                   
	private String object_relation_user_mobil_tel;     //관계인 휴대전화               
	private String object_location;                    //소재지                        
	private String object_use;                         //용도                          
	private String object_buil_struc;                  //건물구조                      
	private String check_extgsh_kind;                  //소화기구 종류                 
	private String check_extgsh_result;                //소화기구 점검결과             
	private String check_alarm_equip_kind;             //경보설비 종류                 
	private String check_alarm_equip_result;           //경보설비 점검결과             
	private String check_extgsh_equip_kind;            //소화설비 종류                 
	private String check_extgsh_equip_result;          //소화설비 점검결과             
	private String check_flee_equip_kind;              //피난설비 종류                 
	private String check_flee_equip_result;            //피난설비 점검결과             
	private String check_etc_equip_kind;               //기타설비 종류                 
	private String check_etc_equip_result;             //기타설비 점검결과             
	private String check_period_start;                 //점검기간시작일                
	private String check_period_end;                   //점검기간종료일                
	private String special_note;                       //특기 사항                     
	private String check_user_nm;                      //점검자성명                    
	private String check_user_qualfication_class;      //자격구분                      
	private String check_user_qualfication_no;         //자격번호
	private String submit_dt;                          //제출일                        
	private String head_fire_depart;                   //소방서장                      
	private String location_lat;                       //소재지위도                    
	private String location_lon;                       //소재지경도
	private String report_cre_dt;                      //보고서 생성일자(관리자가 조사자에게 분배하기 위해 보고서 생성일자)
	private String report_stats;                       //보고서 진행상태(1:진행중, 2:완료)
	private String img_attch_file_nm;                  //이미지 파일명
	private String audio_attch_file_nm;                //오디오 파일명
	private String img_attch_file_url;                 //이미지 경로
	private String audio_attch_file_url;               //오디오 경로
	private int attch_file_cnt;                        //첨부파일 갯수
	//private int tot_cnt;                               //보고서 총갯수
	
	
}
