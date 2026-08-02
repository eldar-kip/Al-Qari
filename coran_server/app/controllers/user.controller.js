const db = require("../models");
const User = db.user;

// POST /api/users
exports.create = (req, res) => {
  const { username, password, status } = req.body;

  if (!username || !password) {
    return res.status(400).send({ message: "Username and password cannot be empty!" });
  }

  User.create({ username, password, status })
    .then(data => res.status(201).send(data))
    .catch(err => res.status(500).send({ message: err.message || "Error creating User." }));
};

// GET /api/users
exports.findAll = (req, res) => {
  User.findAll({ attributes: { exclude: ["password"] } })
    .then(data => res.send(data))
    .catch(err => res.status(500).send({ message: err.message || "Error retrieving users." }));
};

// GET /api/users/:id
exports.findOne = (req, res) => {
  const id = req.params.id;
  User.findByPk(id, { attributes: { exclude: ["password"] } })
    .then(data => {
      if (!data) return res.status(404).send({ message: `User not found with id=${id}` });
      res.send(data);
    })
    .catch(err => res.status(500).send({ message: err.message || `Error retrieving User with id=${id}` }));
};

// PUT /api/users/:id
exports.update = (req, res) => {
  const id = req.params.id;
  User.update(req.body, { where: { Id_user: id } })
    .then(([num]) => {
      if (num === 1) {
        res.send({ message: "User was updated successfully." });
      } else {
        res.status(404).send({ message: `Cannot update User with id=${id}. Not found or data is same.` });
      }
    })
    .catch(err => res.status(500).send({ message: err.message || `Error updating User with id=${id}` }));
};

// DELETE /api/users/:id
exports.delete = (req, res) => {
  const id = req.params.id;
  User.destroy({ where: { Id_user: id } })
    .then(num => {
      if (num === 1) {
        res.send({ message: "User was deleted successfully!" });
      } else {
        res.status(404).send({ message: `Cannot delete User with id=${id}. Not found.` });
      }
    })
    .catch(err => res.status(500).send({ message: err.message || `Could not delete User with id=${id}` }));
};
