<!DOCTYPE html>
<html>
<head>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
<title>Gluck Connect 4 App (by Jamie Guthrie)</title>
<meta>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
</meta>
</head>

<body>
	<script>
		var source = new EventSource('/game/updates/${gameid}');
	 	source.addEventListener('open', function(e) {
	 		window.location.replace(window.location.pathname);
	 	}, false);
	 	source.onmessage = function(e) {
	 		window.location.replace(window.location.pathname);
	 	};
 	</script>
 
	<c:out value="Connect 4 by Jamie Guthrie"></c:out>
	<br /><br />
	<div id="content"></div>
	<table border="1">
		<tr>
			<c:forEach begin="0" end="${game.getBoard().getNumCols()-1}" varStatus="cols">
				<td><a href="/game/${gameid}/?move=${cols.index}">&#11015;</td>
			</c:forEach>
		</tr>
		<c:forEach begin="0" end="${game.getBoard().getNumRows()-1}" varStatus="rows">
			<tr>
				<c:forEach begin="0" end="${game.getBoard().getNumCols()-1}" varStatus="cols">
					<td bgcolor="${game.getBoard().getCell((game.getBoard().getNumRows()-1) - rows.index, cols.index).getColor()}">&nbsp;&nbsp;</td>
				</c:forEach>
			</tr>
		</c:forEach>
	</table>
	<br />
	<c:choose>
	    <c:when test="${game.getWinner() != null}">
	        Player <span style="background:${game.getWinner()}">&nbsp;&nbsp;</span> wins!!
	    </c:when>    
	    <c:otherwise>
	        Next player is: <span style="background:${game.getNextColor()}">&nbsp;&nbsp;</span>
	    </c:otherwise>
	</c:choose>
	
	
</body>
</html>