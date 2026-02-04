<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <link rel="stylesheet" href="<c:url value="/resources/css/frappe/frappe-gantt.css"/>"/>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="<c:url value="/resources/js/frappe/frappe-gantt.umd.js"/>"></script>
    <title>Title</title>
</head>
<body>

<div class="container">
    <h2>프로젝트 일정 (Frappe Gantt)</h2>
    <div class="controls">
        <button onclick="changeView('Day')">일 단위</button>
        <button onclick="changeView('Week')">주 단위</button>
        <button onclick="changeView('Month')">월 단위</button>
    </div>
    <!-- 간트 차트가 그려질 SVG 영역 -->
    <svg id="gantt-target"></svg>
</div>

<script>
    // 1. 데이터 정의 (dependencies 필드가 화살표를 만듭니다)
    const tasks = [
        {
            id: 'T1',
            name: '기획서 작성',
            start: '2023-10-01',
            end: '2023-10-04',
            progress: 100
        },
        {
            id: 'T2',
            name: 'DB 설계',
            start: '2023-10-05',
            end: '2023-10-08',
            progress: 60,
            dependencies: 'T1' // T1이 끝나면 시작됨을 화살표로 표시
        },
        {
            id: 'T3',
            name: 'API 개발',
            start: '2023-10-09',
            end: '2023-10-15',
            progress: 20,
            dependencies: 'T2' // T2와 연결
        }
    ];

    // 2. 간트 차트 초기화
    const gantt = new Gantt("#gantt-target", tasks, {
        on_click: task => console.log(task),
        on_date_change: (task, start, end) => console.log(task.name + " 날짜 변경됨"),
        language: 'ko' // 한국어 지원
    });

    // 3. 뷰 모드 변경 함수
    function changeView(mode) {
        gantt.change_view_mode(mode);
    }
</script>
</body>
</html>
