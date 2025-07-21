@echo off
echo ========================================
echo MOT Club 개발 환경 시작
echo ========================================

echo.
echo 1. 백엔드 시작 중...
cd Backend\mot
start "MOT Backend" cmd /k "gradlew bootRun"

echo.
echo 2. 프론트엔드 시작 중...
cd ..\..\Frontend\mot
start "MOT Frontend" cmd /k "npm run dev"

echo.
echo 3. 브라우저에서 다음 주소로 접속하세요:
echo    - 프론트엔드: http://localhost:3000
echo    - 백엔드 API: http://localhost:8080
echo.
echo 4. 개발 환경이 완전히 시작될 때까지 잠시 기다려주세요.
echo.
pause 