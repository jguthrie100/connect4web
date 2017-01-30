<!DOCTYPE html>
<html>
<head>
<link rel="icon" href="data:;base64,iVBORw0KGgo=">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.1.1/jquery.min.js"></script>
<title>Gl&#252;ck Connect 4 App (by Jamie Guthrie)</title>
<meta>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
</meta>
</head>

<body>
	<c:set var="pid1checked" value="checked" />
	<c:set var="pid2checked" value="" />
	<c:if test="${param.pid == game.getPlayerTag(2)}">
		<c:set var="pid1checked" value="" />
		<c:set var="pid2checked" value="checked" />
	</c:if>
	
	<script>
		// Not entirely sure how the events work, but current implemenation
		// seems to have "open" event triggered every time there is a change
		var source = new EventSource('/game/updates/${gameid}');
	 	source.addEventListener('open', function(e) {
	 		window.location.replace(window.location.pathname + '?pid=${param.pid}');
	 	}, false);
	 	source.onmessage = function(e) {
	 		//window.location.replace(window.location.pathname);
	 	};
 	</script>

	<c:out value="Connect 4 by Jamie Guthrie"></c:out>
	<br /><br />
	<div><b>Choose player:</b><br />
	<input id="pid1" type="radio" name="playerchoice" ${pid1checked}/><b>${game.getPlayerTag(1)}</b> <span style="background:red">&nbsp;&nbsp;</span><br />
	<input id="pid2" type="radio" name="playerchoice" ${pid2checked}/><b>${game.getPlayerTag(2)}</b> <span style="background:yellow">&nbsp;&nbsp;</span></div>
	<br />
	<table border="1">
		<tr>
			<c:forEach begin="0" end="${game.getBoard().getNumCols()-1}" varStatus="cols">
				<td><a id="link${cols.index}" href="/game/${gameid}/?move=${cols.index}&pid=${param.pid}">&#11015;</td>
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
	        <b>Player <span style="background:${game.getWinner()}">&nbsp;&nbsp;</span> wins!!</b>
	    </c:when>
	    <c:otherwise>
	        Next player is: <b>${game.getPlayerTag(game.getNextPlayer())}</b> <span style="background:${game.getNextColor()}">&nbsp;&nbsp;</span>
	    </c:otherwise>
	</c:choose>
	<script>
		$('[type=radio]').change(function() {

			var pid = "";
			if($('#pid1').is(':checked')) {
				pid = "${game.getPlayerTag(1)}"
			} else {
				pid = "${game.getPlayerTag(2)}"
			}
			for(var i = 0; i < <c:out value="${game.getBoard().getNumCols()}"></c:out>; i++) {
				var new_href = document.getElementById("link" + i).href.split("&pid=")[0] + '&pid=' + pid;
				$('#link' + i).attr("href", new_href);
			}
			$('#playername').text(pid);
		});
	</script>
	<br /><br />
</body>
</html>
