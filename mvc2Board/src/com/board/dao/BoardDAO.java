package com.board.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.tomcat.jdbc.pool.DataSource;

import com.board.bean.BoardBean;

public class BoardDAO {

	private Connection con;
	private PreparedStatement pstmt;
	private ResultSet rs;

	public void getConnection() {
		try {

			Context ctx = new InitialContext();
			Context env = (Context) ctx.lookup("java:comp/env");
			DataSource ds = (DataSource) env.lookup("jdbc/pool");
			con = ds.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int getAllListCount() {
		getConnection();
		int count = 0;
		try {
			String sql = "SELECT COUNT(*) FROM BOARD";
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				count = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return count;
	}

	public List<BoardBean> getAllBoard(int startRow, int endRow) {
		List<BoardBean> list = new ArrayList<BoardBean>();
		getConnection();
		try {
			String sql = "SELECT * FROM (SELECT Row_Number() OVER (ORDER BY REF DESC, RE_STEP ASC) AS rownum, *FROM BOARD) T1 WHERE rownum BETWEEN ? AND ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, startRow);
			pstmt.setInt(2, endRow);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				BoardBean bean = new BoardBean();
				bean.setNum(rs.getInt(2));
				bean.setWriter(rs.getString(3));
				bean.setEmail(rs.getString(4));
				bean.setSubject(rs.getString(5));
				bean.setPassword(rs.getString(6));
				bean.setRegDate(rs.getDate(7).toString());
				bean.setRef(rs.getInt(8));
				bean.setReStep(rs.getInt(9));
				bean.setReLevel(rs.getInt(10));
				bean.setReadCount(rs.getInt(11));
				bean.setContent(rs.getString(12));
				list.add(bean);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	public void insertBoard(BoardBean bean) {
		getConnection();
		int ref = 0;
		int re_step = 1;
		int re_level = 1;
		try {
			String sql = "SELET MAX(REF) FROM BOARD";
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				ref = rs.getInt(1) + 1; // 가장 큰 값에 1 더함
			}
			String insertSql = "insert into board values(?,?,?,?,?,getdate(),?,?,?,0,?)";
			pstmt = con.prepareStatement(insertSql);
			// ?에 값 매핑
			pstmt.setString(1, bean.getWriter());
			pstmt.setString(2, bean.getEmail());
			pstmt.setString(3, bean.getSubject());
			pstmt.setString(4, bean.getPassword());
			pstmt.setInt(5, ref);
			pstmt.setInt(6, re_step);
			pstmt.setInt(7, re_level);
			pstmt.setString(8, bean.getContent());
			pstmt.executeUpdate();
			con.close();
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public BoardBean getBoardInfo(int num) {
		getConnection();
		BoardBean bean = new BoardBean();
		try {
			plusReadCount(num);
			String sql = "SELECT * FROM BOARD WHERE num=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, num);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				bean.setNum(rs.getInt(2));
				bean.setWriter(rs.getString(3));
				bean.setEmail(rs.getString(4));
				bean.setSubject(rs.getString(5));
				bean.setPassword(rs.getString(6));
				bean.setRegDate(rs.getDate(7).toString());
				bean.setRef(rs.getInt(8));
				bean.setReStep(rs.getInt(9));
				bean.setReLevel(rs.getInt(10));
				bean.setReadCount(rs.getInt(11));
				bean.setContent(rs.getString(12));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return bean;
	}

	public void plusReadCount(int num) {
		getConnection();
		try {
			String sql = "UPDATE BOARD SET readCount = readCount+1 WHRER num=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, num);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
