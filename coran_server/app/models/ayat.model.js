module.exports = (sequelize, Sequelize) => {
  const Ayat = sequelize.define("Ayat", {
    id_ayats: {
      type: Sequelize.INTEGER,
      primaryKey: true,
      autoIncrement: true,
    },
    number_ayata: {
      type: Sequelize.INTEGER,
      allowNull: false,
    },
    id_sura: {
      type: Sequelize.INTEGER,
      allowNull: false,
      references: {
        model: "Surahs",
        key: "id_sura",
      },
    },
    arab_text: {
      type: Sequelize.TEXT,
    },
    transcription: {
      type: Sequelize.TEXT,
    },
    translation: {
      type: Sequelize.TEXT,
    },
    time_start: {
      type: Sequelize.STRING,
    },
    time_end: {
      type: Sequelize.STRING,
    },
  }, {
    tableName: "Ayats",
    timestamps: false,
  });

  return Ayat;
};
