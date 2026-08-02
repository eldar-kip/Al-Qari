module.exports = app => {
  const user = require("../controllers/user.controller.js");
  const router = require("express").Router();

  /**
   * @swagger
   * tags:
   *   name: Users
   *   description: User management
   */

  /**
   * @swagger
   * /api/users:
   *   post:
   *     summary: Create a new user
   *     tags: [Users]
   *     requestBody:
   *       required: true
   *       content:
   *         application/json:
   *           schema:
   *             type: object
   *             required: [username, password]
   *             properties:
   *               username: { type: string }
   *               password: { type: string }
   *               status:   { type: string }
   *     responses:
   *       201: { description: User created }
   *       400: { description: Bad request }
   */
  router.post("/", user.create);

  /**
   * @swagger
   * /api/users:
   *   get:
   *     summary: Get all users
   *     tags: [Users]
   *     responses:
   *       200: { description: List of users }
   */
  router.get("/", user.findAll);

  /**
   * @swagger
   * /api/users/{id}:
   *   get:
   *     summary: Get user by ID
   *     tags: [Users]
   *     parameters:
   *       - in: path
   *         name: id
   *         required: true
   *         schema: { type: integer }
   *     responses:
   *       200: { description: User object }
   *       404: { description: User not found }
   */
  router.get("/:id", user.findOne);

  /**
   * @swagger
   * /api/users/{id}:
   *   put:
   *     summary: Update user by ID
   *     tags: [Users]
   *     parameters:
   *       - in: path
   *         name: id
   *         required: true
   *         schema: { type: integer }
   *     requestBody:
   *       content:
   *         application/json:
   *           schema:
   *             type: object
   *             properties:
   *               username: { type: string }
   *               status:   { type: string }
   *     responses:
   *       200: { description: Updated }
   *       404: { description: Not found }
   */
  router.put("/:id", user.update);

  /**
   * @swagger
   * /api/users/{id}:
   *   delete:
   *     summary: Delete user by ID
   *     tags: [Users]
   *     parameters:
   *       - in: path
   *         name: id
   *         required: true
   *         schema: { type: integer }
   *     responses:
   *       200: { description: Deleted }
   *       404: { description: Not found }
   */
  router.delete("/:id", user.delete);

  app.use("/api/users", router);
};