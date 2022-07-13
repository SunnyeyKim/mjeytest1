package com.tech.sprj09.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.tech.sprj09.dto.BoardDto;

public class BoardDao {
	DataSource dataSource;
	
	public BoardDao() {
		// 객체호출하면 db접속
		try {
			Context context=new InitialContext();
			dataSource=(DataSource) context.lookup("java:comp/env/jdbc/springxe");
			System.out.println("dataSource 생성성공");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ArrayList<BoardDto> list() {
		//db에 실제접속 데이터 가져오기
		Connection con=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		ArrayList<BoardDto> dtos=new ArrayList<BoardDto>();
		try {
			//db에접속-결과를 dtos에 담고 최종 dtos를 return
			con=dataSource.getConnection();//db연결
			String sql="select bid,bname,btitle,bcontent,bdate,"
					 + "bhit,bgroup,bstep,bindent from replyboard"
					 + " order by bgroup desc, bstep asc";
			pstmt=con.prepareStatement(sql);
			rs=pstmt.executeQuery();
			while(rs.next()) {
				//rs에서 각필드값을 하나씩 가져오기
				int bid=rs.getInt("bid");
				String bname=rs.getString("bname");
				String btitle=rs.getString("btitle");
				String bcontent=rs.getString("bcontent");
				Timestamp bdate=rs.getTimestamp("bdate");
				
				int bhit=rs.getInt("bhit");
				int bgroup=rs.getInt("bgroup");
				int bstep=rs.getInt("bstep");
				int bindent=rs.getInt("bindent");
				
				//BoardDto에 담기(생성자을 활용)
				BoardDto dto=new BoardDto(bid, bname,
						btitle, bcontent, bdate, bhit,
						bgroup, bstep, bindent);
				//리스트에 담기
				dtos.add(dto);	
			}		
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(rs!=null) rs.close();
				if(pstmt!=null) pstmt.close();
				if(con!=null) con.close();
				
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
		
		return dtos;
	}
	public void write(String bname,String btitle, String bcontent) {
		//db에 연결해서 저장
		
		Connection con=null;
		PreparedStatement pstmt=null;
		try {
			con=dataSource.getConnection();//db연결
			String sql="insert into replyboard values(replyboard_seq.nextval,"
					+ "?,?,?,sysdate,0,"
					+ "replyboard_seq.currval,0,0)";
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1, bname);
			pstmt.setString(2, btitle);
			pstmt.setString(3, bcontent);
			int rn=pstmt.executeUpdate();
			System.out.println("rn : >>>>>>>>>"+rn);
					
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {		
				if(pstmt!=null) pstmt.close();
				if(con!=null) con.close();	
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}	
	}
	public void upHit(String sbid) {
		//bid를 조건으로 db에서 글을 update
		Connection con=null;
		PreparedStatement pstmt=null;
		try {
			con=dataSource.getConnection();//db연결
			
			String sql="update replyboard "
					+ " set bhit=bhit+1"
					+ " where bid=?";
			pstmt=con.prepareStatement(sql);
			pstmt.setInt(1, Integer.parseInt(sbid));
			
			int rn=pstmt.executeUpdate();//update실행
			System.out.println("update rn : "+rn);
		} catch (Exception e) {
			// TODO: handle exception
		}finally {
			try {		
				if(pstmt!=null) pstmt.close();
				if(con!=null) con.close();	
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
	}
	
	public BoardDto contentView(String sbid) {
		//조회수 증가
		upHit(sbid);
		
		//bid를 조건으로 db에서 글을 select
		BoardDto dto=null;
		Connection con=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;	
		try {
			con=dataSource.getConnection();//db연결
			
			String sql="select bid,bname,btitle,bcontent"
					+ ",bdate,bhit,bgroup,bstep,bindent"
					+ " from replyboard where bid=?";
			pstmt=con.prepareStatement(sql);
			pstmt.setInt(1, Integer.parseInt(sbid));
			rs=pstmt.executeQuery();//실행하고 결과얻음
			//rs내용을 dto에 담기
			rs.next();
			int bid=rs.getInt("bid");
			String bname=rs.getString("bname");
			String btitle=rs.getString("btitle");
			String bcontent=rs.getString("bcontent");
			Timestamp bdate=rs.getTimestamp("bdate");
			int bhit=rs.getInt("bhit");
			int bgroup=rs.getInt("bgroup");
			int bstep=rs.getInt("bstep");
			int bindent=rs.getInt("bindent");
			//생성자를 통해 BoardDto에 담아주기
			dto=new BoardDto(bid, bname, 
					btitle, bcontent, bdate, 
					bhit, bgroup, bstep, bindent);
					
		} catch (Exception e) {
			// TODO: handle exception
		}finally {
			try {	
				if(rs!=null) rs.close();
				if(pstmt!=null) pstmt.close();
				if(con!=null) con.close();	
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}	
		return dto;	
	}
	public void modify(String sbid,String bname,
			String btitle,String bcontent) {
		//bid를 조건으로 db에서 글을 update
		Connection con=null;
		PreparedStatement pstmt=null;
		try {
			con=dataSource.getConnection();//db연결
			
			String sql="update replyboard "
					+ " set bname=?, btitle=?,bcontent=?"
					+ " where bid=?";
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1, bname);
			pstmt.setString(2, btitle);
			pstmt.setString(3, bcontent);
			pstmt.setInt(4, Integer.parseInt(sbid));
			int rn=pstmt.executeUpdate();//update실행
			System.out.println("update rn : "+rn);
		} catch (Exception e) {
			// TODO: handle exception
		}finally {
			try {		
				if(pstmt!=null) pstmt.close();
				if(con!=null) con.close();	
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
	}
	public void delete(String sbid) {
		//bid를 조건으로 db에서 글을 delete
		Connection con=null;
		PreparedStatement pstmt=null;
		try {
			con=dataSource.getConnection();//db연결
			
			String sql="delete from replyboard where bid=?";
			pstmt=con.prepareStatement(sql);
			pstmt.setInt(1, Integer.parseInt(sbid));
			
			int rn=pstmt.executeUpdate();//delete실행
			System.out.println("delete rn : "+rn);
		} catch (Exception e) {
			// TODO: handle exception
		}finally {
			try {		
				if(pstmt!=null) pstmt.close();
				if(con!=null) con.close();	
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
	
	}
	public BoardDto replyView(String sbid) {
		
		//bid를 조건으로 db에서 글을 select
		BoardDto dto=null;
		Connection con=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;	
		try {
			con=dataSource.getConnection();//db연결
			
			String sql="select bid,bname,btitle,bcontent"
					+ ",bdate,bhit,bgroup,bstep,bindent"
					+ " from replyboard where bid=?";
			pstmt=con.prepareStatement(sql);
			pstmt.setInt(1, Integer.parseInt(sbid));
			rs=pstmt.executeQuery();//실행하고 결과얻음
			//rs내용을 dto에 담기
			rs.next();
			int bid=rs.getInt("bid");
			String bname=rs.getString("bname");
			String btitle=rs.getString("btitle");
			String bcontent=rs.getString("bcontent");
			Timestamp bdate=rs.getTimestamp("bdate");
			int bhit=rs.getInt("bhit");
			int bgroup=rs.getInt("bgroup");
			int bstep=rs.getInt("bstep");
			int bindent=rs.getInt("bindent");
			//생성자를 통해 BoardDto에 담아주기
			dto=new BoardDto(bid, bname, 
					btitle, bcontent, bdate, 
					bhit, bgroup, bstep, bindent);
					
		} catch (Exception e) {
			// TODO: handle exception
		}finally {
			try {	
				if(rs!=null) rs.close();
				if(pstmt!=null) pstmt.close();
				if(con!=null) con.close();	
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}	
		return dto;	
	}	
	private void replyShape(String bgroup, String bstep) {
		//댓글들의 중간에 대댓글을 넣을 때 기존의 댓글중 step이
//		원글의 step보다 큰 step은 1씩 증가
		Connection con=null;
		PreparedStatement pstmt=null;
		try {
			con=dataSource.getConnection();//db연결		
			String sql="update replyboard set bstep=bstep+1 "
					+ "where bgroup=? and bstep>?";
			pstmt=con.prepareStatement(sql);
			pstmt.setInt(1, Integer.parseInt(bgroup));
			pstmt.setInt(2, Integer.parseInt(bstep));
			pstmt.executeUpdate();//실행하고 int결과얻음
			
		} catch (Exception e) {
			// TODO: handle exception
		}finally {
			try {	
				
				if(pstmt!=null) pstmt.close();
				if(con!=null) con.close();	
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}	
	}
	public void reply(String bid,String bname,
			String btitle,String bcontent,
			String bgroup,String bstep,
			String bindent) {
		//댓글의 모양 유지(댓글댓글의 순서유지)
		replyShape(bgroup,bstep);
		
		
		//bid의 글에 댓글을 다는 작업 insert
		BoardDto dto=null;
		Connection con=null;
		PreparedStatement pstmt=null;
			
		try {
			con=dataSource.getConnection();//db연결
			
			String sql="insert into replyboard(bid,bname,"
					+ "btitle,bcontent,bgroup,bstep,bindent)" 
					+ "values(replyboard_seq.nextval,?,?,?,?,?,?)";
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1, bname);
			pstmt.setString(2, btitle);
			pstmt.setString(3, bcontent);
			pstmt.setInt(4, Integer.parseInt(bgroup));
			pstmt.setInt(5, Integer.parseInt(bstep)+1);
			pstmt.setInt(6, Integer.parseInt(bindent)+1);
			pstmt.executeUpdate();//실행하고 int결과얻음
			
		} catch (Exception e) {
			// TODO: handle exception
		}finally {
			try {	
				
				if(pstmt!=null) pstmt.close();
				if(con!=null) con.close();	
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}	
		
	}

	
	
	
	
}
