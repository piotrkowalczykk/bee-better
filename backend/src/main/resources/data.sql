INSERT INTO roles (name)
SELECT 'USER'
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'CUSTOMER');

INSERT INTO roles (name)
SELECT 'ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ADMIN');

INSERT INTO users (email, password, username, email_verified)
SELECT
    'admin@test.com',
    'admin123',
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
    'peter123',
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
SELECT '/images/dumbbell-biceps-curls.webp', 'biceps', 'dumbbell-biceps-curls', 'DUMBBELL'
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'dumbbell-biceps-curls');

INSERT INTO exercises (image, muscle_group, name, equipment)
SELECT '/images/dumbbell-kickback.jpg', 'tricpes', 'dumbbell-kickback', 'DUMBBELL'
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'dumbbell-kickback');