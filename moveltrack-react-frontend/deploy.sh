#!/usr/bin/env bash
# Builda react para produção
npm run build

# Copia a pasta build local para o digital ocean
scp -r build/* root@138.197.22.183:/home/barros/cp-frontend2/build
