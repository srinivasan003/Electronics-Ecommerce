<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
 <%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ page isELIgnored="false" %>
<html>
<head>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<link rel="stylesheet" href="<c:url value='/resources/css/registrationform.css'></c:url>">


<!-- jQuery library -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js"></script>

<!-- Latest compiled JavaScript -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<link rel="stylesheet" href="<c:url value='/resources/css/navbar.css'></c:url>">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta name="viewport" content="width=device-width, initial-scale=1">


<title>Insert title here</title>
</head>
<body>
<div class="container">
<nav class="navbar navbar-default" id="navbar-bg">
<div class="navbar-header">
				<a href="" class="navbar-brand"><img src="<c:url value='/resources/images/television.jpg'></c:url>" alt="ELECTRONIX" height="30px" width="60px"></a>
				<button type="button" class="navbar-toggle collapsed" 
   data-toggle="collapse" data-target="#navbardemo" aria-expanded="false">
        <span class="sr-only">Toggle navigation</span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </button>
</div>
<div class="collapse navbar-collapse" id="navbardemo">
<ul class="nav navbar-nav">
<li><a href="<c:url value='/home'></c:url>"><span class="glyphicon glyphicon-home"></span>Home</a></li>
<li><a href="<c:url value='/aboutus'></c:url>">AboutUs</a></li>
<li><a href="<c:url value='/admin/getproductform'></c:url>"><span class="glyphicon glyphicon-plus"></span>Add Product</a></li>
<li><a href="<c:url value='/all/getallproducts'></c:url>">Browse all products</a></li>
  <li class="dropdown"><a href="#" class="dropdown-toggle"
					data-toggle="dropdown">Select By Category<span class="caret"></span></a>
					
				<ul class="dropdown-menu">
				<li><a href="#">Kitchen and Dining</a></li>
				<li><a href="#">Furniture</a><li>
				<li><a href="#">All</a></li>
				</ul>
</li>
<c:if test="${pageContext.request.userPrincipal.name ==null}">
					<!-- check if principal object is null -->
					<li><a href="<c:url value='all/getregistrationform'></c:url>">SignUp</a></li>
					<li><a href="<c:url value='/login'></c:url>">Sign In</a></li>
				</c:if>
				<c:if test="${pageContext.request.userPrincipal.name!=null }">
					<li><a href="<c:url value='/orderdetails'></c:url>">Welcome ${pageContext.request.userPrincipal.name }</a></li>
					<security:authorize access="hasRole('ROLE_USER')">
						<li><a href="<c:url value='/cart/getcart'></c:url>"><span
								class="glyphicon glyphicon-shopping-cart"></span>(${sessionScope.cartSize })</a></li>
					</security:authorize>
					<li><a
						href="<c:url value='/j_spring_security_logout'></c:url>">Sign
							out</a></li>
				</c:if>
</ul>
</div>
</nav>

</div>
</body>




</html>