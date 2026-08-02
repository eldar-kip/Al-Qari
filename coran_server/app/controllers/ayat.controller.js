const db = require("../models");
const Ayat = db.ayat;

// POST /api/ayats
exports.create = (req, res) => {
  const { number_ayata, id_sura, arab_text, transcription, translation, time_start, time_end } = req.body;

  if (!number_ayata || !id_sura) {
    return res.status(400).send({ message: "number_ayata and id_sura cannot be empty!" });
  }

  Ayat.create({ number_ayata, id_sura, arab_text, transcription, translation, time_start, time_end })
    .then(data => res.status(201).send(data))
    .catch(err => res.status(500).send({ message: err.message || "Error creating Ayat." }));
};

// GET /api/ayats  (опционально фильтр по суре: ?suraId=1)
exports.findAll = (req, res) => {
  const condition = req.query.suraId ? { id_sura: req.query.suraId } : {};

  Ayat.findAll({
    where: condition,
    include: [{ model: db.surah, as: "surah" }],
    order: [["number_ayata", "ASC"]],
  })
    .then(data => res.send(data))
    .catch(err => res.status(500).send({ message: err.message || "Error retrieving ayats." }));
};

// GET /api/ayats/:id
exports.findOne = (req, res) => {
  const id = req.params.id;
  Ayat.findByPk(id, {
    include: [{ model: db.surah, as: "surah" }],
  })
    .then(data => {
      if (!data) return res.status(404).send({ message: `Ayat not found with id=${id}` });
      res.send(data);
    })
    .catch(err => res.status(500).send({ message: err.message || `Error retrieving Ayat with id=${id}` }));
};

// PUT /api/ayats/:id
exports.update = (req, res) => {
  const id = req.params.id;
  Ayat.update(req.body, { where: { id_ayats: id } })
    .then(([num]) => {
      if (num === 1) {
        res.send({ message: "Ayat was updated successfully." });
      } else {
        res.status(404).send({ message: `Cannot update Ayat with id=${id}. Not found or data is same.` });
      }
    })
    .catch(err => res.status(500).send({ message: err.message || `Error updating Ayat with id=${id}` }));
};

// DELETE /api/ayats/:id
exports.delete = (req, res) => {
  const id = req.params.id;
  Ayat.destroy({ where: { id_ayats: id } })
    .then(num => {
      if (num === 1) {
        res.send({ message: "Ayat was deleted successfully!" });
      } else {
        res.status(404).send({ message: `Cannot delete Ayat with id=${id}. Not found.` });
      }
    })
    .catch(err => res.status(500).send({ message: err.message || `Could not delete Ayat with id=${id}` }));
};
