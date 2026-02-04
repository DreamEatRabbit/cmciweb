<%--
  Created by IntelliJ IDEA.
  User: jinsu
  Date: 26. 2. 3.
  Time: 오후 3:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <!-- 1. vis-timeline 스타일 및 스크립트 로드 -->
    <link href="https://unpkg.com" rel="stylesheet" type="text/css" />
    <script src="https://unpkg.com"></script>

    <!-- 2. vis-timeline-arrows (화살표 확장) 로드 -->
    <script src="https://unpkg.com"></script>

    <style>
        body { font-family: sans-serif; }
        #visualization {
            width: 100%;
            border: 1px solid lightgray;
            margin-top: 20px;
        }
        /* 화살표 스타일 커스텀 */
        .vis-arrow {
            stroke-width: 2px;
        }
    </style>
    <script>
        // 1. 작업 아이템 정의 (Items)
        const items = new vis.DataSet([
            { id: 1, content: '1. 요구사항 분석', start: '2024-02-01', end: '2024-02-05', title: '분석 단계' },
            { id: 2, content: '2. DB 설계', start: '2024-02-06', end: '2024-02-10' },
            { id: 3, content: '3. API 개발', start: '2024-02-11', end: '2024-02-15' },
            { id: 4, content: '4. 프론트엔드 개발', start: '2024-02-11', end: '2024-02-20' }
        ]);

        // 2. 의존성 연결 정의 (Dependencies)
        // id_item_1이 선행 작업, id_item_2가 후행 작업입니다.
        const dependencyData = [
            { id: 1, id_item_1: 1, id_item_2: 2 }, // 1번 분석이 끝나야 2번 DB 설계 시작
            { id: 2, id_item_1: 2, id_item_2: 3 }, // 2번 설계가 끝나야 3번 API 개발 시작
            { id: 3, id_item_1: 2, id_item_2: 4 }  // 2번 설계가 끝나야 4번 프론트 개발 시작
        ];

        // 3. 타임라인 옵션 설정
        const options = {
            editable: true,       // 드래그 앤 드롭으로 이동/수정 가능
            stack: false,         // 아이템 겹침 허용 안함 (간트 차트 형태)
            horizontalScroll: true,
            zoomKey: 'ctrlKey',
            orientation: 'top',
            margin: { item: 10, axis: 5 }
        };

        // 4. 타임라인 초기화
        const container = document.getElementById('chart_div');
        const timeline = new vis.Timeline(container, items, options);

        // 5. 화살표(Arrow) 기능 적용
        // Arrow 클래스는 vis-timeline-arrows.js에서 제공됩니다.
        const arrows = new Arrow(timeline, dependencyData);

        // 참고: 데이터가 동적으로 변할 때 화살표를 갱신하려면
        // arrows.addArrow({ id: 4, id_item_1: 3, id_item_2: 5 }) 처럼 사용 가능합니다.
    </script>
</head>
<body>
<div>Vis-Timeline</div>
<div id="chart_div"></div>
</body>
</html>
