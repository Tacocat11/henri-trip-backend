-- Henri Trip - données de démonstration (MariaDB)
-- Prérequis : créer la base henri_trip et démarrer Spring Boot une fois
-- pour que Hibernate crée les tables (ddl-auto=update).
-- Importer ensuite ce script dans la base henri_trip.
-- Réexécutable : ne duplique pas les enregistrements de démonstration.
-- À utiliser UNIQUEMENT dans un environnement local de démonstration.
-- Comptes : admin@henritrip.fr / AdminDemo2026!
--           alice.demo@henritrip.fr / UserDemo2026!
--           thomas.demo@henritrip.fr / UserDemo2026!

USE `henri_trip`;
START TRANSACTION;

-- Les hash BCrypt sont compatibles avec Spring Security BCryptPasswordEncoder.
-- On ne modifie pas le mot de passe d'un compte déjà existant.
INSERT INTO users (email, password, role)
SELECT 'admin@henritrip.fr', '$2y$10$pQeIdafjPg6a7D59HptYXehW4AaElE7a4G6xOLpTs.C91nwyoi8oC', 'ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'admin@henritrip.fr');
INSERT INTO users (email, password, role)
SELECT 'alice.demo@henritrip.fr', '$2y$10$AKTEdNeczPqaStRCJssW5.mq36IKtKH7e6p9nzX.NE3S/kY3VCbCy', 'USER'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'alice.demo@henritrip.fr');
INSERT INTO users (email, password, role)
SELECT 'thomas.demo@henritrip.fr', '$2y$10$AKTEdNeczPqaStRCJssW5.mq36IKtKH7e6p9nzX.NE3S/kY3VCbCy', 'USER'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'thomas.demo@henritrip.fr');

-- Les intitulés préfixés [DEMO] servent d'identifiants logiques au script.
INSERT INTO guides (title, description, number_of_days)
SELECT '[DEMO] Week-end à Toulouse', 'Deux jours de découverte du patrimoine et des espaces verts de Toulouse.', 2
WHERE NOT EXISTS (SELECT 1 FROM guides WHERE title = '[DEMO] Week-end à Toulouse');
INSERT INTO guides (title, description, number_of_days)
SELECT '[DEMO] Découverte du Tarn', 'Deux jours autour d’Albi et de Cordes-sur-Ciel.', 2
WHERE NOT EXISTS (SELECT 1 FROM guides WHERE title = '[DEMO] Découverte du Tarn');

INSERT INTO activities (title, category, description, address, opening_hours, phone, website)
SELECT '[DEMO] Basilique Saint-Sernin', 'ACTIVITY', 'Découverte d’un monument emblématique de Toulouse.', 'Place Saint-Sernin, Toulouse', 'Consulter le site officiel', NULL, NULL
WHERE NOT EXISTS (SELECT 1 FROM activities WHERE title = '[DEMO] Basilique Saint-Sernin');
INSERT INTO activities (title, category, description, address, opening_hours, phone, website)
SELECT '[DEMO] Musée des Augustins', 'MUSEUM', 'Découverte des collections du musée des beaux-arts.', '21 rue de Metz, Toulouse', 'Consulter le site officiel', NULL, NULL
WHERE NOT EXISTS (SELECT 1 FROM activities WHERE title = '[DEMO] Musée des Augustins');
INSERT INTO activities (title, category, description, address, opening_hours, phone, website)
SELECT '[DEMO] Jardin des Plantes', 'PARK', 'Promenade dans un jardin public toulousain.', 'Allée Jules Guesde, Toulouse', 'Horaires variables selon la saison', NULL, NULL
WHERE NOT EXISTS (SELECT 1 FROM activities WHERE title = '[DEMO] Jardin des Plantes');
INSERT INTO activities (title, category, description, address, opening_hours, phone, website)
SELECT '[DEMO] Cité de l’espace', 'ACTIVITY', 'Visite consacrée à l’exploration spatiale.', 'Avenue Jean Gonord, Toulouse', 'Consulter le calendrier officiel', NULL, NULL
WHERE NOT EXISTS (SELECT 1 FROM activities WHERE title = '[DEMO] Cité de l’espace');
INSERT INTO activities (title, category, description, address, opening_hours, phone, website)
SELECT '[DEMO] Musée Toulouse-Lautrec', 'MUSEUM', 'Découverte de collections artistiques à Albi.', 'Palais de la Berbie, Albi', 'Consulter le site officiel', NULL, NULL
WHERE NOT EXISTS (SELECT 1 FROM activities WHERE title = '[DEMO] Musée Toulouse-Lautrec');
INSERT INTO activities (title, category, description, address, opening_hours, phone, website)
SELECT '[DEMO] Cathédrale Sainte-Cécile', 'ACTIVITY', 'Visite du patrimoine architectural d’Albi.', 'Place Sainte-Cécile, Albi', 'Consulter les horaires de visite', NULL, NULL
WHERE NOT EXISTS (SELECT 1 FROM activities WHERE title = '[DEMO] Cathédrale Sainte-Cécile');
INSERT INTO activities (title, category, description, address, opening_hours, phone, website)
SELECT '[DEMO] Château de Mauriac', 'CASTLE', 'Découverte d’un château du Tarn.', 'Mauriac, Senouillac', 'Visites selon disponibilités', NULL, NULL
WHERE NOT EXISTS (SELECT 1 FROM activities WHERE title = '[DEMO] Château de Mauriac');
INSERT INTO activities (title, category, description, address, opening_hours, phone, website)
SELECT '[DEMO] Cordes-sur-Ciel', 'ACTIVITY', 'Promenade dans les ruelles de la cité médiévale.', 'Cordes-sur-Ciel, Tarn', 'Accès libre aux espaces publics', NULL, NULL
WHERE NOT EXISTS (SELECT 1 FROM activities WHERE title = '[DEMO] Cordes-sur-Ciel');

-- Programmation : l'ordre est unique pour chaque jour de chaque guide.
INSERT INTO guide_activities (guide_id, activity_id, day_number, visit_order)
SELECT g.id, a.id, x.day_number, x.visit_order
FROM (
 SELECT '[DEMO] Week-end à Toulouse' AS guide_title, '[DEMO] Basilique Saint-Sernin' AS activity_title, 1 AS day_number, 1 AS visit_order
 UNION ALL SELECT '[DEMO] Week-end à Toulouse', '[DEMO] Musée des Augustins', 1, 2
 UNION ALL SELECT '[DEMO] Week-end à Toulouse', '[DEMO] Jardin des Plantes', 2, 1
 UNION ALL SELECT '[DEMO] Week-end à Toulouse', '[DEMO] Cité de l’espace', 2, 2
 UNION ALL SELECT '[DEMO] Découverte du Tarn', '[DEMO] Cathédrale Sainte-Cécile', 1, 1
 UNION ALL SELECT '[DEMO] Découverte du Tarn', '[DEMO] Musée Toulouse-Lautrec', 1, 2
 UNION ALL SELECT '[DEMO] Découverte du Tarn', '[DEMO] Château de Mauriac', 2, 1
 UNION ALL SELECT '[DEMO] Découverte du Tarn', '[DEMO] Cordes-sur-Ciel', 2, 2
) AS x
JOIN guides g
  ON CONVERT(g.title USING utf8mb4) COLLATE utf8mb4_unicode_ci
   = x.guide_title COLLATE utf8mb4_unicode_ci

JOIN activities a
  ON CONVERT(a.title USING utf8mb4) COLLATE utf8mb4_unicode_ci
   = x.activity_title COLLATE utf8mb4_unicode_ci
WHERE NOT EXISTS (
 SELECT 1 FROM guide_activities ga
 WHERE ga.guide_id = g.id AND ga.day_number = x.day_number AND ga.visit_order = x.visit_order
);

INSERT INTO guide_audiences (guide_id, audience)
SELECT g.id, 'FRIENDS' FROM guides g WHERE g.title = '[DEMO] Week-end à Toulouse'
AND NOT EXISTS (SELECT 1 FROM guide_audiences ga WHERE ga.guide_id = g.id AND ga.audience = 'FRIENDS');
INSERT INTO guide_audiences (guide_id, audience)
SELECT g.id, 'FAMILY' FROM guides g WHERE g.title = '[DEMO] Découverte du Tarn'
AND NOT EXISTS (SELECT 1 FROM guide_audiences ga WHERE ga.guide_id = g.id AND ga.audience = 'FAMILY');
INSERT INTO guide_mobilities (guide_id, mobility)
SELECT g.id, 'WALKING' FROM guides g WHERE g.title = '[DEMO] Week-end à Toulouse'
AND NOT EXISTS (SELECT 1 FROM guide_mobilities gm WHERE gm.guide_id = g.id AND gm.mobility = 'WALKING');
INSERT INTO guide_mobilities (guide_id, mobility)
SELECT g.id, 'CAR' FROM guides g WHERE g.title = '[DEMO] Découverte du Tarn'
AND NOT EXISTS (SELECT 1 FROM guide_mobilities gm WHERE gm.guide_id = g.id AND gm.mobility = 'CAR');
INSERT INTO guide_seasons (guide_id, season)
SELECT g.id, 'SPRING' FROM guides g WHERE g.title = '[DEMO] Week-end à Toulouse'
AND NOT EXISTS (SELECT 1 FROM guide_seasons gs WHERE gs.guide_id = g.id AND gs.season = 'SPRING');
INSERT INTO guide_seasons (guide_id, season)
SELECT g.id, 'AUTUMN' FROM guides g WHERE g.title = '[DEMO] Découverte du Tarn'
AND NOT EXISTS (SELECT 1 FROM guide_seasons gs WHERE gs.guide_id = g.id AND gs.season = 'AUTUMN');

-- Alice voit les deux guides ; Thomas voit uniquement le guide du Tarn.
INSERT INTO guide_invitations (guide_id, user_id)
SELECT g.id, u.id FROM guides g JOIN users u ON u.email = 'alice.demo@henritrip.fr'
WHERE g.title = '[DEMO] Week-end à Toulouse'
AND NOT EXISTS (SELECT 1 FROM guide_invitations gi WHERE gi.guide_id = g.id AND gi.user_id = u.id);
INSERT INTO guide_invitations (guide_id, user_id)
SELECT g.id, u.id FROM guides g JOIN users u ON u.email = 'alice.demo@henritrip.fr'
WHERE g.title = '[DEMO] Découverte du Tarn'
AND NOT EXISTS (SELECT 1 FROM guide_invitations gi WHERE gi.guide_id = g.id AND gi.user_id = u.id);
INSERT INTO guide_invitations (guide_id, user_id)
SELECT g.id, u.id FROM guides g JOIN users u ON u.email = 'thomas.demo@henritrip.fr'
WHERE g.title = '[DEMO] Découverte du Tarn'
AND NOT EXISTS (SELECT 1 FROM guide_invitations gi WHERE gi.guide_id = g.id AND gi.user_id = u.id);

COMMIT;
