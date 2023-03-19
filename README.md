### 프로젝트명: 하루네컷(1D4P)
4컷의 사진으로 하루를 기록하는 어플리케이션

### 📌 1. 진행일정
|내용|날짜|
|-----|----------|
|시작일|2023.03.19 일|

### 🔥 2. 문서 링크 정리
```
Designed by Yeonju.jin & copyright all right reserved by 1D4P
```
|번호|문서명|링크|
|-----|----------|---------------|
|1|Figma UI 기획서|[Figma 링크](https://www.figma.com/file/CtdgCexcy3xKMZhIHMfvzv/1D4P?node-id=0%3A1&t=LKgFVvAJmqEbTQXC-1)|
|2|화면기획서|[화면기획서 링크](https://drive.google.com/file/d/1JE1W6PP1Jeo221fRC21KiNCRNsPxMAd2/view?usp=sharing)|
|3|WBS 문서|[WBS 스프레드시트 링크](https://docs.google.com/spreadsheets/d/1S435tcJCSA03kSBVlM40z11Wx5wGSth9FN6AMhVwdXg/edit?usp=sharing)|

### 🏠 3. Branch 전략
```
1. 이슈 생성 > PR 생성 > 코드리뷰 > Allow > Merge
2. 자신의 Branch는 자신이 Merge 합니다.
3. 반드시 코드리뷰를 진행한 이후에 Merge 하셔야 합니다.
4. 코드리뷰에 나온 사항 중 차후에 변경할 사항이라면 반드시 이슈생성이 선행적으로 이루어진 후 Merge할 수 있습니다.
```
#### 3.1 Branch 명
```
[이슈카테고리]/[이슈번호]-[작업이름]

ex. Base/#4-create-project
```

#### 3.2 Commit
```
[작업 타입] [#이슈번호] - [작업설명]

ex. [CREATE] #4 - create basic compose project
```
|번호|작업타입|설명|
|-----|----------|---------------|
|1|CREATE|파일 및 프로젝트 생성|
|2|EDIT|파일 수정|
|3|FEAT|새로운 기능 추가|
|4|REFACTOR|기존 기능 수정|
|5|DELETE|기존 코드 제거|
|6|TEST|테스트 코드 추가|

<br/>

#### 3.3 Branch Flow
<img width="840" alt="스크린샷 2023-02-22 오전 12 19 27" src="https://user-images.githubusercontent.com/22411296/220386178-478f7056-de6e-45e0-80cf-fd947f3c3e5c.png">
