INSERT INTO roles (name)
SELECT 'USER'
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'CUSTOMER');

INSERT INTO roles (name)
SELECT 'ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ADMIN');

INSERT INTO users (email, password, username, email_verified)
SELECT
    'admin@test.com',
    '$2a$10$HGF1SRGKZ93rkQ63sBsy5uxG6Cs5ZRFaQUwbOfkXqTLvfs8qeHQDm',
    'admin',
    true
WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE email = 'admin@test.com'
);

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u, roles r
WHERE u.email = 'admin@test.com'
  AND r.name = 'USER'
  AND NOT EXISTS (
      SELECT 1 FROM user_roles ur
      WHERE ur.user_id = u.id AND ur.role_id = r.id
);


INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u, roles r
WHERE u.email = 'admin@test.com'
  AND r.name = 'ADMIN'
  AND NOT EXISTS (
      SELECT 1 FROM user_roles ur
      WHERE ur.user_id = u.id AND ur.role_id = r.id
);

INSERT INTO users (email, password, username, email_verified)
SELECT
    'peter@test.com',
    '$2a$10$UtE0qfE91CCVbPh65AsjtOKl6cebkp0eGZ9XkLGVrdXDiveQUOuJW',
    'peter2004',
    true
WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE email = 'peter@test.com'
);

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u, roles r
WHERE u.email = 'peter@test.com'
  AND r.name = 'USER'
  AND NOT EXISTS (
      SELECT 1 FROM user_roles ur
      WHERE ur.user_id = u.id AND ur.role_id = r.id
);



INSERT INTO exercises (image, muscle_group, name, equipment)
SELECT '/images/exercises/dumbbell-biceps-curls.webp', 'Biceps', 'Dumbbell biceps curls', 'DUMBBELL'
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'Dumbbell biceps curls');

INSERT INTO exercises (image, muscle_group, name, equipment)
SELECT '/images/exercises/dumbbell-kickback.jpg', 'Triceps', 'Dumbbell kickback', 'DUMBBELL'
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'Dumbbell kickback');

INSERT INTO exercises (image, muscle_group, name, equipment)
SELECT '/images/exercises/barbell-chest-press.jpg', 'Chest', 'Barbell chest press', 'BARBELL'
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'Barbell chest press');

INSERT INTO exercises (image, muscle_group, name, equipment)
SELECT '/images/exercises/barbell-squat.jpg', 'Legs', 'Barbell Squat', 'BARBELL'
WHERE NOT EXISTS (SELECT 1 FROM exercises WHERE name = 'Barbell Squat');

INSERT INTO exercises (image, muscle_group, name, equipment)
SELECT '/images/exercises/pull-ups.jpg', 'Back', 'Pull-ups', 'BODYWEIGHT'
WHERE NOT EXISTS (SELECT 1 FROM exercises WHERE name = 'Pull-ups');

INSERT INTO exercises (image, muscle_group, name, equipment)
SELECT '/images/exercises/lateral-raises.jpg', 'Shoulders', 'Dumbbell Lateral Raises', 'DUMBBELL'
WHERE NOT EXISTS (SELECT 1 FROM exercises WHERE name = 'Dumbbell Lateral Raises');

INSERT INTO exercises (image, muscle_group, name, equipment)
SELECT '/images/exercises/cable-triceps-pushdown.jpg', 'Triceps', 'Cable triceps pushdown', 'CABLE'
WHERE NOT EXISTS (SELECT 1 FROM exercises WHERE name = 'Cable triceps pushdown');

INSERT INTO exercises (image, muscle_group, name, equipment)
SELECT '/images/exercises/incline-dumbbell-bench-press.jpg', 'Chest', 'Incline dumbbell bench press', 'DUMBBELL'
WHERE NOT EXISTS (SELECT 1 FROM exercises WHERE name = 'Incline dumbbell bench press');

INSERT INTO exercises (image, muscle_group, name, equipment)
SELECT '/images/exercises/dips.jpg', 'Triceps', 'Dips', 'BODYWEIGHT'
WHERE NOT EXISTS (SELECT 1 FROM exercises WHERE name = 'Dips');

INSERT INTO exercises (image, muscle_group, name, equipment)
SELECT '/images/exercises/dumbbell-triceps-extension.jpg', 'Triceps', 'Dumbbell triceps extension', 'DUMBBELL'
WHERE NOT EXISTS (SELECT 1 FROM exercises WHERE name = 'Dumbbell triceps extension');

INSERT INTO exercises (image, muscle_group, name, equipment)
SELECT '/images/exercises/dumbbell-hammer-curls.jpg', 'Biceps', 'Dumbbell hammer curls', 'DUMBBELL'
WHERE NOT EXISTS (SELECT 1 FROM exercises WHERE name = 'Dumbbell hammer curls');