package com.tech.sprj09.dao;

import java.util.ArrayList;

import com.tech.sprj09.dto.BoardDto;

public interface IDao {
	public ArrayList<BoardDto> list(int start, int end, String sk, String selNum);
	public void write(String bname,String btitle, String bcontent, String fname);
	public void upHit(String sbid);
	public BoardDto contentView(String sbid);
	public void modify(String sbid,String bname,
			String btitle,String bcontent);
	public void delete(String sbid);
	public BoardDto replyView(String sbid);
	public void reply(String bid,String bname,
			String btitle,String bcontent,
			String bgroup,String bstep,
			String bindent);
	public void replyShape(String bgroup, String bstep);
	public void download(String path, String fname, String bid);
	
	
//	public int selectBoardTotCount();
	
	public int selectBoardTotCount1(String searchKeyword);
	public int selectBoardTotCount2(String searchKeyword);
	public int selectBoardTotCount3(String searchKeyword);
	public int selectBoardTotCount4(String searchKeyword);
	
}
