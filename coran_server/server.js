require("dotenv").config();
const express = require("express");
const cors = require("cors");
const path = require("path");
const session = require("express-session");
const bcrypt = require("bcryptjs");
const swaggerJSDoc = require("swagger-jsdoc");
const swaggerUi = require("swagger-ui-express");

const app = express();
const db = require("./app/models");
// const { injectUser } = require("./app/middleware/auth.middleware");
// const initTriggers = require("./app/database/init_triggers");

const PORT = process.env.NODE_DOCKER_PORT || 8081;

// --- 1. Middleware (Промежуточные обработчики) ---

// Сессии
app.use(session({
  secret: process.env.SESSION_SECRET || "Coran_ychi",
  resave: false,
  saveUninitialized: false,
  cookie: { maxAge: 24 * 60 * 60 * 1000 } // 24 часа
}));

// CORS
app.use(cors({ origin: "*" }));

// Парсинг JSON и form-data
app.use(express.json());
app.use(express.urlencoded({ extended: true }));

// Статические файлы
app.use(express.static(path.join(__dirname, "public")));

// Inject current user in all EJS templates
// app.use(injectUser);


// --- 2. Swagger (Документация API) ---

const swaggerOptions = {
  definition: {
    openapi: "3.0.0",
    info: {
      title: "Quran API",
      version: "1.0.0",
      description: "REST API для приложения Коран"
    },
    servers: [
      {
        url: `http://localhost:${process.env.NODE_LOCAL_PORT || 8080}`,
        description: "Development server"
      }
    ],
    components: {
      schemas: {},
      securitySchemes: {
        bearerAuth: { type: "http", scheme: "bearer", bearerFormat: "JWT" }
      },
    },
    security: [{ bearerAuth: [] }],
  },
  apis: ["./app/routes/*.js"],
};

const swaggerSpec = swaggerJSDoc(swaggerOptions);
app.use("/api-docs", swaggerUi.serve, swaggerUi.setup(swaggerSpec));


// --- 3. Подключение маршрутов (API Routes) ---

// --- Маршруты API ---
require("./app/routes/user.routes")(app);
require("./app/routes/surah.routes")(app);
require("./app/routes/ayat.routes")(app);
require("./app/routes/playlist.routes")(app);


// --- 4. Подключение к БД и запуск сервера ---

const connectWithRetry = (attempt = 1) => {
  console.log(`Connecting to database (Attempt ${attempt}/5)...`);

  db.sequelize.authenticate()
    .then(() => {
      console.log("Database connection established.");
      return db.sequelize.sync({ alter: true });
    })
    .then(async () => {
      console.log("Database schema synced successfully.");
      // await initTriggers(db.sequelize);

      app.listen(PORT, () => {
        console.log(`>>> Server is ready on http://localhost:${PORT}`);
        console.log(`>>> Swagger docs available at http://localhost:${process.env.NODE_LOCAL_PORT || 8080}/api-docs`);
      });
    })
    .catch((err) => {
      console.error(`Attempt ${attempt} failed: Unable to connect to Database!`);
      console.error(err.message);

      if (attempt < 5) {
        console.log("Retrying in 5 seconds...");
        setTimeout(() => connectWithRetry(attempt + 1), 5000);
      } else {
        console.warn("CRITICAL: All connection attempts failed. Starting in SAFE MODE.");
        app.listen(PORT, () => console.log(`Server started in SAFE MODE on port ${PORT}.`));
      }
    });
};

connectWithRetry();