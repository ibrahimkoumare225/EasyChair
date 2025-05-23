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
         CURRENT_DATE,
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
    ),
    (   'confConcrete',
        'Techno web.',
        CURRENT_DATE,
        DATEADD('DAY', 10, CURRENT_DATE),
        DATEADD('DAY', 20, CURRENT_DATE),
        DATEADD('DAY', 30, CURRENT_DATE),
        DATEADD('DAY', 40, CURRENT_DATE),
        DATEADD('DAY', 50, CURRENT_DATE),
        DATEADD('DAY', 60, CURRENT_DATE),
        DATEADD('DAY', 70, CURRENT_DATE),
        'CONCRETE_SUBMISSION'
    )
;
INSERT INTO conferences (title, description, creation_date, commitee_assignment_date, abstract_sub_date, sub_assignment_date, concrete_sub_date, evaluation_date, final_sub_date, end_date, phase)
VALUES
    ('Conference Initialization', 'Conference in Initialization phase.', CURRENT_DATE, CURRENT_DATE, DATEADD('DAY', 20, CURRENT_DATE), DATEADD('DAY', 30, CURRENT_DATE), DATEADD('DAY', 40, CURRENT_DATE), DATEADD('DAY', 50, CURRENT_DATE), DATEADD('DAY', 60, CURRENT_DATE), DATEADD('DAY', 70, CURRENT_DATE), 'INITIALIZATION'),
    ('Conference Commitee Assignment', 'Conference in Commitee Assignment phase.', DATEADD('DAY', -70, CURRENT_DATE), DATEADD('DAY', -60, CURRENT_DATE), DATEADD('DAY', -50, CURRENT_DATE), DATEADD('DAY', -40, CURRENT_DATE), DATEADD('DAY', -30, CURRENT_DATE), DATEADD('DAY', -20, CURRENT_DATE), DATEADD('DAY', -10, CURRENT_DATE), CURRENT_DATE, 'COMMITEE_ASSIGNMENT'),
    ('Conference Abstract Submission', 'Conference in Abstract Submission phase.', DATEADD('DAY', -70, CURRENT_DATE), DATEADD('DAY', -60, CURRENT_DATE), CURRENT_DATE, DATEADD('DAY', 10, CURRENT_DATE), DATEADD('DAY', 20, CURRENT_DATE), DATEADD('DAY', 30, CURRENT_DATE), DATEADD('DAY', 40, CURRENT_DATE), DATEADD('DAY', 50, CURRENT_DATE), 'ABSTRACT_SUBMISSION'),
    ('Conference Submission Assignment', 'Conference in Submission Assignment phase.', DATEADD('DAY', -70, CURRENT_DATE), DATEADD('DAY', -60, CURRENT_DATE), DATEADD('DAY', -50, CURRENT_DATE), CURRENT_DATE, DATEADD('DAY', 10, CURRENT_DATE), DATEADD('DAY', 20, CURRENT_DATE), DATEADD('DAY', 30, CURRENT_DATE), DATEADD('DAY', 40, CURRENT_DATE), 'SUBMISSION_ASSIGNMENT'),
    ('Conference Concrete Submission', 'Conference in Concrete Submission phase.', DATEADD('DAY', -70, CURRENT_DATE), DATEADD('DAY', -60, CURRENT_DATE), DATEADD('DAY', -50, CURRENT_DATE), DATEADD('DAY', -40, CURRENT_DATE), CURRENT_DATE, DATEADD('DAY', 10, CURRENT_DATE), DATEADD('DAY', 20, CURRENT_DATE), DATEADD('DAY', 30, CURRENT_DATE), 'CONCRETE_SUBMISSION'),
    ('Conference Evaluation', 'Conference in Evaluation phase.', DATEADD('DAY', -70, CURRENT_DATE), DATEADD('DAY', -60, CURRENT_DATE), DATEADD('DAY', -50, CURRENT_DATE), DATEADD('DAY', -40, CURRENT_DATE), DATEADD('DAY', -30, CURRENT_DATE), CURRENT_DATE, DATEADD('DAY', 10, CURRENT_DATE), DATEADD('DAY', 20, CURRENT_DATE), 'EVALUATION'),
    ('Conference Final Submission', 'Conference in Final Submission phase.', DATEADD('DAY', -70, CURRENT_DATE), DATEADD('DAY', -60, CURRENT_DATE), DATEADD('DAY', -50, CURRENT_DATE), DATEADD('DAY', -40, CURRENT_DATE), DATEADD('DAY', -30, CURRENT_DATE), DATEADD('DAY', -20, CURRENT_DATE), CURRENT_DATE, DATEADD('DAY', 10, CURRENT_DATE), 'FINAL_SUBMISSION'),
    ('Conference Validation', 'Conference in Validation phase.', DATEADD('DAY', -70, CURRENT_DATE), DATEADD('DAY', -60, CURRENT_DATE), DATEADD('DAY', -50, CURRENT_DATE), DATEADD('DAY', -40, CURRENT_DATE), DATEADD('DAY', -30, CURRENT_DATE), DATEADD('DAY', -20, CURRENT_DATE), DATEADD('DAY', -10, CURRENT_DATE), CURRENT_DATE, 'VALIDATION'),
    ('Conference Closed', 'Conference in Closed phase.', DATEADD('DAY', -70, CURRENT_DATE), DATEADD('DAY', -60, CURRENT_DATE), DATEADD('DAY', -50, CURRENT_DATE), DATEADD('DAY', -40, CURRENT_DATE), DATEADD('DAY', -30, CURRENT_DATE), DATEADD('DAY', -20, CURRENT_DATE), DATEADD('DAY', -10, CURRENT_DATE), CURRENT_DATE, 'CLOSED');


-- ORGANIZERS
-- John Doe (user_id=1) devient ORGANIZER de la conférence 1
INSERT INTO roles (user_id, role_type, conference_id) VALUES (1, 'ORGANIZER', 1);
-- Son ID d'organisateur est 1
INSERT INTO organizers (id) VALUES (1);

-- Jane Doe (user_id=2) devient ORGANIZER de la conférence 2
INSERT INTO roles (user_id, role_type, conference_id) VALUES (2, 'ORGANIZER', 2);
-- Son ID d'organisateur est 2
INSERT INTO organizers (id) VALUES (2);

-- REVIEWERS
-- Jane Doe (user_id=2) devient également REVIEWER de la conférence 1
INSERT INTO roles (user_id, role_type, conference_id) VALUES (2, 'REVIEWER', 1);
-- Son ID de reviewer est 3
INSERT INTO reviewers (id) VALUES (3);

-- John Doe (user_id=1) devient également REVIEWER de la conférence 1
INSERT INTO roles (user_id, role_type, conference_id) VALUES (1, 'REVIEWER', 1);
-- Son ID de reviewer est 4
INSERT INTO reviewers (id) VALUES (4);

-- AUTHORS
-- Alban Cousin (user_id=3) devient AUTHOR de la conférence 1
INSERT INTO roles (user_id, role_type, conference_id) VALUES (3, 'AUTHOR', 1);
-- Son ID d'auteur est 5
INSERT INTO authors (id) VALUES (5);

-- Ibrahim Koumare (user_id=5) devient AUTHOR de la conférence 1
INSERT INTO roles (user_id, role_type, conference_id) VALUES (5, 'AUTHOR', 1);
-- Son ID d'auteur est 6
INSERT INTO authors (id) VALUES (6);

-- Jeremie Pennec (user_id=4) devient AUTHOR de la conférence 2
INSERT INTO roles (user_id, role_type, conference_id) VALUES (4, 'AUTHOR', 2);
-- Son ID d'auteur est 7
INSERT INTO authors (id) VALUES (7);

-- Jeremie Pennec (user_id=4) devient également REVIEWER de la conférence 1
INSERT INTO roles (user_id, role_type, conference_id) VALUES (4, 'REVIEWER', 1);
-- Son ID de reviewer est 8
INSERT INTO reviewers (id) VALUES (8);

-- Jeremie Pennec (user_id=4) devient également AUTHOR de la conférence 3
INSERT INTO roles (user_id, role_type, conference_id) VALUES (4, 'AUTHOR', 3);
-- Son ID d'auteur pour ce rôle est 9
INSERT INTO authors (id) VALUES (9);
-- Submissions (3 articles)
-- Authors (user_id 3)
INSERT INTO roles (user_id, role_type, conference_id) VALUES (3, 'AUTHOR', 4);
INSERT INTO authors (id) VALUES (10);
INSERT INTO roles (user_id, role_type, conference_id) VALUES (3, 'AUTHOR', 5);
INSERT INTO authors (id) VALUES (11);
INSERT INTO roles (user_id, role_type, conference_id) VALUES (3, 'AUTHOR', 6);
INSERT INTO authors (id) VALUES (12);
INSERT INTO roles (user_id, role_type, conference_id) VALUES (3, 'AUTHOR', 7);
INSERT INTO authors (id) VALUES (13);
INSERT INTO roles (user_id, role_type, conference_id) VALUES (3, 'AUTHOR', 8);
INSERT INTO authors (id) VALUES (14);
INSERT INTO roles (user_id, role_type, conference_id) VALUES (3, 'AUTHOR', 9);
INSERT INTO authors (id) VALUES (15);
INSERT INTO roles (user_id, role_type, conference_id) VALUES (3, 'AUTHOR', 10);
INSERT INTO authors (id) VALUES (16);
INSERT INTO roles (user_id, role_type, conference_id) VALUES (3, 'AUTHOR', 11);
INSERT INTO authors (id) VALUES (17);
INSERT INTO roles (user_id, role_type, conference_id) VALUES (3, 'AUTHOR', 12);
INSERT INTO authors (id) VALUES (18);

-- Reviewers (user_id 4)
INSERT INTO roles (user_id, role_type, conference_id) VALUES (4, 'REVIEWER', 4);
INSERT INTO reviewers (id) VALUES (19);
INSERT INTO roles (user_id, role_type, conference_id) VALUES (4, 'REVIEWER', 5);
INSERT INTO reviewers (id) VALUES (20);
INSERT INTO roles (user_id, role_type, conference_id) VALUES (4, 'REVIEWER', 6);
INSERT INTO reviewers (id) VALUES (21);
INSERT INTO roles (user_id, role_type, conference_id) VALUES (4, 'REVIEWER', 7);
INSERT INTO reviewers (id) VALUES (22);
INSERT INTO roles (user_id, role_type, conference_id) VALUES (4, 'REVIEWER', 8);
INSERT INTO reviewers (id) VALUES (23);
INSERT INTO roles (user_id, role_type, conference_id) VALUES (4, 'REVIEWER', 9);
INSERT INTO reviewers (id) VALUES (24);
INSERT INTO roles (user_id, role_type, conference_id) VALUES (4, 'REVIEWER', 10);
INSERT INTO reviewers (id) VALUES (25);
INSERT INTO roles (user_id, role_type, conference_id) VALUES (4, 'REVIEWER', 11);
INSERT INTO reviewers (id) VALUES (26);
INSERT INTO roles (user_id, role_type, conference_id) VALUES (4, 'REVIEWER', 12);
INSERT INTO reviewers (id) VALUES (27);
-- Organizers (user_id 5)
INSERT INTO roles (user_id, role_type, conference_id) VALUES (5, 'ORGANIZER', 4);
INSERT INTO organizers (id) VALUES (28);
INSERT INTO roles (user_id, role_type, conference_id) VALUES (5, 'ORGANIZER', 5);
INSERT INTO organizers (id) VALUES (29);
INSERT INTO roles (user_id, role_type, conference_id) VALUES (5, 'ORGANIZER', 6);
INSERT INTO organizers (id) VALUES (30);
INSERT INTO roles (user_id, role_type, conference_id) VALUES (5, 'ORGANIZER', 7);
INSERT INTO organizers (id) VALUES (31);
INSERT INTO roles (user_id, role_type, conference_id) VALUES (5, 'ORGANIZER', 8);
INSERT INTO organizers (id) VALUES (32);
INSERT INTO roles (user_id, role_type, conference_id) VALUES (5, 'ORGANIZER', 9);
INSERT INTO organizers (id) VALUES (33);
INSERT INTO roles (user_id, role_type, conference_id) VALUES (5, 'ORGANIZER', 10);
INSERT INTO organizers (id) VALUES (34);
INSERT INTO roles (user_id, role_type, conference_id) VALUES (5, 'ORGANIZER', 11);
INSERT INTO organizers (id) VALUES (35);
INSERT INTO roles (user_id, role_type, conference_id) VALUES (5, 'ORGANIZER', 12);
INSERT INTO organizers (id) VALUES (36);
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
    ),    (
        'soumission en phase concrete',
        CURRENT_TIMESTAMP,
        'PROGRESS',
        'Ce travail utilise des modèles d''apprentissage profond pour prédire les tendances climatiques.',
        3
    );


-- Lien auteur-soumissions (table de jointure author_submission)
INSERT INTO author_submission (author_id, submission_id)
VALUES
    (5, 1),  -- Alban (author_id 5) → soumission 1
    (6, 3), -- Ibrahim (author_id 6) → soumission 3
    (9, 4) ; -- Jeremie pour conférence 3 (author_id 9) → soumission 4

-- Evaluation pour submission 1
INSERT INTO evaluations ( submission_id)
VALUES ( 1);

INSERT INTO evaluations ( submission_id)
VALUES ( 2);

INSERT INTO evaluations ( submission_id)
VALUES ( 3);

INSERT INTO evaluations ( submission_id)
VALUES ( 4);


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
           4,
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