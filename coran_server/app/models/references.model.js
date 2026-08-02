module.exports = (db) => {
  // Surah -> Ayat (одна сура содержит много аятов)
  db.surah.hasMany(db.ayat, { foreignKey: "id_sura", as: "ayats" });
  db.ayat.belongsTo(db.surah, { foreignKey: "id_sura", as: "surah" });

  // PlayList <-> Ayat (через промежуточную таблицу Pley_list_ayats)
  db.playList.belongsToMany(db.ayat, {
    through: db.playListAyat,
    foreignKey: "id_play_list",
    otherKey: "id_ayats",
    as: "ayats",
  });
  db.ayat.belongsToMany(db.playList, {
    through: db.playListAyat,
    foreignKey: "id_ayats",
    otherKey: "id_play_list",
    as: "playLists",
  });

  // PlayList <-> User (через промежуточную таблицу Play_list_user)
  db.playList.belongsToMany(db.user, {
    through: db.playListUser,
    foreignKey: "id_play_list",
    otherKey: "id_user",
    as: "users",
  });
  db.user.belongsToMany(db.playList, {
    through: db.playListUser,
    foreignKey: "id_user",
    otherKey: "id_play_list",
    as: "playLists",
  });
};
