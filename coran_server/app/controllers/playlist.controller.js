const db = require("../models");
const PlayList = db.playList;

// POST /api/playlists
exports.create = (req, res) => {
  const { name_play_list } = req.body;

  if (!name_play_list) {
    return res.status(400).send({ message: "name_play_list cannot be empty!" });
  }

  PlayList.create({ name_play_list })
    .then(data => res.status(201).send(data))
    .catch(err => res.status(500).send({ message: err.message || "Error creating PlayList." }));
};

// GET /api/playlists
exports.findAll = (req, res) => {
  PlayList.findAll({
    include: [
      { model: db.ayat, as: "ayats" },
      { model: db.user, as: "users", attributes: { exclude: ["password"] } },
    ],
  })
    .then(data => res.send(data))
    .catch(err => res.status(500).send({ message: err.message || "Error retrieving playlists." }));
};

// GET /api/playlists/:id
exports.findOne = (req, res) => {
  const id = req.params.id;
  PlayList.findByPk(id, {
    include: [
      { model: db.ayat, as: "ayats", include: [{ model: db.surah, as: "surah" }] },
      { model: db.user, as: "users", attributes: { exclude: ["password"] } },
    ],
  })
    .then(data => {
      if (!data) return res.status(404).send({ message: `PlayList not found with id=${id}` });
      res.send(data);
    })
    .catch(err => res.status(500).send({ message: err.message || `Error retrieving PlayList with id=${id}` }));
};

// PUT /api/playlists/:id
exports.update = (req, res) => {
  const id = req.params.id;
  PlayList.update(req.body, { where: { id_play_list: id } })
    .then(([num]) => {
      if (num === 1) {
        res.send({ message: "PlayList was updated successfully." });
      } else {
        res.status(404).send({ message: `Cannot update PlayList with id=${id}. Not found or data is same.` });
      }
    })
    .catch(err => res.status(500).send({ message: err.message || `Error updating PlayList with id=${id}` }));
};

// DELETE /api/playlists/:id
exports.delete = (req, res) => {
  const id = req.params.id;
  PlayList.destroy({ where: { id_play_list: id } })
    .then(num => {
      if (num === 1) {
        res.send({ message: "PlayList was deleted successfully!" });
      } else {
        res.status(404).send({ message: `Cannot delete PlayList with id=${id}. Not found.` });
      }
    })
    .catch(err => res.status(500).send({ message: err.message || `Could not delete PlayList with id=${id}` }));
};

// POST /api/playlists/:id/ayats   — добавить аят в плейлист
exports.addAyat = (req, res) => {
  const { id } = req.params;
  const { id_ayats } = req.body;

  if (!id_ayats) {
    return res.status(400).send({ message: "id_ayats cannot be empty!" });
  }

  PlayList.findByPk(id)
    .then(playlist => {
      if (!playlist) return res.status(404).send({ message: `PlayList not found with id=${id}` });
      return db.playListAyat.create({ id_play_list: id, id_ayats });
    })
    .then(() => res.status(201).send({ message: "Ayat added to PlayList successfully." }))
    .catch(err => res.status(500).send({ message: err.message || "Error adding Ayat to PlayList." }));
};

// DELETE /api/playlists/:id/ayats/:ayatId   — убрать аят из плейлиста
exports.removeAyat = (req, res) => {
  const { id, ayatId } = req.params;
  db.playListAyat.destroy({ where: { id_play_list: id, id_ayats: ayatId } })
    .then(num => {
      if (num === 1) {
        res.send({ message: "Ayat removed from PlayList successfully." });
      } else {
        res.status(404).send({ message: "Record not found." });
      }
    })
    .catch(err => res.status(500).send({ message: err.message || "Error removing Ayat from PlayList." }));
};
