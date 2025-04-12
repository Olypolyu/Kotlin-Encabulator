$latest = "test"
$binPath = "$env:USERPROFILE\.local\bin"
$exePath = Join-Path $binPath "kt-encabulator.exe"

if (Test-Path -Path $exePath) {
    Write-Host "`n> Found existing binary in local bin. Do you wish to Continue? `e[1;34m(y/n)`e[0m"
    $choice = Read-Host
    if ($choice -match '^[yY]$') {
        Remove-Item -Path $exePath -Force
    } else {
        Write-Host "The existing file will not be overwritten. `e[1;34mExiting...`e[0m" -ForegroundColor Blue
        exit
    }
}

if (-Not (Test-Path -Path $binPath)) {
    New-Item -ItemType Directory -Path $binPath | Out-Null
}

Write-Host "`n> Downloading binary with Invoke-WebRequest..."
Invoke-WebRequest -Uri "https://github.com/Olypolyu/Kotlin-Encabulator/releases/download/$latest/kt-encabulator.exe" -OutFile $exePath

Write-Host "`n`e[1;32mReady to go!`e[0m Execute with `"`e[1;34mkt-encabulator.exe`e[0m`""
Write-Host "  - Run with -h or --help for documentation."
Write-Host "  - Add "$binPath" to your PATH to run from anywhere"
Write-Host " "