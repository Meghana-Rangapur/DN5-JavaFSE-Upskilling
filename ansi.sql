-- ================================================================
--  CivicPulse — Community Event Portal
--  Module 2: ANSI SQL Using MySQL
--  Full script: Schema → Sample Data → 25 Exercise Queries
-- ================================================================

-- ----------------------------------------------------------------
--  DATABASE SETUP
-- ----------------------------------------------------------------
DROP DATABASE IF EXISTS civicpulse;
CREATE DATABASE civicpulse
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE civicpulse;


-- ================================================================
--  TABLE DEFINITIONS
-- ================================================================

-- 1. Users
CREATE TABLE Users (
    user_id           INT           PRIMARY KEY AUTO_INCREMENT,
    full_name         VARCHAR(100)  NOT NULL,
    email             VARCHAR(100)  UNIQUE NOT NULL,
    city              VARCHAR(100)  NOT NULL,
    registration_date DATE          NOT NULL
);

-- 2. Events
CREATE TABLE Events (
    event_id     INT            PRIMARY KEY AUTO_INCREMENT,
    title        VARCHAR(200)   NOT NULL,
    description  TEXT,
    city         VARCHAR(100)   NOT NULL,
    start_date   DATETIME       NOT NULL,
    end_date     DATETIME       NOT NULL,
    status       ENUM('upcoming','completed','cancelled') NOT NULL DEFAULT 'upcoming',
    organizer_id INT,
    CONSTRAINT fk_event_organizer FOREIGN KEY (organizer_id)
        REFERENCES Users(user_id) ON DELETE SET NULL
);

-- 3. Sessions
CREATE TABLE Sessions (
    session_id   INT           PRIMARY KEY AUTO_INCREMENT,
    event_id     INT           NOT NULL,
    title        VARCHAR(200)  NOT NULL,
    speaker_name VARCHAR(100)  NOT NULL,
    start_time   DATETIME      NOT NULL,
    end_time     DATETIME      NOT NULL,
    CONSTRAINT fk_session_event FOREIGN KEY (event_id)
        REFERENCES Events(event_id) ON DELETE CASCADE
);

-- 4. Registrations
CREATE TABLE Registrations (
    registration_id   INT   PRIMARY KEY AUTO_INCREMENT,
    user_id           INT   NOT NULL,
    event_id          INT   NOT NULL,
    registration_date DATE  NOT NULL,
    CONSTRAINT fk_reg_user  FOREIGN KEY (user_id)  REFERENCES Users(user_id)  ON DELETE CASCADE,
    CONSTRAINT fk_reg_event FOREIGN KEY (event_id) REFERENCES Events(event_id) ON DELETE CASCADE
);

-- 5. Feedback
CREATE TABLE Feedback (
    feedback_id   INT   PRIMARY KEY AUTO_INCREMENT,
    user_id       INT   NOT NULL,
    event_id      INT   NOT NULL,
    rating        INT   CHECK (rating BETWEEN 1 AND 5),
    comments      TEXT,
    feedback_date DATE  NOT NULL,
    CONSTRAINT fk_fb_user  FOREIGN KEY (user_id)  REFERENCES Users(user_id)  ON DELETE CASCADE,
    CONSTRAINT fk_fb_event FOREIGN KEY (event_id) REFERENCES Events(event_id) ON DELETE CASCADE
);

-- 6. Resources
CREATE TABLE Resources (
    resource_id   INT            PRIMARY KEY AUTO_INCREMENT,
    event_id      INT            NOT NULL,
    resource_type ENUM('pdf','image','link') NOT NULL,
    resource_url  VARCHAR(255)   NOT NULL,
    uploaded_at   DATETIME       NOT NULL,
    CONSTRAINT fk_res_event FOREIGN KEY (event_id)
        REFERENCES Events(event_id) ON DELETE CASCADE
);


-- ================================================================
--  SAMPLE DATA
-- ================================================================

-- Users
INSERT INTO Users (full_name, email, city, registration_date) VALUES
  ('Alice Johnson', 'alice@example.com', 'New York',    '2024-12-01'),
  ('Bob Smith',     'bob@example.com',   'Los Angeles', '2024-12-05'),
  ('Charlie Lee',   'charlie@example.com','Chicago',    '2024-12-10'),
  ('Diana King',    'diana@example.com', 'New York',    '2025-01-15'),
  ('Ethan Hunt',    'ethan@example.com', 'Los Angeles', '2025-02-01');

-- Events
INSERT INTO Events (title, description, city, start_date, end_date, status, organizer_id) VALUES
  ('Tech Innovators Meetup',      'A meetup for tech enthusiasts.',           'New York',    '2025-06-10 10:00:00', '2025-06-10 16:00:00', 'upcoming',  1),
  ('AI & ML Conference',          'Conference on AI and ML advancements.',    'Chicago',     '2025-05-15 09:00:00', '2025-05-15 17:00:00', 'completed', 3),
  ('Frontend Development Bootcamp','Hands-on training on frontend tech.',     'Los Angeles', '2025-07-01 10:00:00', '2025-07-03 16:00:00', 'upcoming',  2);

-- Sessions
INSERT INTO Sessions (event_id, title, speaker_name, start_time, end_time) VALUES
  (1, 'Opening Keynote',    'Dr. Tech',      '2025-06-10 10:00:00', '2025-06-10 11:00:00'),
  (1, 'Future of Web Dev',  'Alice Johnson', '2025-06-10 11:15:00', '2025-06-10 12:30:00'),
  (2, 'AI in Healthcare',   'Charlie Lee',   '2025-05-15 09:30:00', '2025-05-15 11:00:00'),
  (3, 'Intro to HTML5',     'Bob Smith',     '2025-07-01 10:00:00', '2025-07-01 12:00:00');

-- Registrations
INSERT INTO Registrations (user_id, event_id, registration_date) VALUES
  (1, 1, '2025-05-01'),
  (2, 1, '2025-05-02'),
  (3, 2, '2025-04-30'),
  (4, 2, '2025-04-28'),
  (5, 3, '2025-06-15');

-- Feedback
INSERT INTO Feedback (user_id, event_id, rating, comments, feedback_date) VALUES
  (3, 2, 4, 'Great insights!',      '2025-05-16'),
  (4, 2, 5, 'Very informative.',    '2025-05-16'),
  (2, 1, 3, 'Could be better.',     '2025-06-11');

-- Resources
INSERT INTO Resources (event_id, resource_type, resource_url, uploaded_at) VALUES
  (1, 'pdf',   'https://portal.com/resources/tech_meetup_agenda.pdf', '2025-05-01 10:00:00'),
  (2, 'image', 'https://portal.com/resources/ai_poster.jpg',          '2025-04-20 09:00:00'),
  (3, 'link',  'https://portal.com/resources/html5_docs',             '2025-06-25 15:00:00');


-- ================================================================
--  EXERCISE QUERIES
-- ================================================================


-- ----------------------------------------------------------------
-- Exercise 1: User Upcoming Events
-- Show all upcoming events a user is registered for in their city,
-- sorted by start date.
-- ----------------------------------------------------------------
-- Usage: Replace 1 with any user_id you want to query
SELECT
    e.event_id,
    e.title        AS event_title,
    e.city,
    e.start_date,
    e.end_date,
    e.status
FROM Registrations r
JOIN Events e  ON e.event_id = r.event_id
JOIN Users  u  ON u.user_id  = r.user_id
WHERE r.user_id   = 1               -- change to target user
  AND e.status    = 'upcoming'
  AND e.city      = u.city          -- same city as the user
ORDER BY e.start_date ASC;


-- ----------------------------------------------------------------
-- Exercise 2: Top Rated Events
-- Events with the highest average rating (min 10 feedbacks).
-- NOTE: sample data has < 10 feedbacks; threshold lowered to 1
--       for demonstration. Replace 1 with 10 in production.
-- ----------------------------------------------------------------
SELECT
    e.event_id,
    e.title,
    COUNT(f.feedback_id)   AS total_feedback,
    ROUND(AVG(f.rating), 2) AS avg_rating
FROM Events e
JOIN Feedback f ON f.event_id = e.event_id
GROUP BY e.event_id, e.title
HAVING COUNT(f.feedback_id) >= 1        -- change to >= 10 in production
ORDER BY avg_rating DESC;


-- ----------------------------------------------------------------
-- Exercise 3: Inactive Users
-- Users who have NOT registered for any event in the last 90 days.
-- ----------------------------------------------------------------
SELECT
    u.user_id,
    u.full_name,
    u.email,
    u.city,
    MAX(r.registration_date) AS last_registration
FROM Users u
LEFT JOIN Registrations r ON r.user_id = u.user_id
GROUP BY u.user_id, u.full_name, u.email, u.city
HAVING MAX(r.registration_date) < CURDATE() - INTERVAL 90 DAY
    OR MAX(r.registration_date) IS NULL
ORDER BY last_registration ASC;


-- ----------------------------------------------------------------
-- Exercise 4: Peak Session Hours
-- Count sessions scheduled between 10 AM and 12 PM per event.
-- ----------------------------------------------------------------
SELECT
    e.event_id,
    e.title         AS event_title,
    COUNT(s.session_id) AS sessions_10am_to_12pm
FROM Events e
JOIN Sessions s ON s.event_id = e.event_id
WHERE TIME(s.start_time) >= '10:00:00'
  AND TIME(s.start_time) <  '12:00:00'
GROUP BY e.event_id, e.title
ORDER BY sessions_10am_to_12pm DESC;


-- ----------------------------------------------------------------
-- Exercise 5: Most Active Cities
-- Top 5 cities by number of distinct user registrations.
-- ----------------------------------------------------------------
SELECT
    u.city,
    COUNT(DISTINCT r.user_id) AS distinct_registrations
FROM Users u
JOIN Registrations r ON r.user_id = u.user_id
GROUP BY u.city
ORDER BY distinct_registrations DESC
LIMIT 5;


-- ----------------------------------------------------------------
-- Exercise 6: Event Resource Summary
-- Report: number of PDFs, images, and links per event.
-- ----------------------------------------------------------------
SELECT
    e.event_id,
    e.title AS event_title,
    COUNT(res.resource_id)                                  AS total_resources,
    SUM(res.resource_type = 'pdf')                          AS pdf_count,
    SUM(res.resource_type = 'image')                        AS image_count,
    SUM(res.resource_type = 'link')                         AS link_count
FROM Events e
LEFT JOIN Resources res ON res.event_id = e.event_id
GROUP BY e.event_id, e.title
ORDER BY total_resources DESC;


-- ----------------------------------------------------------------
-- Exercise 7: Low Feedback Alerts
-- Users who gave a rating < 3, with comments and event name.
-- ----------------------------------------------------------------
SELECT
    u.user_id,
    u.full_name,
    u.email,
    e.title   AS event_title,
    f.rating,
    f.comments,
    f.feedback_date
FROM Feedback f
JOIN Users  u ON u.user_id  = f.user_id
JOIN Events e ON e.event_id = f.event_id
WHERE f.rating < 3
ORDER BY f.rating ASC, f.feedback_date DESC;


-- ----------------------------------------------------------------
-- Exercise 8: Sessions per Upcoming Event
-- Upcoming events with their session count.
-- ----------------------------------------------------------------
SELECT
    e.event_id,
    e.title          AS event_title,
    e.city,
    e.start_date,
    COUNT(s.session_id) AS session_count
FROM Events e
LEFT JOIN Sessions s ON s.event_id = e.event_id
WHERE e.status = 'upcoming'
GROUP BY e.event_id, e.title, e.city, e.start_date
ORDER BY session_count DESC;


-- ----------------------------------------------------------------
-- Exercise 9: Organizer Event Summary
-- For each organizer: events created grouped by status.
-- ----------------------------------------------------------------
SELECT
    u.user_id                AS organizer_id,
    u.full_name              AS organizer_name,
    e.status,
    COUNT(e.event_id)        AS event_count
FROM Users u
JOIN Events e ON e.organizer_id = u.user_id
GROUP BY u.user_id, u.full_name, e.status
ORDER BY u.user_id, e.status;


-- ----------------------------------------------------------------
-- Exercise 10: Feedback Gap
-- Events that had registrations but received NO feedback at all.
-- ----------------------------------------------------------------
SELECT
    e.event_id,
    e.title     AS event_title,
    e.status,
    COUNT(DISTINCT r.registration_id) AS total_registrations
FROM Events e
JOIN Registrations r ON r.event_id = e.event_id
LEFT JOIN Feedback  f ON f.event_id = e.event_id
WHERE f.feedback_id IS NULL
GROUP BY e.event_id, e.title, e.status
ORDER BY total_registrations DESC;


-- ----------------------------------------------------------------
-- Exercise 11: Daily New User Count
-- Number of users who registered each day in the last 7 days.
-- ----------------------------------------------------------------
SELECT
    registration_date        AS signup_date,
    COUNT(user_id)           AS new_users
FROM Users
WHERE registration_date >= CURDATE() - INTERVAL 7 DAY
GROUP BY registration_date
ORDER BY registration_date DESC;


-- ----------------------------------------------------------------
-- Exercise 12: Event with Maximum Sessions
-- Event(s) that have the highest number of sessions.
-- ----------------------------------------------------------------
SELECT
    e.event_id,
    e.title          AS event_title,
    COUNT(s.session_id) AS session_count
FROM Events e
JOIN Sessions s ON s.event_id = e.event_id
GROUP BY e.event_id, e.title
HAVING COUNT(s.session_id) = (
    SELECT MAX(cnt)
    FROM (
        SELECT COUNT(session_id) AS cnt
        FROM Sessions
        GROUP BY event_id
    ) AS session_counts
)
ORDER BY e.event_id;


-- ----------------------------------------------------------------
-- Exercise 13: Average Rating per City
-- Average feedback rating for events held in each city.
-- ----------------------------------------------------------------
SELECT
    e.city,
    COUNT(DISTINCT e.event_id)   AS events_with_feedback,
    ROUND(AVG(f.rating), 2)      AS avg_rating
FROM Events e
JOIN Feedback f ON f.event_id = e.event_id
GROUP BY e.city
ORDER BY avg_rating DESC;


-- ----------------------------------------------------------------
-- Exercise 14: Most Registered Events
-- Top 3 events by total user registrations.
-- ----------------------------------------------------------------
SELECT
    e.event_id,
    e.title                        AS event_title,
    COUNT(r.registration_id)       AS total_registrations
FROM Events e
JOIN Registrations r ON r.event_id = e.event_id
GROUP BY e.event_id, e.title
ORDER BY total_registrations DESC
LIMIT 3;


-- ----------------------------------------------------------------
-- Exercise 15: Event Session Time Conflict
-- Overlapping sessions within the same event.
-- Two sessions A and B overlap when:
--   A.start_time < B.end_time AND A.end_time > B.start_time
--   (and they are different sessions)
-- ----------------------------------------------------------------
SELECT
    s1.event_id,
    e.title          AS event_title,
    s1.session_id    AS session_a_id,
    s1.title         AS session_a_title,
    s1.start_time    AS a_start,
    s1.end_time      AS a_end,
    s2.session_id    AS session_b_id,
    s2.title         AS session_b_title,
    s2.start_time    AS b_start,
    s2.end_time      AS b_end
FROM Sessions s1
JOIN Sessions s2 ON  s2.event_id   = s1.event_id
                 AND s2.session_id > s1.session_id   -- avoid duplicates
JOIN Events   e  ON  e.event_id    = s1.event_id
WHERE s1.start_time < s2.end_time
  AND s1.end_time   > s2.start_time
ORDER BY s1.event_id, s1.session_id;


-- ----------------------------------------------------------------
-- Exercise 16: Unregistered Active Users
-- Users who signed up in the last 30 days with NO event registrations.
-- ----------------------------------------------------------------
SELECT
    u.user_id,
    u.full_name,
    u.email,
    u.city,
    u.registration_date
FROM Users u
LEFT JOIN Registrations r ON r.user_id = u.user_id
WHERE u.registration_date >= CURDATE() - INTERVAL 30 DAY
  AND r.registration_id IS NULL
ORDER BY u.registration_date DESC;


-- ----------------------------------------------------------------
-- Exercise 17: Multi-Session Speakers
-- Speakers handling more than one session across all events.
-- ----------------------------------------------------------------
SELECT
    speaker_name,
    COUNT(session_id)          AS total_sessions,
    GROUP_CONCAT(
        CONCAT(title, ' (Event #', event_id, ')')
        ORDER BY event_id
        SEPARATOR ' | '
    )                          AS sessions_detail
FROM Sessions
GROUP BY speaker_name
HAVING COUNT(session_id) > 1
ORDER BY total_sessions DESC;


-- ----------------------------------------------------------------
-- Exercise 18: Resource Availability Check
-- Events that have NO resources uploaded.
-- ----------------------------------------------------------------
SELECT
    e.event_id,
    e.title  AS event_title,
    e.city,
    e.status
FROM Events e
LEFT JOIN Resources res ON res.event_id = e.event_id
WHERE res.resource_id IS NULL
ORDER BY e.event_id;


-- ----------------------------------------------------------------
-- Exercise 19: Completed Events with Feedback Summary
-- For each completed event: total registrations + avg rating.
-- ----------------------------------------------------------------
SELECT
    e.event_id,
    e.title                            AS event_title,
    e.city,
    COUNT(DISTINCT r.registration_id)  AS total_registrations,
    COUNT(DISTINCT f.feedback_id)      AS total_feedback,
    ROUND(AVG(f.rating), 2)            AS avg_rating
FROM Events e
LEFT JOIN Registrations r ON r.event_id = e.event_id
LEFT JOIN Feedback      f ON f.event_id = e.event_id
WHERE e.status = 'completed'
GROUP BY e.event_id, e.title, e.city
ORDER BY avg_rating DESC;


-- ----------------------------------------------------------------
-- Exercise 20: User Engagement Index
-- For each user: events attended (registered) + feedbacks submitted.
-- ----------------------------------------------------------------
SELECT
    u.user_id,
    u.full_name,
    u.city,
    COUNT(DISTINCT r.event_id)      AS events_registered,
    COUNT(DISTINCT f.feedback_id)   AS feedbacks_submitted,
    -- Simple engagement score: registrations + feedbacks
    (COUNT(DISTINCT r.event_id) + COUNT(DISTINCT f.feedback_id))
                                    AS engagement_score
FROM Users u
LEFT JOIN Registrations r ON r.user_id = u.user_id
LEFT JOIN Feedback      f ON f.user_id = u.user_id
GROUP BY u.user_id, u.full_name, u.city
ORDER BY engagement_score DESC;


-- ----------------------------------------------------------------
-- Exercise 21: Top Feedback Providers
-- Top 5 users by number of feedback entries submitted.
-- ----------------------------------------------------------------
SELECT
    u.user_id,
    u.full_name,
    u.email,
    COUNT(f.feedback_id)        AS feedback_count,
    ROUND(AVG(f.rating), 2)     AS avg_rating_given
FROM Users u
JOIN Feedback f ON f.user_id = u.user_id
GROUP BY u.user_id, u.full_name, u.email
ORDER BY feedback_count DESC
LIMIT 5;


-- ----------------------------------------------------------------
-- Exercise 22: Duplicate Registrations Check
-- Users registered more than once for the same event.
-- ----------------------------------------------------------------
SELECT
    r.user_id,
    u.full_name,
    r.event_id,
    e.title          AS event_title,
    COUNT(r.registration_id) AS registration_count
FROM Registrations r
JOIN Users  u ON u.user_id  = r.user_id
JOIN Events e ON e.event_id = r.event_id
GROUP BY r.user_id, u.full_name, r.event_id, e.title
HAVING COUNT(r.registration_id) > 1
ORDER BY registration_count DESC;


-- ----------------------------------------------------------------
-- Exercise 23: Registration Trends
-- Month-wise registration count over the past 12 months.
-- ----------------------------------------------------------------
SELECT
    DATE_FORMAT(registration_date, '%Y-%m') AS month,
    COUNT(registration_id)                  AS registrations
FROM Registrations
WHERE registration_date >= DATE_FORMAT(
          CURDATE() - INTERVAL 12 MONTH, '%Y-%m-01'
      )
GROUP BY DATE_FORMAT(registration_date, '%Y-%m')
ORDER BY month ASC;


-- ----------------------------------------------------------------
-- Exercise 24: Average Session Duration per Event
-- Average duration of sessions (in minutes) for each event.
-- ----------------------------------------------------------------
SELECT
    e.event_id,
    e.title         AS event_title,
    COUNT(s.session_id)                       AS session_count,
    ROUND(
        AVG(
            TIMESTAMPDIFF(MINUTE, s.start_time, s.end_time)
        ), 1
    )                                          AS avg_duration_minutes
FROM Events e
JOIN Sessions s ON s.event_id = e.event_id
GROUP BY e.event_id, e.title
ORDER BY avg_duration_minutes DESC;


-- ----------------------------------------------------------------
-- Exercise 25: Events Without Sessions
-- All events that have no sessions scheduled.
-- ----------------------------------------------------------------
SELECT
    e.event_id,
    e.title  AS event_title,
    e.city,
    e.status,
    e.start_date
FROM Events e
LEFT JOIN Sessions s ON s.event_id = e.event_id
WHERE s.session_id IS NULL
ORDER BY e.start_date ASC;


-- ================================================================
--  END OF SCRIPT
-- ================================================================