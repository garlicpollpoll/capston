# [온라인 쇼핑몰] ShopFiesta

# 📈 Introduce
해당 프로젝트의 의의는 현재 존재하는 온라인 쇼핑몰을 현실적으로 구현하는 것입니다. 

기획, 프론트 엔드, 벡 엔드, 배포에 걸쳐서 현재까지 5개월정도 시간이 소요됐습니다. 

현재 버전 3까지 배포된 상태이며 추후 버전 4, 버전 5까지 계속해서 고도화시킬 예정입니다. 

# 🛠️ Use Tools
* **헬퍼** : ChatGPT
* **에디터** : DBeaver, Medis, VSCode, IntelliJ
* **언어** : HTML, CSS, Javascript, Java
* **빌드** : Gradle
* **프레임워크** : Spring Boot, Spring Security, Spring Batch, JUnit5, Mockito
* **ORM** : Spring Data JPA, QueryDSL
* **데이터베이스** : MySQL, Redis, Elasticsearch
* **인프라** : Route53, EC2, S3, ACM, ELB, ECR
* **배포** : Docker, Docker-Compose
* **CI/CD** : Jenkins
* **버전관리** : Git

# ⚙️ System Architecture
<img src="https://github.com/garlicpollpoll/capston/assets/86602266/bc242e4a-591a-4684-9f24-15150d868217">

## 배포 방식
EC2에 Docker를 활용해 배포항녔습니다. 

배포할 때 단순히 배포하기 보단 부정적인 사용자 경험을 줄이기 위해 Blue / Green 배포를 진행했습니다. 

* [Docker-Compose를 이용해 배포하기](https://coding-review.tistory.com/406)
* [Docker-Compose + Nginx를 이용해 무중단 배포하기](https://coding-review.tistory.com/408)

## CI / CD
Blue / Green 배포를 통해 무중단으로 배포할 수 있었지만 배포 과정이 너무 복잡하여 배포하는 것이 큰 부담이 되었습니다. 

이를 해결하기위해 CI 자동화 툴인 Jenkins를 이용해 파이프라인을 작성하여 모든 CI / CD 과정을 클릭 한 번으로 자동화했습니다. 

* [Jenkins vs Github Action](https://coding-review.tistory.com/413)
* [Jenkins 파이프라인 작성하고 CI 자동화](https://coding-review.tistory.com/415)

CI / CD 과정에서 발생할 수 있는 문제인 Hard Shutdown 문제와 JVM의 특성상 배포 직후 Latency가 발생할 것을 우려해 Graceful Shutdown과 ClassLoader Warmup, JIT 컴파일러 Warmup 을 진행했습니다. 

* [Graceful Shutdown](https://coding-review.tistory.com/428)
* [ClassLoader Warmup / JIT compiler Warmup](https://coding-review.tistory.com/428)

# ✔️ Test
서버사이드 랜더링을 선택했기 때문에 Controller 계층의 테스트는 진행하지 않았습니다. 이는 추후 버전 4에 추가할 예정입니다. 

총 43개의 Service 계층 단위 테스트와 총 23개의 Repository 계층 단위 테스트를 완료했습니다. 
<img src="https://github.com/garlicpollpoll/capston/assets/86602266/e4e04729-359b-4a24-9765-75b06b72a8fb">
<img src="https://github.com/garlicpollpoll/capston/assets/86602266/54ed7a14-afcb-4a11-986a-4ba600f326d0">

# 🛒 Core functionality 
## 1. 장바구니 
<img src="https://github.com/garlicpollpoll/capston/assets/86602266/9c398c0b-db10-4663-8e00-7988c1c9e3a9">

장바구니에 제품을 담아 원하는 수량을 선택할 수 있고 체크 후 장바구니에서 삭제할 수 있는 로직을 구현했습니다. 

오른쪽 아래에는 현재 선택한 상품의 총 개수와 가격이 적혀있어 사용자가 직관적으로 장바구니 내에 있는 상품을 체크할 수 있습니다. 

## 2. 좋아요
<img src="https://github.com/garlicpollpoll/capston/assets/86602266/e8257dba-7449-44aa-bd1f-ac9dafa364d4">

상품 상세 페이지에서 좋아요 (혹은 찜하기) 를 선택할 수 있고 좋아요를 누른 상품은 오른쪽 하단 하트모양을 눌러 바로 이동할 수 있습니다. 

좋아요 페이지에선 내가 좋아요를 누른 상품을 한 눈에 볼 수 있으며 체크후 삭제를 진행할 수 있고 체크한 모든 상품을 장바구니로 가져가는 기능을 구현했습니다. 

## 3. 결제
<img src="https://github.com/garlicpollpoll/capston/assets/86602266/ac56cf8b-9afb-411e-b112-00fb50f17992">

Iamport API를 가져와 실제로 결제가 되는 로직을 구현했습니다. 

결제를 진행할 때는 실제로 결제한 것이 맞는지 검증을 통해 사용자가 결제한 내역을 더블체크 하는 로직을 구현했습니다. 

## 4. 쿠폰
<img src="https://github.com/garlicpollpoll/capston/assets/86602266/b4ff89ce-cebc-4062-9d54-37ba13b09a7d">

쿠폰은 등록된 쿠폰에 한해서 쿠폰을 등록할 수 있습니다. 

이미 등록한 쿠폰은 등록할 수 없으며 이미 사용한 쿠폰 또한 등록할 수 없습니다.

등록한 쿠폰은 결제 페이지에서 자유롭게 선택할 수 있습니다. 

## 5. ETC
이 외에도 댓글, 상품 검색, 문의하기, 상품 등록 (관리자 페이지), 회원가입 (이메일로 2차 인증), 소셜 로그인 등이 구현되어있습니다. 

# ❗ Problem Solve
## ver.1 
버전 1에선 우선 돌아가게 만든 후 제대로 만드는 작업을 진행했습니다. 

그러기위해 객체지향 원칙을 지키도록 리팩토링 하였고 애플리케이션의 신뢰도를 높이기 위해 테스트케이스를 작성했습니다. 

## ver.2
버전 2의 컨셉은 **"만약 사용자 수가 1000만명이 된다면 어떤 문제가 발생할까"** 입니다. 

주로 성능적으로 이슈가 될만한 부분을 개선하였습니다. 

### 1. 동시성 문제
재고가 1개 밖에 남지 않은 상황에서 세명이 동시에 결제를 요청하게 되면 재고는 0으로 바뀌고 세개의 결제 로그가 찍히는 상황이 발생했습니다. 

기존 버전 1에선 synchronized 키워드로 동시성 문제를 해결했지만 JPA를 사용하는만큼 트랜잭션이 각각의 스레드에 별도로 할당되며 트랜잭션의 격리수준을 따르기 때문에 값이 꼬이는 동시성 문제는 발생하지 않을 것이라 판단하여 JPA의 비관적 락을 사용하기로 결정했습니다. 

<img src="https://github.com/garlicpollpoll/capston/assets/86602266/8f1160f6-b1d9-4387-a77f-2868055cd7b7">



### 2. Redis를 이용한 캐싱
온라인 쇼핑몰의 특성상 **"누가"** 에 해당하는 데이터가 굉장히 많이 필요합니다. 

**"누가"** 좋아요를 눌렀는지, **"누가"** 결제를 했는지, **"누가"** 장바구니에 어떤 물건을 담았는지, **"누가"** 상품을 등록했는지 등등...

제 프로젝트에선 총 66%에 해당하는 42개의 API 중 28개의 API에서 **"누가"** 에 해당하는 데이터를 요청했습니다. 

이를 데이터베이스 연산으로 보면 총 30%에 해당하는 143개 중 43개였습니다. 

결과적으로 Redis를 도입함으로써 데이터베이스 부하를 30퍼센트 줄일 수 있었습니다. 
<img src="https://github.com/garlicpollpoll/capston/assets/86602266/02375936-2c49-4bdd-a2b3-971cb4fbd89e">

### 3. 검색 성능 개선
기존 검색 기능은 SQL의 LIKE 연산자를 이용해 검색을 구현했습니다. 

문제는 LIKE 연산자를 사용한다면 인덱스를 사용할 수 없고 이 말은 검색을 위해서 풀 스캔을 해야하는 상황이 발생했습니다. 

실제로 100만행의 데이터의 맨 마지막 행을 검색한 결과 0.8초라는 긴 시간이 걸리는 것을 확인했습니다. 
<img src="https://github.com/garlicpollpoll/capston/assets/86602266/4cb21559-ad8f-4ffd-9866-50190aed77d8">

이러한 문제를 Elasticsearch를 도입해 검색 성능을 획기적으로 줄였습니다. 
<img src="https://github.com/garlicpollpoll/capston/assets/86602266/1f9c5564-e81c-4205-b3cb-0af7d8b26d7f"> 

실제로 스프링이 구동되는 시간인 0.35초를 빼고 나면 Elasticsearch는 0.021초 기존 풀 스캔은 0.434초가 걸리는 것을 확인했습니다. 

## ver.3 
버전 3의 컨셉은 **"버전 2를 지속적인 통합, 지속적인 배포 (CI / CD) 를 위해서 어떤 부분이 개선되어야 하는가"** 입니다.

버전 3에서는 주로 배포와 관련된 개선을 이루어냈습니다. 

### 1. Jenkins로 배포 자동화
기존 Blue / Green 배포는 무중단 배포라는 쾌거를 이루었지만 배포 방식이 너무 복잡하다는 문제가 있었습니다. 

총 8단계의 배포 과정이 필요했고 이를 자동화 할 필요성이 생기게 되었습니다. 

이에 자바진영 CI 자동화 툴의 표준인 Jenkins로 CI / CD 과정을 자동화 하였습니다. 
<img src="https://github.com/garlicpollpoll/capston/assets/86602266/31906b10-a345-4927-ba6c-c65821418090"> 

### 2. Graceful shutdown
기존 배포 방식은 자동화로인해 편하게 배포를 진행할 수 있었지만 새로운 서버가 올라갈 때 기존 서버를 이용하고 있던 사용자가 갑자기 연결이 끊겨버리는 상황이 우려되었습니다. 

만약 이 때 사용자가 결제를 진행하고 있었다면 이는 굉장히 부정적인 사용자 경험으로 이어질 수 있다고 판단했습니다. 

때문에 기존 Hard shutdown을 Graceful Shutdown으로 변경함으로써 사용자의 모든 요청이 마무리 되고 서버가 내려가도록 설정했습니다. 

### 3. Warmup
JVM의 특성상 한번 배포가 된 다음부터는 제 성능을 보여주지만 배포가 막 끝났을 때 초기에는 심한 Latency가 발생합니다. 

이는 ClassLoader의 지연 로딩과 JIT 컴파일러가 네이티브 코드로 변경하기 위한 최소 반복 횟수인 250번 때문에 생기는 문제였습니다. 

이를 해결하기위해 ClassLoader Warmup은 스프링의 ApplicationRunner를 구현함으로써 개선하였고, JIT 컴파일러 Warmup은 Jenkins 파이프라인에 warmup 과정을 추가하여 개선하였습니다. 

JIT 컴파일러가 warmup 되는 과정에서 발생할 수 있는 서버 부하는 warmup이 진행되는 중간에는 400코드를 내려보내고 모두 완료된 다음엔 200코드를 내려보냄으로써 개선하였습니다. 


# 🔆 느낀점
개발, 성능 개선, 배포까지 모두 완료한 상태에서 총 5개월이라는 시간이 아깝지 않으면서 뿌듯하다는 느낌을 받았습니다.

하지만 이 프로젝트는 계속해서 관리되고 개선해 나아갈 것이며 최종적인 목표는 제 프로젝트를 MSA로 구현하는 것을 생각하고 있습니다. 

이 프로젝트를 진행하면서 느낀 점은

1. **테스트 케이스의 중요성** : 기존 버전 1에서 테스트 케이스를 작성하던 중에 동시성 문제를 확인했습니다. 동시성 문제는 치명적인 문제이며 반드시 해결해야 할 과제임에 분명합니다. 이를 테스트 케이스 작성 도중 알게 되어 테스트 케이스를 작성하는 것이 얼마나 중요한 것인지 깨닫게 되었습니다.
2. **항상 의심해라** : 실제로 오픈한 서비스가 아니기 때문에 어떤 부분에서 문제가 생길지 추상적으로 생각할 수 밖에 없었습니다. 문제가 닥쳤을 때 해결하는 것이 아닌 문제가 발생할만한 곳을 찾아서 개선해야 하는 상황이다보니 모든 상황에 대해 의심하여 성능 이슈가 발생할만한 부분을 발견할 수 있게 되었습니다. 
3. **불편하면 개선해라 다 방법이 있다** : 배포 과정은 앞에 생략된 부분이 많은데 초기엔 Cafe24를 이용해서 배포를 진행했습니다. 하지만 Cafe24의 특성상 유연하게 서버를 관리할 수 없기 때문에 서버가 내려갔다 올라가는데 5분이상 소요된 상황도 종종 있었습니다. 이후 천천히 EC2, Docker, Docker-Compose, Nginx, Jenkins까지 단계적으로 경험하고 불편한 것을 개선하니 이제 불편한 것이 생기면 어떻게든 방법을 찾아내게 되었습니다. 












