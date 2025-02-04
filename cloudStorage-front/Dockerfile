FROM node:19
WORKDIR /app
COPY package*.json ./
EXPOSE 8080
COPY . .
RUN npm install
CMD [ "npm", "run", "serve" ]