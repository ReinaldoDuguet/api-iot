#!/bin/bash
sudo apt update
sudo apt install -y docker.io docker-compose git
sudo systemctl enable docker
sudo usermod -aG docker $USER

# Clonar repositorio si no existe
cd ~
if [ ! -d "api-iot" ]; then
  git clone https://github.com/JulioRom/api-iot.git api-iot
fi
