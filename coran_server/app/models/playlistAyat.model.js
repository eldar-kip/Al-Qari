module.exports = (sequelize, Sequelize) => {
  const PlayListAyat = sequelize.define("PlayListAyat", {
    id_play_list: {
      type: Sequelize.INTEGER,
      allowNull: false,
      references: {
        model: "Play_list",
        key: "id_play_list",
      },
    },
    id_ayats: {
      type: Sequelize.INTEGER,
      allowNull: false,
      references: {
        model: "Ayats",
        key: "id_ayats",
      },
    },
  }, {
    tableName: "Pley_list_ayats",
    timestamps: false,
  });

  return PlayListAyat;
};
