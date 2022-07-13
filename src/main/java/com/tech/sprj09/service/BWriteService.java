package com.tech.sprj09.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;

import com.tech.sprj09.dao.BoardDao;
import com.tech.sprj09.dto.BoardDto;

public class BWriteService implements BServiceInter{

	@Override
	public void execute(Model model) {
		System.out.println(">>>BWriteService");
		//모델에서 request를 풀기
//		맵으로변환
		Map<String, Object> map=model.asMap();
//		맵에서 request를 풀기
		HttpServletRequest request=
				(HttpServletRequest) map.get("request");
		String bname=request.getParameter("bname");
		String btitle=request.getParameter("btitle");
		String bcontent=request.getParameter("bcontent");
		
		BoardDao dao=new BoardDao();//db연결객체가 생성됨
		dao.write(bname,btitle,bcontent);
		
	}

}
