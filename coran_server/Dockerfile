FROM node:20-alpine

WORKDIR /coran_server

COPY package*.json ./

RUN npm install

COPY . .

EXPOSE 6868

CMD ["node", "server.js"]