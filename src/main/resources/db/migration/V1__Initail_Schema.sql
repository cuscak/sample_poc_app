CREATE TABLE owner (
    id int AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    email VARCHAR(255) NOT NULL
);

CREATE TABLE folder (
    id int AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    parent int,
    FOREIGN KEY (parent) REFERENCES folder(id)
);

CREATE TABLE document_meta (
    created_on TIMESTAMP NOT NULL,
    updated_on TIMESTAMP,
    description TEXT,
    size int,
    document_type VARCHAR(100),
    document int
);

CREATE TABLE document (
    document_id int AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255),
    owner int,
    FOREIGN KEY (owner) REFERENCES owner(id),
    folder int,
    FOREIGN KEY (folder) REFERENCES folder(id)
);