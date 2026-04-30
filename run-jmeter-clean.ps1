# run-jmeter-clean.ps1
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  JMeter Performance Test Runner" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

$JMETER_BAT = "C:\Users\Priyansu Sikdar\Downloads\apache-jmeter-5.6.3\apache-jmeter-5.6.3\bin\jmeter.bat"

# Clean up previous test artifacts
Write-Host "Cleaning previous test artifacts..." -ForegroundColor Yellow

if (Test-Path "target/jmeter-results.jtl") {
    Remove-Item -Path "target/jmeter-results.jtl" -Force
    Write-Host "  ✅ Removed old results file" -ForegroundColor Green
}

if (Test-Path "target/jmeter-report") {
    Remove-Item -Path "target/jmeter-report" -Recurse -Force
    Write-Host "  ✅ Removed old report folder" -ForegroundColor Green
}

Write-Host ""
Write-Host "Compiling test classes..." -ForegroundColor Yellow
mvn test-compile -q

if ($LASTEXITCODE -ne 0) {
    Write-Host "❌ Compilation failed!" -ForegroundColor Red
    pause
    exit $LASTEXITCODE
}

Write-Host "✅ Compilation successful" -ForegroundColor Green
Write-Host ""
Write-Host "Running JMeter test..." -ForegroundColor Yellow
Write-Host "========================================" -ForegroundColor Cyan

& $JMETER_BAT -n -t src/test/jmeter/user-load-test.jmx -l target/jmeter-results.jtl -e -o target/jmeter-report -JTHREADS=50 -JRAMP_UP=10 -JLOOPS=5

if ($LASTEXITCODE -ne 0) {
    Write-Host ""
    Write-Host "❌ JMeter test failed!" -ForegroundColor Red
    pause
    exit $LASTEXITCODE
}

Write-Host ""
Write-Host "========================================" -ForegroundColor Green
Write-Host "✅ Test completed successfully!" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Green
Write-Host ""

# Open the report
$reportPath = "target\jmeter-report\index.html"
if (Test-Path $reportPath) {
    Write-Host "Opening HTML report..." -ForegroundColor Yellow
    Start-Process $reportPath
} else {
    Write-Host "⚠️ Report not found at: $reportPath" -ForegroundColor Yellow
}

Write-Host ""
pause
