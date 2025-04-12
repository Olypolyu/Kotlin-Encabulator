#!/usr/bin/sh
LASTEST="test"

if [ -f "$HOME/.local/bin/kt-encabulator" ]; then
  read -p "Found existing binary in local bin. Do you wish to Continue (y/n)?" choice

  case "$choice" in
    y|Y ) rm -r "$HOME/.local/bin/kt-encabulator";;
    * ) echo "Exiting."; exit;;
  esac
fi

echo "Downloading file with curl."
curl -L -o "$HOME/.local/bin/kt-encabulator" "https://github.com/Olypolyu/Kotlin-Encabulator/releases/download/$LASTEST/kt-encabulator"
chmod +x "$HOME/.local/bin/kt-encabulator"
echo "Ready to go! Execute with `$kt-encabulator`"