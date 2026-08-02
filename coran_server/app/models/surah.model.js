module.exports = (sequelize, Sequelize) => {
  const Surah = sequelize.define("Surah", {
    id_sura: {
      type: Sequelize.INTEGER,
      primaryKey: true,
      autoIncrement: true,
    },
    number_sura: {
      type: Sequelize.INTEGER,
      allowNull: false,
    },
    audio_link: {
      type: Sequelize.STRING,
    },
    sura_name: {
      type: Sequelize.STRING,
    },
    transcription_name: {
      type: Sequelize.STRING,
    },
    translation_name: {
      type: Sequelize.STRING,
    },
  }, {
    tableName: "Surahs",
    timestamps: false,
  });

  return Surah;
};
