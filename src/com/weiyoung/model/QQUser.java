package com.weiyoung.model;

public class QQUser {
	private String nickname;		//qq�û��ǳ�
	private String gender;			//�Ա�
	private String figureurl_qq_1;	//qqͷ��*40
	private String figureurl_qq_2;	//qqͷ��*100
	private int is_yellow_vidp;		//0���ǣ�1��
	private int vip;				
	private int yellow_vip_level;	//0���ǣ�1��
	private int level;
	private int is_yellow_year_vip;	//0���ǣ�1��
	private int usertype;			//�û����ͣ�usertype=1����ʾqq�û�
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getFigureurl_qq_1() {
		return figureurl_qq_1;
	}
	public void setFigureurl_qq_1(String figureurl_qq_1) {
		this.figureurl_qq_1 = figureurl_qq_1;
	}
	public String getFigureurl_qq_2() {
		return figureurl_qq_2;
	}
	public void setFigureurl_qq_2(String figureurl_qq_2) {
		this.figureurl_qq_2 = figureurl_qq_2;
	}
	public int getIs_yellow_vidp() {
		return is_yellow_vidp;
	}
	public void setIs_yellow_vidp(int is_yellow_vidp) {
		this.is_yellow_vidp = is_yellow_vidp;
	}
	public int getVip() {
		return vip;
	}
	public void setVip(int vip) {
		this.vip = vip;
	}
	public int getYellow_vip_level() {
		return yellow_vip_level;
	}
	public void setYellow_vip_level(int yellow_vip_level) {
		this.yellow_vip_level = yellow_vip_level;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public int getIs_yellow_year_vip() {
		return is_yellow_year_vip;
	}
	public void setIs_yellow_year_vip(int is_yellow_year_vip) {
		this.is_yellow_year_vip = is_yellow_year_vip;
	}
	public int getUsertype() {
		return usertype;
	}
	public void setUsertype(int usertype) {
		this.usertype = usertype;
	}
	
}
