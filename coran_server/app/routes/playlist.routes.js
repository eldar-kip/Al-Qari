module.exports = app => {
  const playlist = require("../controllers/playlist.controller.js");
  const router = require("express").Router();

  /**
   * @swagger
   * tags:
   *   name: PlayLists
   *   description: User playlists of ayats
   */

  /**
   * @swagger
   * /api/playlists:
   *   post:
   *     summary: Create a new playlist
   *     tags: [PlayLists]
   *     requestBody:
   *       required: true
   *       content:
   *         application/json:
   *           schema:
   *             type: object
   *             required: [name_play_list]
   *             properties:
   *               name_play_list: { type: string }
   *     responses:
   *       201: { description: Playlist created }
   *       400: { description: Bad request }
   */
  router.post("/", playlist.create);

  /**
   * @swagger
   * /api/playlists:
   *   get:
   *     summary: Get all playlists (with ayats and users)
   *     tags: [PlayLists]
   *     responses:
   *       200: { description: List of playlists }
   */
  router.get("/", playlist.findAll);

  /**
   * @swagger
   * /api/playlists/{id}:
   *   get:
   *     summary: Get playlist by ID (full detail)
   *     tags: [PlayLists]
   *     parameters:
   *       - in: path
   *         name: id
   *         required: true
   *         schema: { type: integer }
   *     responses:
   *       200: { description: Playlist object }
   *       404: { description: Not found }
   */
  router.get("/:id", playlist.findOne);

  /**
   * @swagger
   * /api/playlists/{id}:
   *   put:
   *     summary: Update playlist by ID
   *     tags: [PlayLists]
   *     parameters:
   *       - in: path
   *         name: id
   *         required: true
   *         schema: { type: integer }
   *     responses:
   *       200: { description: Updated }
   *       404: { description: Not found }
   */
  router.put("/:id", playlist.update);

  /**
   * @swagger
   * /api/playlists/{id}:
   *   delete:
   *     summary: Delete playlist by ID
   *     tags: [PlayLists]
   *     parameters:
   *       - in: path
   *         name: id
   *         required: true
   *         schema: { type: integer }
   *     responses:
   *       200: { description: Deleted }
   *       404: { description: Not found }
   */
  router.delete("/:id", playlist.delete);

  /**
   * @swagger
   * /api/playlists/{id}/ayats:
   *   post:
   *     summary: Add an ayat to a playlist
   *     tags: [PlayLists]
   *     parameters:
   *       - in: path
   *         name: id
   *         required: true
   *         schema: { type: integer }
   *     requestBody:
   *       required: true
   *       content:
   *         application/json:
   *           schema:
   *             type: object
   *             required: [id_ayats]
   *             properties:
   *               id_ayats: { type: integer }
   *     responses:
   *       201: { description: Ayat added }
   *       400: { description: Bad request }
   */
  router.post("/:id/ayats", playlist.addAyat);

  /**
   * @swagger
   * /api/playlists/{id}/ayats/{ayatId}:
   *   delete:
   *     summary: Remove an ayat from a playlist
   *     tags: [PlayLists]
   *     parameters:
   *       - in: path
   *         name: id
   *         required: true
   *         schema: { type: integer }
   *       - in: path
   *         name: ayatId
   *         required: true
   *         schema: { type: integer }
   *     responses:
   *       200: { description: Ayat removed }
   *       404: { description: Not found }
   */
  router.delete("/:id/ayats/:ayatId", playlist.removeAyat);

  app.use("/api/playlists", router);
};
