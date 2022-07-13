package com.tech.sprj09.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;

import com.tech.sprj09.dao.BoardDao;
import com.tech.sprj09.dto.BoardDto;

public class BDeleteService implements BServiceInter{

	@Override
	public void execute(Model model) {
		System.out.println(">>>BDeleteService");
		//모델에서 request를 풀기
//		맵으로변환
		Map<String, Object> map=model.asMap();
//		맵에서 request를 풀기
		HttpServletRequest request=
				(HttpServletRequest) map.get("request");
		
		String bid=request.getParameter("bid");
		
		
		BoardDao dao=new BoardDao();//db연결객체가 생성됨
		dao.delete(bid);
		
	}

}
