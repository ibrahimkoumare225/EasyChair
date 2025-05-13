-- Insertion des utilisateurs
--INSERT INTO users (first_name, last_name, email, pseudo, password, birth_date)
--VALUES
--   ('John', 'Doe', 'john@doe.com', 'toto', 'password123', DATE '2000-01-01'),
--  ('Jane', 'Doe', 'jane@doe.com', 'tata', 'password123', DATE '2001-01-01'),
--  ('Alban', 'Cousin', 'alban@cousin.com', 'Banban', 'alban123', DATE '2003-04-12'),
--    ('Jeremie', 'Pennec', 'jeremie@pennec.com', 'Jeje', 'jeremie123', DATE '1980-12-12'),
--  ('Ibrahim', 'Koumare', 'ibrahim@koumare.com', 'ibrah', 'ibrahim123', DATE '2000-12-12');

-- Insertion des conférences
INSERT INTO conferences (title, description, creation_date, commitee_assignment_date, abstract_sub_date, sub_assignment_date, concrete_sub_date, evaluation_date, final_sub_date, end_date, phase)
VALUES
    (
        'Sample Conference',
        'This is a sample conference description.',
        CURRENT_DATE,
        DATEADD('DAY', 10, CURRENT_DATE),
        DATEADD('DAY', 20, CURRENT_DATE),
        DATEADD('DAY', 30, CURRENT_DATE),
        DATEADD('DAY', 40, CURRENT_DATE),
        DATEADD('DAY', 50, CURRENT_DATE),
        DATEADD('DAY', 60, CURRENT_DATE),
        DATEADD('DAY', 70, CURRENT_DATE),
        'INITIALIZATION'
    ),
    (
        'conf1',
        'Techno web.',
        CURRENT_DATE,
        DATEADD('DAY', 10, CURRENT_DATE),
        DATEADD('DAY', 20, CURRENT_DATE),
        DATEADD('DAY', 30, CURRENT_DATE),
        DATEADD('DAY', 40, CURRENT_DATE),
        DATEADD('DAY', 50, CURRENT_DATE),
        DATEADD('DAY', 60, CURRENT_DATE),
        DATEADD('DAY', 70, CURRENT_DATE),
        'ABSTRACT_SUBMISSION'
    );


INSERT INTO roles (user_id, role_type) VALUES (1, 'ORGANIZER');
INSERT INTO organizers (id, conference_id) VALUES (1, 1);
INSERT INTO roles (user_id, role_type) VALUES (2, 'ORGANIZER');
INSERT INTO organizers (id, conference_id) VALUES (2, 2);

-- Reviewers
INSERT INTO roles (user_id, role_type) VALUES (2, 'REVIEWER');
INSERT INTO reviewers (id, conference_id) VALUES (2, 1);
INSERT INTO roles (user_id, role_type) VALUES (1, 'REVIEWER');
INSERT INTO reviewers (id, conference_id) VALUES (3, 1);
-- Authors
INSERT INTO roles (user_id, role_type) VALUES (3, 'AUTHOR'); -- Alban
INSERT INTO authors (id, conference_id) VALUES (4, 1);
INSERT INTO roles (user_id, role_type) VALUES (5, 'AUTHOR'); -- Ibrahim
INSERT INTO authors (id, conference_id) VALUES ( 5,1);
SELECT * FROM authors;

-- Submissions (3 articles)
INSERT INTO submissions (title, creation_date, status, abstract_sub, conference_id)
VALUES
    (
        'Systèmes intelligents pour la reconnaissance de motifs',
        CURRENT_TIMESTAMP,
        'PROGRESS',
        'Cet article traite de la reconnaissance de motifs dans les systèmes d''intelligence artificielle.',
        1
    ),
    (
        'Exploration de la vie privée dans les réseaux décentralisés',
        CURRENT_TIMESTAMP,
        'PROGRESS',
        'Ce résumé aborde les défis liés à la protection de la vie privée dans les systèmes décentralisés.',
        1
    ),
    (
        'Apprentissage profond pour la prévision climatique',
        CURRENT_TIMESTAMP,
        'PROGRESS',
        'Ce travail utilise des modèles d''apprentissage profond pour prédire les tendances climatiques.',
        1
    );

-- Lien auteur-soumissions (table de jointure author_submission)
INSERT INTO author_submission (author_id, submission_id)
VALUES
    (4, 1),  -- Alban → submission 1
    (5, 3);          -- Ibrahim → submission 3


-- Evaluation pour submission 1
INSERT INTO evaluations ( submission_id)
VALUES ( 1);

-- Post lié à l'évaluation précédente (evaluation_id = 1), reviewer 1 (Jane Doe = 2)
INSERT INTO posts (evaluation_id, reviewer_id, date, body)
VALUES (
           1,
           3,
           CURRENT_TIMESTAMP,
           'Le corps du message de post de l''évaluation. Ce post contient des informations importantes concernant l''évaluation.'
       );

INSERT INTO posts (evaluation_id, reviewer_id, date, body)
VALUES (
           1,
           2,
           CURRENT_TIMESTAMP,
           'Le corps du message de post de l''évaluation. Ce post contient des informations importantes concernant l''évaluation.'
       );




-- Notification pour John Doe sur conférence 1
INSERT INTO notifications (message, sending_date, user_id, conference_id)
VALUES (
           'La conférence est ouverte',
           CURRENT_TIMESTAMP,
           1,
           1
       );

-- Alerte liée à submission 1 par reviewer 2 (Jane) et organisateur (John)
INSERT INTO alerts (body, type, submission_id, reviewer_id, organizer_id)
VALUES (
           'Le contenu de votre soumission n''est pas approprié',
           'CONTENT_REPORT',
           1,
           3,
           1
       );