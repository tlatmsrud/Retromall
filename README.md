# 🕹️Retromall
## 💻 프로젝트 소개
콘솔 게이머들을 위한 중고장터</br>
기존 중고장터앱은 많은 생활용품에 대해 초점이 맞춰져있으며, 게이머들은 루리웹, 네이버 카페, 스마트 스토어 등 여러 커뮤니티에 의존하여 매장홍보 및 직거래를 통해 거래가 이루어집니다.
Retromall 서비스는 소수의 게임 리커머스 서비스로써 현세대 및 레트로 게임기에 맞춰진 서비스입니다.
## 🔧 개발기간
2023.03 ~
## ⚙️ 개발환경
- Spring Boot
- Spring Data Jpa
- QueryDsl
- Mysql
- Redis
## 📌 주요기능
- 유저 관련
  - OAuth2 로그인 기능
  - JWT를 이용한 인증
  - Redis를 이용한 RefreshToken 관리
- 상품 관련
  - 상품등록 및 삭제, 수정
  - 상품 수정, 삭제 시 유저 권한 확인
  - 상품 리스트 필터 조회
  - 카테고리별 상품 조회
  - 상품리스트 noOffset 페이징처리
  - 상품 좋아요 기능
  - 캐시를 통한 주소 검색기능(작업 중)


## 🖼️ 이미지
개발 진행중인 프로젝트로 피그마 이미지로 대체하였습니다.
<img src="https://user-images.githubusercontent.com/52519728/241888299-f3cabb6e-7bd9-41b6-90cf-36ccd9b82a72.png">
<img src="https://user-images.githubusercontent.com/52519728/241888723-887f466d-6aab-4067-a28c-c6a2f02db87a.png">
<img src="https://user-images.githubusercontent.com/52519728/241888758-d3d7987a-581e-4052-a6ce-db2abed44db8.png">
<img src="https://user-images.githubusercontent.com/52519728/241888776-4592cd8e-385b-4b60-b210-d10b794f9f9b.png">
## ER 다이어그램
<img src="https://user-images.githubusercontent.com/52519728/241885626-85a652d0-3809-4869-a851-db249f3145ff.svg" alt="Retromall ER Diagram">

## 시퀀스 다이어그램
- OAuth2 로그인
<img src="https://user-images.githubusercontent.com/52519728/241889276-1a3f5c51-2add-4de5-92d4-cdcd8f7e362d.svg">

- 상품 등록, 수정, 삭제
<img src="https://user-images.githubusercontent.com/52519728/241885915-7ff27712-06b9-48bf-837a-8d7225ca8abd.svg">
<img src="https://user-images.githubusercontent.com/52519728/241885988-9136e638-c645-4352-be8f-c3b526c9d253.svg">
<img src="https://user-images.githubusercontent.com/52519728/241886367-35bcb2a4-7943-483e-bb7a-80a4cf822fa1.svg">

- 상품 좋아요, 취소
<img src="https://user-images.githubusercontent.com/52519728/241886038-7d64d95c-36cf-4e74-95f7-67a352ad14dc.svg">
<img src="https://user-images.githubusercontent.com/52519728/242162017-fdbd44fd-9871-497f-b351-451f463ba3d1.svg">
## 클래스 다이어그램