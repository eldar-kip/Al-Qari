module.exports = app => {
  const surah = require("../controllers/surah.controller.js");
  const router = require("express").Router();

  /**
   * @swagger
   * tags:
   *   name: Surahs
   *   description: Quran Surahs
   */

  /**
   * @swagger
   * /api/surahs:
   *   post:
   *     summary: Create a new surah
   *     tags: [Surahs]
   *     requestBody:
   *       required: true
   *       content:
   *         application/json:
   *           schema:
   *             type: object
   *             required: [number_sura, sura_name]
   *             properties:
   *               number_sura:       { type: integer }
   *               audio_link:        { type: string }
   *               sura_name:         { type: string }
   *               transcription_name:{ type: string }
   *               translation_name:  { type: string }
   *     responses:
   *       201: { description: Surah created }
   *       400: { description: Bad request }
   */
  router.post("/", surah.create);

  /**
   * @swagger
   * /api/surahs:
   *   get:
   *     summary: Get all surahs
   *     tags: [Surahs]
   *     responses:
   *       200: { description: List of surahs }
   */
  router.get("/", surah.findAll);

  /**
   * @swagger
   * /api/surahs/{id}:
   *   get:
   *     summary: Get surah by ID (includes its ayats)
   *     tags: [Surahs]
   *     parameters:
   *       - in: path
   *         name: id
   *         required: true
   *         schema: { type: integer }
   *     responses:
   *       200: { description: Surah object with ayats }
   *       404: { description: Surah not found }
   */
  router.get("/:id", surah.findOne);

  /**
   * @swagger
   * /api/surahs/{id}:
   *   put:
   *     summary: Update surah by ID
   *     tags: [Surahs]
   *     parameters:
   *       - in: path
   *         name: id
   *         required: true
   *         schema: { type: integer }
   *     responses:
   *       200: { description: Updated }
   *       404: { description: Not found }
   */
  router.put("/:id", surah.update);

  /**
   * @swagger
   * /api/surahs/{id}:
   *   delete:
   *     summary: Delete surah by ID
   *     tags: [Surahs]
   *     parameters:
   *       - in: path
   *         name: id
   *         required: true
   *         schema: { type: integer }
   *     responses:
   *       200: { description: Deleted }
   *       404: { description: Not found }
   */
  router.delete("/:id", surah.delete);

  app.use("/api/surahs", router);
};
