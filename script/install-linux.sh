#!/usr/bin/sh
LASTEST="test"

if [ -f "$HOME/.local/bin/kt-encabulator" ]; then
  echo -e "> Found existing binary in local bin. Do you wish to Continue? \e[1;34m(y/n)\e[0m"

  read choice < /dev/tty
  case "$choice" in
    y|Y ) rm -r "$HOME/.local/bin/kt-encabulator";;
    * ) echo "The existing file will not be overwritten. Exiting..."; exit;;
  esac
fi

echo " "
echo -e "> Downloading binary with curl...\n"
curl -L -o "$HOME/.local/bin/kt-encabulator" "https://github.com/Olypolyu/Kotlin-Encabulator/releases/download/$LASTEST/kt-encabulator"
chmod +x "$HOME/.local/bin/kt-encabulator"

echo " "
echo -e "\e[1;32mReady to go!\e[0m Execute with \"\e[1;34mkt-encabulator\e[0m\""
echo "  - Run with -h or --help for documentation."
echo " "