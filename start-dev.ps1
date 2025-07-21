# MOT Club 개발 환경 시작 스크립트
Write-Host "========================================" -ForegroundColor Green
Write-Host "MOT Club 개발 환경 시작" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Green

Write-Host ""
Write-Host "1. 백엔드 시작 중..." -ForegroundColor Yellow
Set-Location "Backend\mot"
Start-Process powershell -ArgumentList "-NoExit", "-Command", ".\gradlew bootRun" -WindowStyle Normal

Write-Host ""
Write-Host "2. 프론트엔드 시작 중..." -ForegroundColor Yellow
Set-Location "..\..\Frontend\mot"
Start-Process powershell -ArgumentList "-NoExit", "-Command", "npm run dev" -WindowStyle Normal

Write-Host ""
Write-Host "3. 브라우저에서 다음 주소로 접속하세요:" -ForegroundColor Cyan
Write-Host "   - 프론트엔드: http://localhost:3000" -ForegroundColor White
Write-Host "   - 백엔드 API: http://localhost:8080" -ForegroundColor White
Write-Host ""
Write-Host "4. 개발 환경이 완전히 시작될 때까지 잠시 기다려주세요." -ForegroundColor Yellow
Write-Host ""
Write-Host "종료하려면 각 PowerShell 창을 닫거나 Ctrl+C를 누르세요." -ForegroundColor Red
Write-Host ""
Read-Host "계속하려면 Enter를 누르세요" 