module.exports = (sequelize, Sequelize) => {
  const User = sequelize.define("User", {
    Id_user: {
      type: Sequelize.INTEGER,
      primaryKey: true,
      autoIncrement: true,
    },
    username: {
      type: Sequelize.STRING,
      allowNull: false,
    },
    password: {
      type: Sequelize.STRING,
      allowNull: false,
    },
    status: {
      type: Sequelize.STRING,
    },
  }, {
    tableName: "Users",
    timestamps: false,
  });

  return User;
};
