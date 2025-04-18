#!/usr/bin/sh
LASTEST="0.1.0"

if [ -f "$HOME/.local/bin/kt-encabulator" ]; then
  echo -e "\n> Found existing binary in local bin. Do you wish to Continue? \e[1;34m(y/n)\e[0m"

  read choice < /dev/tty
  case "$choice" in
    y|Y ) rm -r "$HOME/.local/bin/kt-encabulator";;
    * ) echo -e "The existing file will not be overwritten. \e[1;34mExiting...\e[0m"; exit;;
  esac
fi

echo -e "\n> Downloading binary with curl...\n"
curl -L -o "$HOME/.local/bin/kt-encabulator" "https://github.com/Olypolyu/Kotlin-Encabulator/releases/download/$LASTEST/kt-encabulator"
chmod +x "$HOME/.local/bin/kt-encabulator"

echo -e "\n\e[1;32mReady to go!\e[0m Execute with \"\e[1;34mkt-encabulator\e[0m\""
echo "  - Run with -h or --help for documentation. \n"

echo $LASTEST >> "$HOME/.local/bin/.kt-encabulator.version.txt"