const dbConfig = require("../config/db.config.js");
const Sequelize = require("sequelize");

const sequelize = new Sequelize(dbConfig.DB, dbConfig.USER, dbConfig.PASSWORD, {
  host: dbConfig.HOST,
  dialect: dbConfig.dialect,
  port: dbConfig.port,
  define: {
    underscored: false,
  },
  pool: {
    max: dbConfig.pool.max,
    min: dbConfig.pool.min,
    acquire: dbConfig.pool.acquire,
    idle: dbConfig.pool.idle,
  },
});

const db = {};
db.Sequelize = Sequelize;
db.sequelize = sequelize;

// --- Модели ---
db.user         = require("./user.js")(sequelize, Sequelize);
db.surah        = require("./surah.model.js")(sequelize, Sequelize);
db.ayat         = require("./ayat.model.js")(sequelize, Sequelize);
db.playList     = require("./playlist.model.js")(sequelize, Sequelize);
db.playListAyat = require("./playlistAyat.model.js")(sequelize, Sequelize);
db.playListUser = require("./playlistUser.model.js")(sequelize, Sequelize);

// --- Ассоциации (вынесены в отдельный файл) ---
require("./references.model.js")(db);

module.exports = db;