# To Do List
## 프로그래밍 요구 사항
- JDBC를 통해 mariaDB와 연결
- Controller - Service - Repository(DAO)로 구성
  - JDBC를 통한 
- 메서드 라인 수 10줄 이하
- 프론트는 제작되지 않고 백엔드 API를 상상하여 제작
## 도메인 별 정리
### Todo
- 할 일을 처음 만들면 Todo로 만들어짐
- 각 할일은 시간 순서, 중요도등 다양한 정렬 방식을 통해 정렬
- 할 일을 체크하면 Todo -> In Progress 혹은 In Progress -> Done으로 전환
- 각 할일은 Todo, In Progress, Done의 상태를 가짐
  - 각 상태는 바뀔 수 있음
- 시간 순서, 중요도로 정렬
- 제목과 세부 내용으로 저장됨
### 휴지통
- 지운 할일은 휴지통에 저장
- 휴지통에서 복원 가능
- 휴지통에 30일동안 저장되고 이후 삭제