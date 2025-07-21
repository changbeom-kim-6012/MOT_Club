# 🐳 MOT Club 도커 기동 가이드

## 📋 사전 요구사항

- Docker Desktop 설치
- Docker Compose 설치
- 최소 4GB RAM 권장

## 🚀 빠른 시작

### 1. 전체 시스템 기동
```bash
# Windows
start.bat

# 또는 수동으로
docker-compose up --build -d
```

### 2. 시스템 중지
```bash
# Windows
stop.bat

# 또는 수동으로
docker-compose down
```

## 📊 접속 정보

| 서비스 | URL | 포트 | 설명 |
|--------|-----|------|------|
| 프론트엔드 | http://localhost:3000 | 3000 | Next.js 웹 애플리케이션 |
| 백엔드 API | http://localhost:8080 | 8080 | Spring Boot REST API |
| 데이터베이스 | localhost:5432 | 5432 | PostgreSQL |
| Redis | localhost:6379 | 6379 | 세션 저장소 |

## 🔧 관리 명령어

### 컨테이너 상태 확인
```bash
docker-compose ps
```

### 로그 확인
```bash
# 전체 로그
docker-compose logs

# 실시간 로그 (특정 서비스)
docker-compose logs -f backend
docker-compose logs -f frontend
docker-compose logs -f postgres
```

### 컨테이너 재시작
```bash
# 특정 서비스만 재시작
docker-compose restart backend
docker-compose restart frontend

# 전체 재시작
docker-compose restart
```

### 데이터베이스 접속
```bash
# PostgreSQL 접속
docker exec -it mot_postgres psql -U postgres -d mot_db
```

## 🗂️ 프로젝트 구조

```
MOT_Club/
├── docker-compose.yml          # 도커 컴포즈 설정
├── start.bat                   # Windows 기동 스크립트
├── stop.bat                    # Windows 중지 스크립트
├── Backend/
│   └── mot/
│       ├── Dockerfile          # 백엔드 도커 설정
│       └── .dockerignore       # 백엔드 제외 파일
└── Frontend/
    └── mot/
        ├── Dockerfile          # 프론트엔드 도커 설정
        └── .dockerignore       # 프론트엔드 제외 파일
```

## 🔍 문제 해결

### 1. 포트 충돌
```bash
# 사용 중인 포트 확인
netstat -ano | findstr :3000
netstat -ano | findstr :8080
netstat -ano | findstr :5432

# 해당 프로세스 종료 후 재시작
```

### 2. 컨테이너 빌드 실패
```bash
# 캐시 제거 후 재빌드
docker-compose build --no-cache
docker-compose up -d
```

### 3. 데이터베이스 연결 실패
```bash
# 데이터베이스 컨테이너 상태 확인
docker-compose logs postgres

# 데이터베이스 재시작
docker-compose restart postgres
```

### 4. 메모리 부족
```bash
# Docker Desktop 메모리 설정 증가 (8GB 권장)
# Docker Desktop > Settings > Resources > Memory
```

## 📝 환경 변수

### 백엔드 환경 변수
- `SPRING_DATASOURCE_URL`: 데이터베이스 연결 URL
- `SPRING_DATASOURCE_USERNAME`: 데이터베이스 사용자명
- `SPRING_DATASOURCE_PASSWORD`: 데이터베이스 비밀번호
- `SPRING_REDIS_HOST`: Redis 호스트
- `SPRING_REDIS_PORT`: Redis 포트

### 프론트엔드 환경 변수
- `NEXT_PUBLIC_API_URL`: 백엔드 API URL

## 🔄 개발 모드

### 로컬 개발 (도커 없이)
```bash
# 백엔드 (Backend/mot 디렉토리에서)
./gradlew bootRun

# 프론트엔드 (Frontend/mot 디렉토리에서)
npm run dev
```

### 도커 개발 모드
```bash
# 개발용 docker-compose 파일 사용
docker-compose -f docker-compose.dev.yml up
```

## 🗑️ 완전 삭제

```bash
# 모든 컨테이너, 이미지, 볼륨 삭제
docker-compose down -v --rmi all
docker system prune -a
```

## 📞 지원

문제가 발생하면 다음을 확인하세요:
1. Docker Desktop이 실행 중인지 확인
2. 포트가 사용 중이지 않은지 확인
3. 충분한 메모리가 있는지 확인
4. 로그를 확인하여 구체적인 오류 메시지 확인 

# MOT Club 개발 환경 설정

## 빠른 시작

### Windows에서 개발 환경 시작

#### 방법 1: 배치 파일 사용 (권장)
```bash
# 프로젝트 루트에서 실행
start-dev.bat
```

#### 방법 2: PowerShell 스크립트 사용
```powershell
# 프로젝트 루트에서 실행
.\start-dev.ps1
```

#### 방법 3: 수동 시작
```bash
# 백엔드 시작
cd Backend/mot
./gradlew bootRun

# 새 터미널에서 프론트엔드 시작
cd Frontend/mot
npm run dev
```

## 자동 재시작 설정

### 백엔드 (Spring Boot)
- Spring Boot DevTools가 이미 설정되어 있어 코드 변경 시 자동 재시작됩니다.
- `application.properties`에서 `spring.devtools.restart.enabled=true` 설정 확인

### 프론트엔드 (Next.js)
- Next.js는 기본적으로 개발 모드에서 자동 재시작이 활성화되어 있습니다.
- 파일 변경 시 자동으로 브라우저가 새로고침됩니다.

## 접속 주소
- **백엔드 API**: http://localhost:8080
- **프론트엔드**: http://localhost:3000
- **관리자 페이지**: http://localhost:3000/admin

## 문제 해결

### PowerShell에서 && 연산자 오류
Windows PowerShell에서는 `&&` 연산자가 지원되지 않습니다. 대신:
- `;` 사용: `cd Backend/mot; ./gradlew bootRun`
- 별도 명령어로 실행
- 제공된 스크립트 파일 사용

### 포트 충돌
- 백엔드: 8080 포트가 사용 중인 경우 `application.properties`에서 `server.port` 변경
- 프론트엔드: 3000 포트가 사용 중인 경우 자동으로 3001, 3002 등으로 변경됨

## 개발 팁
1. 코드 변경 시 자동으로 재시작되므로 별도 재시작이 필요 없습니다.
2. 백엔드 변경 후 프론트엔드에서 새로고침만 하면 변경사항이 반영됩니다.
3. 브라우저 개발자 도구의 Network 탭에서 API 호출 상태를 확인할 수 있습니다. 