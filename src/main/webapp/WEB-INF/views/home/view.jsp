<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<html>
<head>
    <title>Home</title>
</head>
<body>

<h2>View Page</h2>

<p>${greeting}</p>
<p>Service : ${obj}</p>

<div>신규 생성</div>
<div>
    <table>
        <colgroup>
            <col width="200px;"/>
            <col width="*"/>
        </colgroup>
        <tr>
            <td>User ID</td>
            <td><input type="text" id="userId" name="userId" value="${obj.USER_ID}"></td>
        </tr>
        <tr>
            <td>User Password</td>
            <td><input type="text" id="userPwd" name="userPwd" value="${obj.userPwd}"></td>
        </tr>
        <tr>
            <td>User Name</td>
            <td><input type="text" id="userName" name="userName" value="${obj.userName}"></td>
        </tr>
    </table>
</div>
<button id="userAddBtn">사용자 생성</button>
<br>
<button id="pythonBtn">Python 결과</button>
<div id="pythonResult"></div>
<br>
<button id="pythonRestBtn">Python Rest 결과</button>
<div id="pythonRestResult"></div>
<script>
    $("#userAddBtn").on("click", function () {
        $.ajax({
            url: "/webtest/user/addUserInfo",
            type: "GET",
            data: jQuery.param({
                userId: $("#userId").val(),
                userPwd: $("#userPwd").val(),
                userName: $("#userName").val()
            }),
            dataType: "json",
            success: function (data) {
                alert("Add Cnt : " + data.addCnt);
            },
            error: function (xhr, status, error) {
                console.log("에러 발생: " + error);
            }

        });
    });

    $("#pythonBtn").on("click", function () {
        $.get('/webtest2/home/getPythonResult?type=SSIM', function (data) {
            $("#pythonResult").html("<img src='data:image/jpeg;base64, " + data + "' style='width:100%'>");
        });
        /*
        $.ajax({
            url : "/webtest2/home/getPythonResult",
            type : "GET",
            data : jQuery.param({type:"SSIM"}),
            dataType : "json",
            beforeSend : function () {
                $("#pythonResult").html("진행중....");
            },
            success : function (response) {
                $("#pythonResult").html(
                    "<img src='data:image/jpeg;base64,"+response+"' style='width:100%'> "
                );
            },
            error : function (xhr, status, error) {
                $("#pythonResult").html(
                    "<span style='color:red;'>에러 발생: " + error + "</span>"
                );
            }
        });
        */
    });

    $("#pythonRestBtn").on("click", function () {
        $.get('/webtest2/home/getRestPythonResult?type=SSIM', function (data) {
            $("#pythonRestResult").html("<img src='data:image/jpeg;base64, " + data + "' style='width:100%'>");
        });
    });
</script>
</body>
</html>