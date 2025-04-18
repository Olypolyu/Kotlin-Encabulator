$latest = "0.1.0"
$binPath = "$env:USERPROFILE\.local\bin"
$exePath = Join-Path $binPath "kt-encabulator.exe"

if (Test-Path -Path $exePath) {
    Write-Host -NoNewline "`n> Found existing binary in local bin. Do you wish to Continue? "
    Write-Host "(y/n)" -ForegroundColor Blue

    $choice = Read-Host
    if ($choice -match '^[yY]$') {
        Remove-Item -Path $exePath -Force
    }

    else {
        Write-Host "The existing file will not be overwritten. Exiting..." -ForegroundColor Blue
        exit
    }
}

if (-Not (Test-Path -Path $binPath)) {
    New-Item -ItemType Directory -Path $binPath | Out-Null
}

Write-Host "`n> Downloading binary with Invoke-WebRequest..."
Invoke-WebRequest -Uri "https://github.com/Olypolyu/Kotlin-Encabulator/releases/download/$latest/kt-encabulator.exe" -OutFile $exePath
Out-File $latest -FilePath Join-Path $binPath ".kt-encabulator.version.txt"

Write-Host -NoNewline "`nReady to go! " -ForegroundColor Green
Write-Host -NoNewline "Execute with "
Write-Host "`"kt-encabulator.exe`"" -ForegroundColor Blue

Write-Host "  - Add $binPath to your PATH to run from anywhere"
Write-Host "  - Run with -h or --help for documentation."
Write-Host " "
