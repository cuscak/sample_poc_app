INSERT INTO owner (username, first_name, last_name, email)
VALUES
    ('user1', 'John', 'Doe', 'john.doe@example.com'),
    ('user2', 'Jane', 'Smith', 'jane.smith@example.com'),
    ('user3', 'Alice', 'Johnson', 'alice.johnson@example.com');

INSERT INTO folder (name, parent)
VALUES
    ('Root Folder', NULL), -- Root folder (parent for other 2)
    ('Subfolder 1', 1),    -- Child folder 1
    ('Subfolder 2', 1);    -- Child folder 2


INSERT INTO document (title, owner, folder)
VALUES
    ('Document 1', 1, 2), -- Document 1 in Subfolder 1
    ('Document 2', 2, 3), -- Document 2 in Subfolder 2
    ('Document 3', 3, 3); -- Document 3 in Subfolder 2