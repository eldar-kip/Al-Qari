module.exports = (sequelize, Sequelize) => {
  const PlayListUser = sequelize.define("PlayListUser", {
    id_play_list: {
      type: Sequelize.INTEGER,
      allowNull: false,
      references: {
        model: "Play_list",
        key: "id_play_list",
      },
    },
    id_user: {
      type: Sequelize.INTEGER,
      allowNull: false,
      references: {
        model: "Users",
        key: "Id_user",
      },
    },
  }, {
    tableName: "Play_list_user",
    timestamps: false,
  });

  return PlayListUser;
};
