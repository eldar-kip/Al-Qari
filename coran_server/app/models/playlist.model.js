module.exports = (sequelize, Sequelize) => {
  const PlayList = sequelize.define("PlayList", {
    id_play_list: {
      type: Sequelize.INTEGER,
      primaryKey: true,
      autoIncrement: true,
    },
    name_play_list: {
      type: Sequelize.STRING,
      allowNull: false,
    },
  }, {
    tableName: "Play_list",
    timestamps: false,
  });

  return PlayList;
};
