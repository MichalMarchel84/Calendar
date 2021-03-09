DROP TABLE IF EXISTS "events";
CREATE TABLE IF NOT EXISTS "events" (
	"client_id"	INTEGER NOT NULL,
	"entry_id"	INTEGER NOT NULL,
	"start_time"	INTEGER NOT NULL,
	"end_time"	INTEGER NOT NULL,
	"title"	TEXT,
	"description"	TEXT,
	PRIMARY KEY("client_id","entry_id")
);
DROP TABLE IF EXISTS "repetitive_events";
CREATE TABLE IF NOT EXISTS "repetitive_events" (
	"client_id"	INTEGER NOT NULL,
	"entry_id"	INTEGER NOT NULL,
	"started_at"	INTEGER NOT NULL,
	"finished_at"	INTEGER,
	"start_time"	INTEGER NOT NULL,
	"end_time"	INTEGER NOT NULL,
	"title"	TEXT,
	"description"	TEXT,
	PRIMARY KEY("client_id","entry_id")
);
DROP TABLE IF EXISTS "repetitive_reminders";
CREATE TABLE IF NOT EXISTS "repetitive_reminders" (
	"client_id"	INTEGER NOT NULL,
	"entry_id"	INTEGER NOT NULL,
	"started_at"	INTEGER NOT NULL,
	"finished_at"	INTEGER,
	"time"	INTEGER NOT NULL,
	"title"	TEXT,
	"description"	TEXT,
	PRIMARY KEY("client_id","entry_id")
);
DROP TABLE IF EXISTS "clients";
CREATE TABLE IF NOT EXISTS "clients" (
	"client_id"	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	"login"	TEXT NOT NULL UNIQUE,
	"password"	TEXT NOT NULL
);