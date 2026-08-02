const db = require("../models");
const Surah = db.surah;

// POST /api/surahs
exports.create = (req, res) => {
  const { number_sura, audio_link, sura_name, transcription_name, translation_name } = req.body;

  if (!number_sura || !sura_name) {
    return res.status(400).send({ message: "number_sura and sura_name cannot be empty!" });
  }

  Surah.create({ number_sura, audio_link, sura_name, transcription_name, translation_name })
    .then(data => res.status(201).send(data))
    .catch(err => res.status(500).send({ message: err.message || "Error creating Surah." }));
};

// GET /api/surahs
exports.findAll = (req, res) => {
  Surah.findAll()
    .then(data => res.send(data))
    .catch(err => res.status(500).send({ message: err.message || "Error retrieving surahs." }));
};

// GET /api/surahs/:id
exports.findOne = (req, res) => {
  const id = req.params.id;
  Surah.findByPk(id, {
    include: [{ model: db.ayat, as: "ayats" }],
  })
    .then(data => {
      if (!data) return res.status(404).send({ message: `Surah not found with id=${id}` });
      res.send(data);
    })
    .catch(err => res.status(500).send({ message: err.message || `Error retrieving Surah with id=${id}` }));
};

// PUT /api/surahs/:id
exports.update = (req, res) => {
  const id = req.params.id;
  Surah.update(req.body, { where: { id_sura: id } })
    .then(([num]) => {
      if (num === 1) {
        res.send({ message: "Surah was updated successfully." });
      } else {
        res.status(404).send({ message: `Cannot update Surah with id=${id}. Not found or data is same.` });
      }
    })
    .catch(err => res.status(500).send({ message: err.message || `Error updating Surah with id=${id}` }));
};

// DELETE /api/surahs/:id
exports.delete = (req, res) => {
  const id = req.params.id;
  Surah.destroy({ where: { id_sura: id } })
    .then(num => {
      if (num === 1) {
        res.send({ message: "Surah was deleted successfully!" });
      } else {
        res.status(404).send({ message: `Cannot delete Surah with id=${id}. Not found.` });
      }
    })
    .catch(err => res.status(500).send({ message: err.message || `Could not delete Surah with id=${id}` }));
};
