module.exports = app => {
  const ayat = require("../controllers/ayat.controller.js");
  const router = require("express").Router();

  /**
   * @swagger
   * tags:
   *   name: Ayats
   *   description: Quran Ayats (verses)
   */

  /**
   * @swagger
   * /api/ayats:
   *   post:
   *     summary: Create a new ayat
   *     tags: [Ayats]
   *     requestBody:
   *       required: true
   *       content:
   *         application/json:
   *           schema:
   *             type: object
   *             required: [number_ayata, id_sura]
   *             properties:
   *               number_ayata:  { type: integer }
   *               id_sura:       { type: integer }
   *               arab_text:     { type: string }
   *               transcription: { type: string }
   *               translation:   { type: string }
   *               time_start:    { type: string }
   *               time_end:      { type: string }
   *     responses:
   *       201: { description: Ayat created }
   *       400: { description: Bad request }
   */
  router.post("/", ayat.create);

  /**
   * @swagger
   * /api/ayats:
   *   get:
   *     summary: Get all ayats (optional ?suraId=N filter)
   *     tags: [Ayats]
   *     parameters:
   *       - in: query
   *         name: suraId
   *         schema: { type: integer }
   *         description: Filter by Surah ID
   *     responses:
   *       200: { description: List of ayats }
   */
  router.get("/", ayat.findAll);

  /**
   * @swagger
   * /api/ayats/{id}:
   *   get:
   *     summary: Get ayat by ID
   *     tags: [Ayats]
   *     parameters:
   *       - in: path
   *         name: id
   *         required: true
   *         schema: { type: integer }
   *     responses:
   *       200: { description: Ayat object }
   *       404: { description: Ayat not found }
   */
  router.get("/:id", ayat.findOne);

  /**
   * @swagger
   * /api/ayats/{id}:
   *   put:
   *     summary: Update ayat by ID
   *     tags: [Ayats]
   *     parameters:
   *       - in: path
   *         name: id
   *         required: true
   *         schema: { type: integer }
   *     responses:
   *       200: { description: Updated }
   *       404: { description: Not found }
   */
  router.put("/:id", ayat.update);

  /**
   * @swagger
   * /api/ayats/{id}:
   *   delete:
   *     summary: Delete ayat by ID
   *     tags: [Ayats]
   *     parameters:
   *       - in: path
   *         name: id
   *         required: true
   *         schema: { type: integer }
   *     responses:
   *       200: { description: Deleted }
   *       404: { description: Not found }
   */
  router.delete("/:id", ayat.delete);

  app.use("/api/ayats", router);
};
