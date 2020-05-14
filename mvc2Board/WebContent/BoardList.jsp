<%@ page language="java" contentType="text/html; charset=EUC-KR"
	pageEncoding="EUC-KR"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<body>
	<center>
		<h2>��ü �Խñ� ����</h2>
		<table width="700" border="1">
			<tr height="40">
				<td colspan="5" align="right">
					<button onclick="location.href='BoardWriterForm.jsp'">�۾���</button>
				</td>
			</tr>
			<tr height="40">
				<td align="center" width="50">��ȣ</td>
				<td align="center" width="250">����</td>
				<td align="center" width="100">�ۼ���</td>
				<td align="center" width="150">�ۼ���</td>
				<td align="center" width="50">��ȸ��</td>
			</tr>
			<c:set var="number" value="${number }"></c:set>
			<c:forEach var="bean" items="${list}">

				<tr>
					<td align="center" width="50">${number }</td>
					<td align="left" width="250">
					<c:if test="${bean.reStep>1 }">
							<c:forEach var="j" begin="1" end="${(bean.reStep-1)*5 }">
								&nbsp;
							</c:forEach>
						</c:if>
						<a href="BoardInfoControl.do?num=${bean.num }">${bean.subject }</a>
					</td>
					<td align="left" width="100">${bean.writer }</td>
					<td align="left" width="150">${bean.reg_date }</td>
					<td align="left" width="50">${bean.readCount }</td>
				</tr>
				<c:set var="number" value="${number-1 }"></c:set>
			</c:forEach>
		</table>
	</center>
</body>
</html>